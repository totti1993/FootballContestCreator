package com.totti.footballcontestcreator.fragments;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.adapters.TeamListAdapter;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.TeamActivity;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

import java.util.List;

public class TeamListFragment extends Fragment implements TeamListAdapter.OnTeamClickedListener {

	private DatabaseReference onlineTeams;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");

		final TeamListAdapter teamListAdapter = new TeamListAdapter(this);

		TeamViewModel teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
		teamViewModel.getAllTeamsOrdered().observe(getViewLifecycleOwner(), new Observer<List<Team>>() {
			@Override
			public void onChanged(@Nullable List<Team> teams) {
				teamListAdapter.setTeams(teams);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(teamListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		FloatingActionButton fab = rootView.findViewById(R.id.new_item_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new NewTeamDialogFragment().show(requireActivity().getSupportFragmentManager(), NewTeamDialogFragment.TAG);
			}
		});

		return rootView;
	}

	@Override
	public void onTeamClicked(Team team) {
		Intent intent = new Intent(requireActivity(), TeamActivity.class);
		intent.putExtra("id", team.getId());
		startActivity(intent);
	}

	@Override
	public void onTeamLongClicked(final Team team) {
		new AlertDialog.Builder(requireContext()).setMessage("What would you like to do?")
				.setPositiveButton("Send email", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendEmail(team);
					}
				})
				.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to delete team \"" + team.getName() + "\"?")
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										onlineTeams.child(team.getId()).removeValue();

										Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
									}
								})
								.setNegativeButton("No", null)
								.show();
					}
				})
				.setNegativeButton("Cancel", null)
				.show();
	}

	@Override
	public void onTeamStarClicked(Team team) {
		onlineTeams.child(team.getId()).child("favorite").setValue(team.getFavorite());

		if(team.getFavorite()) {
			Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmail(final Team team) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"tothlaszlo.930618@gmail.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "FCC - Every information about team \"" + team.getName() + "\"");

		final String[] emailText = {""};

		emailText[0] += "¤¤¤¤¤¤\n";
		emailText[0] += "- Name: " + team.getName() + "\n";
		emailText[0] += "¤¤¤¤¤¤\n";
		emailText[0] += "- Number of trophies: " + team.getTrophies() + "\n";
		emailText[0] += "- Number of all-time wins: " + team.getAll_time_wins() + "\n";
		emailText[0] += "- Number of all-time draws: " + team.getAll_time_draws() + "\n";
		emailText[0] += "- Number of all-time losses: " + team.getAll_time_losses() + "\n";
		emailText[0] += (team.getComments() != null && !team.getComments().isEmpty()) ? "- Comments: " + team.getComments() + "\n\n" : "\n";

		final RankingViewModel rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		new AsyncTask<Void, Void, List<Ranking>>() {
			@Override
			protected List<Ranking> doInBackground(Void... voids) {
				return rankingViewModel.getAllRankingsByTeamAsync(team.getId());
			}

			@Override
			protected void onPostExecute(List<Ranking> rankings) {
				emailText[0] += "- Rankings:\n";
				for(Ranking ranking: rankings) {
					emailText[0] += "    ¤¤¤¤¤¤¤¤¤¤¤\n";
					emailText[0] += "    - Tournament: " + ranking.getTournament_name() + "\n";
					emailText[0] += "    ¤¤¤¤¤¤¤¤¤¤¤\n";
					emailText[0] += "        - Active (relevant for eliminations): " + ranking.getActive() + "\n";
					emailText[0] += "        - Points (relevant for championships): " + ranking.getPoints() + "\n";
					emailText[0] += "        - W/D/L: " + ranking.getWins() + "/" + ranking.getDraws() + "/" + ranking.getLosses() + "\n";
					emailText[0] += "        - GF/GA/GD: " + ranking.getGoals_for() + "/" + ranking.getGoals_against() + "/" + ranking.getGoal_difference() + "\n";
					emailText[0] += (ranking.getComments() != null && !ranking.getComments().isEmpty()) ? "        - Comments: " + ranking.getComments() + "\n" : "";
				}
				emailText[0] += "\n";

				final MatchViewModel matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
				new AsyncTask<Void, Void, List<Match>>() {
					@Override
					protected List<Match> doInBackground(Void... voids) {
						return matchViewModel.getAllMatchesByTeamAsync(team.getId());
					}

					@Override
					protected void onPostExecute(List<Match> matches) {
						emailText[0] += "- Results:\n";
						for(Match match: matches) {
							if(match.getFinal_score()) {
								emailText[0] += "    " + match.getHome_name() + " - " + match.getVisitor_name() + " : " + match.getHome_score() + "-" + match.getVisitor_score() + "\n";
								emailText[0] += "        - Tournament: " + match.getTournament_name() + "\n";
								emailText[0] += "        - Match day: " + match.getMatch_day() + "\n";
								emailText[0] += (match.getComments() != null && !match.getComments().isEmpty()) ? "        - Comments: " + match.getComments() + "\n" : "";
							}
						}
						emailText[0] += "\n- Matches:\n";
						for(Match match: matches) {
							if(!match.getFinal_score()) {
								emailText[0] += "    " + match.getHome_name() + " - " + match.getVisitor_name() + " : " + match.getHome_score() + "-" + match.getVisitor_score() + "\n";
								emailText[0] += "        - Tournament: " + match.getTournament_name() + "\n";
								emailText[0] += "        - Match day: " + match.getMatch_day() + "\n";
								emailText[0] += (match.getComments() != null && !match.getComments().isEmpty()) ? "        - Comments: " + match.getComments() + "\n" : "";
							}
						}

						intent.putExtra(Intent.EXTRA_TEXT, emailText[0]);
						startActivity(Intent.createChooser(intent, "Choose an application"));
					}
				}.execute();
			}
		}.execute();
	}
}
