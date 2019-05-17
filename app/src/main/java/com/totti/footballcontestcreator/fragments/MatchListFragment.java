package com.totti.footballcontestcreator.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.adapters.MatchListAdapter;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MatchListFragment extends Fragment implements MatchListAdapter.OnMatchClickedListener {

	private long id;

	private MatchViewModel matchViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_list_fragment, container, false);

		id = this.getArguments().getLong("id");
		String type = this.getArguments().getString("type");
		String tournamentType = this.getArguments().getString("tournamentType");
		String tab = this.getArguments().getString("tab");

		final MatchListAdapter matchListAdapter = new MatchListAdapter(this);

		matchViewModel = ViewModelProviders.of(getActivity()).get(MatchViewModel.class);
		if(type != null && tab != null && tournamentType != null) {
			if(type.equals("team")) {
				if(tab.equals("matches")) {
					matchViewModel.getAllMatchesByTeamAndFinalScore(id, false).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
				else if(tab.equals("results")) {
					matchViewModel.getAllMatchesByTeamAndFinalScore(id, true).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
			}
			else if(type.equals("tournament")) {
				if(tab.equals("matches")) {
					matchViewModel.getAllMatchesByTournamentAndFinalScore(id, false).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});

					if(tournamentType.equals("Elimination")) {
						new AsyncTask<Void, Void, List<Match>>() {
							@Override
							protected List<Match> doInBackground(Void... voids) {
								return matchViewModel.getAllMatchesByTournamentAndFinalScoreAsync(id, false);
							}

							@Override
							protected void onPostExecute(List<Match> matches) {
								if(matches.isEmpty()) {
									final RankingViewModel rankingViewModel = ViewModelProviders.of(getActivity()).get(RankingViewModel.class);
									new AsyncTask<Void, Void, List<Ranking>>() {
										@Override
										protected List<Ranking> doInBackground(Void... voids) {
											return rankingViewModel.getAllActiveRankingsByTournament(id);
										}

										@Override
										protected void onPostExecute(final List<Ranking> rankings) {
											if(rankings.size() > 1) {
												new AlertDialog.Builder(getContext()).setMessage("All matches finished in this round!\nReady to generate the next round?")
														.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
															@Override
															public void onClick(DialogInterface dialog, int which) {
																generateNextRound(rankings);
															}
														})
														.setNegativeButton("No", null)
														.show();
											}
										}
									}.execute();
								}
							}
						}.execute();
					}
				}
				else if(tab.equals("results")) {
					matchViewModel.getAllMatchesByTournamentAndFinalScore(id, true).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
			}
		}

		RecyclerView recyclerView = rootView.findViewById(R.id.match_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(matchListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		return rootView;
	}

	@Override
	public void onMatchClicked(Match match) {
		Bundle args = new Bundle();
		args.putLong("id", match.getId());
		args.putLong("tournament_id", match.getTournament_id());
		args.putString("tournament_name", match.getTournament_name());
		args.putInt("match_day", match.getMatch_day());
		args.putLong("home_id", match.getHome_id());
		args.putString("home_name", match.getHome_name());
		args.putInt("home_score", match.getHome_score());
		args.putLong("visitor_id", match.getVisitor_id());
		args.putString("visitor_name", match.getVisitor_name());
		args.putInt("visitor_score", match.getVisitor_score());
		args.putString("comments", match.getComments());
		args.putBoolean("final_score", match.getFinal_score());

		if(match.getFinal_score()) {
			ResultDetailsDialogFragment resultDetailsDialogFragment = new ResultDetailsDialogFragment();
			resultDetailsDialogFragment.setArguments(args);
			resultDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), ResultDetailsDialogFragment.TAG);
		}
		else {
			MatchDetailsDialogFragment matchDetailsDialogFragment = new MatchDetailsDialogFragment();
			matchDetailsDialogFragment.setArguments(args);
			matchDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), MatchDetailsDialogFragment.TAG);
		}
	}

	private void generateNextRound(List<Ranking> rankings) {
		final ArrayList<ArrayList<Ranking>> pairs = new ArrayList<>();

		int numberOfMatches = rankings.size() / 2;

		final Random rand = new Random();
		for(int i = 0; i < numberOfMatches; i++) {
			Ranking firstRanking = rankings.get(rand.nextInt(rankings.size()));
			rankings.remove(firstRanking);
			Ranking secondRanking = rankings.get(rand.nextInt(rankings.size()));
			rankings.remove(secondRanking);

			ArrayList<Ranking> newPair = new ArrayList<>();
			newPair.add(firstRanking);
			newPair.add(secondRanking);
			Collections.shuffle(newPair);
			pairs.add(newPair);
		}

		Collections.shuffle(pairs);

		new AsyncTask<Void, Void, List<Match>>() {
			@Override
			protected List<Match> doInBackground(Void... voids) {
				return matchViewModel.getAllMatchesByTournamentAndFinalScoreAsync(id, true);
			}

			@Override
			protected void onPostExecute(List<Match> matches) {
				int rounds = 0;
				for(Match match : matches) {
					if(match.getMatch_day() > rounds) {
						rounds = match.getMatch_day();
					}
				}
				final int matchDay = rounds;

				final TournamentViewModel tournamentViewModel = ViewModelProviders.of(getActivity()).get(TournamentViewModel.class);
				new AsyncTask<Void, Void, Tournament>() {
					@Override
					protected Tournament doInBackground(Void... voids) {
						return tournamentViewModel.getTournamentById(id);
					}

					@Override
					protected void onPostExecute(final Tournament tournament) {
						for(int i = 1; i <= tournament.getRounds(); i++) {
							for(final ArrayList<Ranking> match : pairs) {
								if(i % 2 != 0) {
									new AsyncTask<Integer, Void, Void>() {
										@Override
										protected Void doInBackground(Integer... day) {
											matchViewModel.insert(new Match(tournament.getId(), tournament.getName(), matchDay + day[0], match.get(0).getTeam_id(), match.get(0).getTeam_name(), match.get(1).getTeam_id(), match.get(1).getTeam_name()));
											return null;
										}
									}.execute(i);
								}
								else {
									new AsyncTask<Integer, Void, Void>() {
										@Override
										protected Void doInBackground(Integer... day) {
											matchViewModel.insert(new Match(tournament.getId(), tournament.getName(), matchDay + day[0], match.get(1).getTeam_id(), match.get(1).getTeam_name(), match.get(0).getTeam_id(), match.get(0).getTeam_name()));
											return null;
										}
									}.execute(i);
								}
							}
						}
					}
				}.execute();
			}
		}.execute();

		Toast.makeText(getContext(), "Next round generated!", Toast.LENGTH_SHORT).show();
	}
}
