package com.totti.footballcontestcreator.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MatchListFragment extends Fragment implements MatchListAdapter.OnMatchClickedListener {

	private String id;
	private String tournamentType;
	private String type;
	private String tab;

	private DatabaseReference onlineMatches;

	private MatchListAdapter matchListAdapter;

	private MatchViewModel matchViewModel;
	private RankingViewModel rankingViewModel;
	private TournamentViewModel tournamentViewModel;

	private boolean alreadyAsked;   // Variable to store if round generation for Elimination has already been asked

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_list_fragment, container, false);

		id = this.getArguments().getString("id");
		type = this.getArguments().getString("type");
		tournamentType = this.getArguments().getString("tournamentType");
		tab = this.getArguments().getString("tab");

		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");

		matchListAdapter = new MatchListAdapter(this);

		matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
		rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);

		alreadyAsked = false;

		RecyclerView recyclerView = rootView.findViewById(R.id.match_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(matchListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.shared_action_bar, menu);

		/* Spinner: Match filter
		 *     Tournaments: filter by teams
		 *     Teams: filter by tournaments
		 */
		MenuItem item = menu.findItem(R.id.action_spinner_filter);
		Spinner spinner = (Spinner) item.getActionView();

		final ArrayList<String> items = new ArrayList<>();

		final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		final Map<String, String> validItemIds = new HashMap<>();

		// Team: Fill the spinner with all tournaments the team participates in
		if(type.equals("team")) {
			rankingViewModel.getAllRankingsByTeam(id).observe(this, new Observer<List<Ranking>>() {
				@Override
				public void onChanged(List<Ranking> rankings) {
					validItemIds.clear();
					for(Ranking ranking: rankings) {
						validItemIds.put(ranking.getTournament_name(), ranking.getTournament_id());
					}

					items.clear();
					items.add("All tournaments");
					for(Ranking ranking: rankings) {
						items.add(ranking.getTournament_name());
					}
					adapter.notifyDataSetChanged();
				}
			});
		}
		// Tournament: Fill the spinner with all teams the tournament has
		else if(type.equals("tournament")) {
			rankingViewModel.getAllRankingsByTournament(id).observe(this, new Observer<List<Ranking>>() {
				@Override
				public void onChanged(List<Ranking> rankings) {
					validItemIds.clear();
					for(Ranking ranking: rankings) {
						validItemIds.put(ranking.getTeam_name(), ranking.getTeam_id());
					}

					items.clear();
					items.add("All teams");
					for(Ranking ranking: rankings) {
						items.add(ranking.getTeam_name());
					}
					adapter.notifyDataSetChanged();
				}
			});
		}

		// Listener: Click on spinner item and change filter
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id_number) {
				String validItemId = validItemIds.get(parent.getItemAtPosition(position).toString());

				// Remove previous observer not to observe multiple LiveData at once
				if(matchViewModel.getAllMatchesByTeamTournamentAndFinalScore(null, null, false) != null) {
					matchViewModel.getAllMatchesByTeamTournamentAndFinalScore(null, null, false).removeObservers(getViewLifecycleOwner());
				}

				if(type != null && tournamentType != null && tab != null) {
					if(type.equals("team")) {
						if(tab.equals("matches")) {
							matchViewModel.getAllMatchesByTeamTournamentAndFinalScore(id, validItemId, false).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
								@Override
								public void onChanged(List<Match> matches) {
									matchListAdapter.setMatches(matches);
								}
							});
						}
						else if(tab.equals("results")) {
							matchViewModel.getAllMatchesByTeamTournamentAndFinalScore(id, validItemId, true).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
								@Override
								public void onChanged(List<Match> matches) {
									matchListAdapter.setMatches(matches);
								}
							});
						}
					}
					else if(type.equals("tournament")) {
						if(tab.equals("matches")) {
							matchViewModel.getAllMatchesByTeamTournamentAndFinalScore(validItemId, id, false).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
								@Override
								public void onChanged(List<Match> matches) {
									matchListAdapter.setMatches(matches);

									if(tournamentType.equals("Elimination") && !alreadyAsked) {
										new AsyncTask<Void, Void, String>() {
											@Override
											protected String doInBackground(Void... voids) {
												return tournamentViewModel.getCreatorByIdAsync(id);
											}

											@Override
											protected void onPostExecute(String creator) {
												if(FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(creator)) {
													new AsyncTask<Void, Void, List<Match>>() {
														@Override
														protected List<Match> doInBackground(Void... voids) {
															return matchViewModel.getAllMatchesByTournamentAndFinalScoreAsync(id, false);
														}

														@Override
														protected void onPostExecute(List<Match> matches) {
															if(matches.isEmpty()) {
																new AsyncTask<Void, Void, List<Ranking>>() {
																	@Override
																	protected List<Ranking> doInBackground(Void... voids) {
																		return rankingViewModel.getAllActiveRankingsByTournamentAsync(id);
																	}

																	@Override
																	protected void onPostExecute(final List<Ranking> rankings) {
																		if(rankings.size() > 1) {
																			alreadyAsked = true;
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
													}.execute();
												}
											}
										}.execute();
									}
								}
							});
						}
						else if(tab.equals("results")) {
							matchViewModel.getAllMatchesByTeamTournamentAndFinalScore(validItemId, id, true).observe(getViewLifecycleOwner(), new Observer<List<Match>>() {
								@Override
								public void onChanged(List<Match> matches) {
									matchListAdapter.setMatches(matches);
								}
							});
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	// Click on a match to show match or result details
	@Override
	public void onMatchClicked(Match match) {
		if(FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(match.getCreator())) {
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
		else {
			Toast.makeText(requireContext(), "Invalid user! Access denied!", Toast.LENGTH_SHORT).show();
		}
	}

	// Generate round for Elimination with the remaining teams and update "matches" table in the database
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
									newMatch = new Match(matchId, tournament.getCreator(), tournament.getId(), tournament.getName(), matchDay + i, pair.get(0).getTeam_id(), pair.get(0).getTeam_name(), pair.get(1).getTeam_id(), pair.get(1).getTeam_name());
								}
								else {
									newMatch = new Match(matchId, tournament.getCreator(), tournament.getId(), tournament.getName(), matchDay + i, pair.get(1).getTeam_id(), pair.get(1).getTeam_name(), pair.get(0).getTeam_id(), pair.get(0).getTeam_name());
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

	// Helper method only for testing
	public ArrayList<ArrayList<Ranking>> generateNextRound_OnlyForTesting(List<Ranking> rankings) {
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

		return pairs;
	}
}
