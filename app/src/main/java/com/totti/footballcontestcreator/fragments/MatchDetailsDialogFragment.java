package com.totti.footballcontestcreator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.HashMap;
import java.util.Map;

public class MatchDetailsDialogFragment extends DialogFragment {

	public static final String TAG = "MatchDetailsDialogFragment";

	private TextView tournamentNameTextView;
	private TextView matchDayNumberTextView;
	private EditText homeTeamScoreEditText;
	private TextView homeTeamNameTextView;
	private EditText visitorTeamScoreEditText;
	private TextView visitorTeamNameTextView;
	private EditText commentsEditText;
	private CheckBox finalScoreCheckBox;

	private String id;

	private DatabaseReference onlineMatches;
	private DatabaseReference onlineRankings;
	private DatabaseReference onlineTeams;

	private MatchViewModel matchViewModel;
	private RankingViewModel rankingViewModel;
	private TeamViewModel teamViewModel;
	private TournamentViewModel tournamentViewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = this.getArguments().getString("id");

		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");
		onlineRankings = FirebaseDatabase.getInstance().getReference("rankings");
		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");

		matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
		rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
		tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.match_details)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if(isValid()) {
							Map<String, Object> matchAttributes = new HashMap<>();

							matchAttributes.put("home_score", Integer.parseInt(homeTeamScoreEditText.getText().toString()));
							matchAttributes.put("visitor_score", Integer.parseInt(visitorTeamScoreEditText.getText().toString()));
							matchAttributes.put("comments", commentsEditText.getText().toString());
							matchAttributes.put("final_score", finalScoreCheckBox.isChecked());

							onlineMatches.child(id).updateChildren(matchAttributes);

							if(finalScoreCheckBox.isChecked()) {
								new AsyncTask<Void, Void, Match>() {
									@Override
									protected Match doInBackground(Void... voids) {
										return matchViewModel.getMatchByIdAsync(id);
									}

									@Override
									protected void onPostExecute(Match match) {
										updateTeamsAndRankings(match, Integer.parseInt(homeTeamScoreEditText.getText().toString()), Integer.parseInt(visitorTeamScoreEditText.getText().toString()));
									}
								}.execute();
							}

							Toast.makeText(requireContext(), "Match updated!", Toast.LENGTH_SHORT).show();
						}
						else {
							Toast.makeText(requireContext(), "Match not updated!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.match_details_dialog_fragment, null);

		tournamentNameTextView = contentView.findViewById(R.id.match_tournament_name_textView);
		matchDayNumberTextView = contentView.findViewById(R.id.match_day_number_textView);
		homeTeamScoreEditText = contentView.findViewById(R.id.match_home_team_score_editText);
		homeTeamNameTextView = contentView.findViewById(R.id.match_home_team_name_textView);
		visitorTeamScoreEditText = contentView.findViewById(R.id.match_visitor_team_score_editText);
		visitorTeamNameTextView = contentView.findViewById(R.id.match_visitor_team_name_textView);
		commentsEditText = contentView.findViewById(R.id.match_comments_editText);
		finalScoreCheckBox = contentView.findViewById(R.id.match_final_score_checkBox);

		new AsyncTask<Void, Void, Match>() {
			@Override
			protected Match doInBackground(Void... voids) {
				return matchViewModel.getMatchByIdAsync(id);
			}

			@Override
			protected void onPostExecute(Match match) {
				tournamentNameTextView.setText(match.getTournament_name());
				String matchDay = "Matchday #" + match.getMatch_day();
				matchDayNumberTextView.setText(matchDay);
				homeTeamScoreEditText.setText(Integer.toString(match.getHome_score()));
				homeTeamNameTextView.setText(match.getHome_name());
				visitorTeamScoreEditText.setText(Integer.toString(match.getVisitor_score()));
				visitorTeamNameTextView.setText(match.getVisitor_name());
				commentsEditText.setText(match.getComments());
				finalScoreCheckBox.setChecked(match.getFinal_score());
			}
		}.execute();

		return contentView;
	}

	private boolean isValid() {
		if((Integer.parseInt(homeTeamScoreEditText.getText().toString()) < 0) || (Integer.parseInt(visitorTeamScoreEditText.getText().toString()) < 0)) {
			return false;
		}

		return true;
	}

	private void updateTeamsAndRankings(final Match match, final int homeScore, final int visitorScore) {
		new AsyncTask<Void, Void, Team>() {
			@Override
			protected Team doInBackground(Void... voids) {
				return teamViewModel.getTeamByIdAsync(match.getHome_id());
			}

			@Override
			protected void onPostExecute(Team team) {
				if(homeScore > visitorScore) {
					onlineTeams.child(team.getId()).child("all_time_wins").setValue(team.getAll_time_wins() + 1);
				}
				else if(homeScore == visitorScore) {
					onlineTeams.child(team.getId()).child("all_time_draws").setValue(team.getAll_time_draws() + 1);
				}
				else {
					onlineTeams.child(team.getId()).child("all_time_losses").setValue(team.getAll_time_losses() + 1);
				}
			}
		}.execute();

		new AsyncTask<Void, Void, Team>() {
			@Override
			protected Team doInBackground(Void... voids) {
				return teamViewModel.getTeamByIdAsync(match.getVisitor_id());
			}

			@Override
			protected void onPostExecute(Team team) {
				if(visitorScore > homeScore) {
					onlineTeams.child(team.getId()).child("all_time_wins").setValue(team.getAll_time_wins() + 1);
				}
				else if(visitorScore == homeScore) {
					onlineTeams.child(team.getId()).child("all_time_draws").setValue(team.getAll_time_draws() + 1);
				}
				else {
					onlineTeams.child(team.getId()).child("all_time_losses").setValue(team.getAll_time_losses() + 1);
				}
			}
		}.execute();

		new AsyncTask<Void, Void, Ranking>() {
			@Override
			protected Ranking doInBackground(Void... voids) {
				return rankingViewModel.getRankingByTournamentAndTeamAsync(match.getTournament_id(), match.getHome_id());
			}

			@Override
			protected void onPostExecute(final Ranking ranking) {
				final Map<String, Object> rankingAttributes = new HashMap<>();

				if(homeScore > visitorScore) {
					rankingAttributes.put("points", ranking.getPoints() + 3);
					rankingAttributes.put("wins", ranking.getWins() + 1);
				}
				else if(homeScore == visitorScore) {
					rankingAttributes.put("points", ranking.getPoints() + 1);
					rankingAttributes.put("draws", ranking.getDraws() + 1);
				}
				else {
					rankingAttributes.put("losses", ranking.getLosses() + 1);
				}

				rankingAttributes.put("goals_for", ranking.getGoals_for() + homeScore);
				rankingAttributes.put("goals_against", ranking.getGoals_against() + visitorScore);
				rankingAttributes.put("goal_difference", ranking.getGoal_difference() + homeScore - visitorScore);

				new AsyncTask<Void, Void, Tournament>() {
					@Override
					protected Tournament doInBackground(Void... voids) {
						return tournamentViewModel.getTournamentByIdAsync(match.getTournament_id());
					}

					@Override
					protected void onPostExecute(final Tournament tournament) {
						if(tournament.getType().equals("Elimination")) {
							if(tournament.getRounds() == 1) {
								if(homeScore < visitorScore) {
									rankingAttributes.put("active", false);
								}
								onlineRankings.child(ranking.getId()).updateChildren(rankingAttributes);
							}
							else {
								new AsyncTask<Void, Void, Match>() {
									@Override
									protected Match doInBackground(Void... voids) {
										return matchViewModel.getMatchByTournamentAndTeamsInEliminationAsync(tournament.getId(), match.getVisitor_id(), match.getHome_id());
									}

									@Override
									protected void onPostExecute(Match otherMatch) {
										if(otherMatch.getFinal_score()) {
											if((homeScore + otherMatch.getVisitor_score()) < (visitorScore + otherMatch.getHome_score())) {
												rankingAttributes.put("active", false);
											}
											else if((homeScore + otherMatch.getVisitor_score()) == (visitorScore + otherMatch.getHome_score())) {
												if(otherMatch.getVisitor_score() < visitorScore) {
													rankingAttributes.put("active", false);
												}
											}
										}
										onlineRankings.child(ranking.getId()).updateChildren(rankingAttributes);
									}
								}.execute();
							}
						}
						else {
							onlineRankings.child(ranking.getId()).updateChildren(rankingAttributes);
						}
					}
				}.execute();
			}
		}.execute();

		new AsyncTask<Void, Void, Ranking>() {
			@Override
			protected Ranking doInBackground(Void... voids) {
				return rankingViewModel.getRankingByTournamentAndTeamAsync(match.getTournament_id(), match.getVisitor_id());
			}

			@Override
			protected void onPostExecute(final Ranking ranking) {
				final Map<String, Object> rankingAttributes = new HashMap<>();

				if(visitorScore > homeScore) {
					rankingAttributes.put("points", ranking.getPoints() + 3);
					rankingAttributes.put("wins", ranking.getWins() + 1);
				}
				else if(visitorScore == homeScore) {
					rankingAttributes.put("points", ranking.getPoints() + 1);
					rankingAttributes.put("draws", ranking.getDraws() + 1);
				}
				else {
					rankingAttributes.put("losses", ranking.getLosses() + 1);
				}

				rankingAttributes.put("goals_for", ranking.getGoals_for() + visitorScore);
				rankingAttributes.put("goals_against", ranking.getGoals_against() + homeScore);
				rankingAttributes.put("goal_difference", ranking.getGoal_difference() + visitorScore - homeScore);

				new AsyncTask<Void, Void, Tournament>() {
					@Override
					protected Tournament doInBackground(Void... voids) {
						return tournamentViewModel.getTournamentByIdAsync(match.getTournament_id());
					}

					@Override
					protected void onPostExecute(final Tournament tournament) {
						if(tournament.getType().equals("Elimination")) {
							if(tournament.getRounds() == 1) {
								if(visitorScore < homeScore) {
									rankingAttributes.put("active", false);
								}
								onlineRankings.child(ranking.getId()).updateChildren(rankingAttributes);
							}
							else {
								new AsyncTask<Void, Void, Match>() {
									@Override
									protected Match doInBackground(Void... voids) {
										return matchViewModel.getMatchByTournamentAndTeamsInEliminationAsync(tournament.getId(), match.getVisitor_id(), match.getHome_id());
									}

									@Override
									protected void onPostExecute(Match otherMatch) {
										if(otherMatch.getFinal_score()) {
											if((visitorScore + otherMatch.getHome_score()) < (homeScore + otherMatch.getVisitor_score())) {
												rankingAttributes.put("active", false);
											}
											else if((visitorScore + otherMatch.getHome_score()) == (homeScore + otherMatch.getVisitor_score())) {
												if(visitorScore < otherMatch.getVisitor_score()) {
													rankingAttributes.put("active", false);
												}
											}
										}
										onlineRankings.child(ranking.getId()).updateChildren(rankingAttributes);
									}
								}.execute();
							}
						}
						else {
							onlineRankings.child(ranking.getId()).updateChildren(rankingAttributes);
						}
					}
				}.execute();
			}
		}.execute();
	}
}
