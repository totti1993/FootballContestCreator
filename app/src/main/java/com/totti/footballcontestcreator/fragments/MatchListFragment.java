package com.totti.footballcontestcreator.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.adapters.MatchListAdapter;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MatchListFragment extends Fragment implements MatchListAdapter.OnMatchClickedListener {

	private String id;
	private String tournamentType;

	private DatabaseReference onlineMatches;

	private MatchViewModel matchViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_list_fragment, container, false);

		id = this.getArguments().getString("id");
		tournamentType = this.getArguments().getString("tournamentType");
		String type = this.getArguments().getString("type");
		String tab = this.getArguments().getString("tab");

		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");

		final MatchListAdapter matchListAdapter = new MatchListAdapter(this);

		matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
		if(type != null && tab != null && tournamentType != null) {
			if(type.equals("team")) {
				if(tab.equals("matches")) {
					matchViewModel.getAllMatchesByTeamAndFinalScore(id, false).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
				else if(tab.equals("results")) {
					matchViewModel.getAllMatchesByTeamAndFinalScore(id, true).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
			}
			else if(type.equals("tournament")) {
				if(tab.equals("matches")) {
					matchViewModel.getAllMatchesByTournamentAndFinalScore(id, false).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);

							if(matches.isEmpty() && tournamentType.equals("Elimination")) {
								final RankingViewModel rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
								new AsyncTask<Void, Void, List<Ranking>>() {
									@Override
									protected List<Ranking> doInBackground(Void... voids) {
										return rankingViewModel.getAllActiveRankingsByTournamentAsync(id);
									}

									@Override
									protected void onPostExecute(final List<Ranking> rankings) {
										if(rankings.size() > 1) {
											new AlertDialog.Builder(requireContext()).setMessage("All matches finished in this round!\nReady to generate the next round?")
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
					});
				}
				else if(tab.equals("results")) {
					matchViewModel.getAllMatchesByTournamentAndFinalScore(id, true).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
			}
		}

		RecyclerView recyclerView = rootView.findViewById(R.id.match_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(matchListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		return rootView;
	}

	@Override
	public void onMatchClicked(Match match) {
		Bundle args = new Bundle();
		args.putString("id", match.getId());

		if(match.getFinal_score()) {
			ResultDetailsDialogFragment resultDetailsDialogFragment = new ResultDetailsDialogFragment();
			resultDetailsDialogFragment.setArguments(args);
			resultDetailsDialogFragment.show(requireActivity().getSupportFragmentManager(), ResultDetailsDialogFragment.TAG);
		}
		else {
			MatchDetailsDialogFragment matchDetailsDialogFragment = new MatchDetailsDialogFragment();
			matchDetailsDialogFragment.setArguments(args);
			matchDetailsDialogFragment.show(requireActivity().getSupportFragmentManager(), MatchDetailsDialogFragment.TAG);
		}
	}

	private void generateNextRound(List<Ranking> rankings) {
		final ArrayList<ArrayList<Ranking>> pairs = new ArrayList<>();

		int numberOfMatches = rankings.size() / 2;

		Random rand = new Random();
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

				final TournamentViewModel tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);
				new AsyncTask<Void, Void, Tournament>() {
					@Override
					protected Tournament doInBackground(Void... voids) {
						return tournamentViewModel.getTournamentByIdAsync(id);
					}

					@Override
					protected void onPostExecute(Tournament tournament) {
						for(int i = 1; i <= tournament.getRounds(); i++) {
							for(ArrayList<Ranking> pair : pairs) {
								String matchId = onlineMatches.push().getKey();
								Match newMatch;

								if(i % 2 != 0) {
									newMatch = new Match(matchId, tournament.getId(), tournament.getName(), matchDay + i, pair.get(0).getTeam_id(), pair.get(0).getTeam_name(), pair.get(1).getTeam_id(), pair.get(1).getTeam_name());
								}
								else {
									newMatch = new Match(matchId, tournament.getId(), tournament.getName(), matchDay + i, pair.get(1).getTeam_id(), pair.get(1).getTeam_name(), pair.get(0).getTeam_id(), pair.get(0).getTeam_name());
								}

								onlineMatches.child(matchId).setValue(newMatch);
							}
						}
					}
				}.execute();
			}
		}.execute();

		Toast.makeText(requireContext(), "Next round generated!", Toast.LENGTH_SHORT).show();
	}
}
