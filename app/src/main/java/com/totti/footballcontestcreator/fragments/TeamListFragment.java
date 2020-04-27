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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.totti.footballcontestcreator.adapters.TeamListAdapter;
import com.totti.footballcontestcreator.database.Favorite;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.TeamActivity;
import com.totti.footballcontestcreator.viewmodels.FavoriteViewModel;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

import java.util.List;

public class TeamListFragment extends Fragment implements TeamListAdapter.OnTeamClickedListener {

	private DatabaseReference onlineTeams;
	private DatabaseReference onlineFavorites;

	private FavoriteViewModel favoriteViewModel;
	private RankingViewModel rankingViewModel;
	private TeamViewModel teamViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");
		onlineFavorites = FirebaseDatabase.getInstance().getReference("favorites");

		favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
		rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);

		final TeamListAdapter teamListAdapter = new TeamListAdapter(this);

		// Keep all teams up to date
		teamViewModel.getAllTeams().observe(getViewLifecycleOwner(), new Observer<List<Team>>() {
			@Override
			public void onChanged(final List<Team> teams) {
				new AsyncTask<Void, Void, List<String>>() {
					@Override
					protected List<String> doInBackground(Void... voids) {
						return favoriteViewModel.getAllFavoriteTeamsAsync();
					}

					@Override
					protected void onPostExecute(List<String> favorites) {
						teamListAdapter.setTeams(teams, favorites);
					}
				}.execute();
			}
		});

		// Keep all favorite teams up to date
		favoriteViewModel.getAllFavoriteTeams().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
			@Override
			public void onChanged(final List<String> favorites) {
				new AsyncTask<Void, Void, List<Team>>() {
					@Override
					protected List<Team> doInBackground(Void... voids) {
						return teamViewModel.getAllTeamsAsync();
					}

					@Override
					protected void onPostExecute(List<Team> teams) {
						teamListAdapter.setTeams(teams, favorites);
					}
				}.execute();
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(teamListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		// Button to add new teams
		FloatingActionButton fab = rootView.findViewById(R.id.new_item_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(FirebaseAuth.getInstance().getCurrentUser() != null) {
					new NewTeamDialogFragment().show(requireActivity().getSupportFragmentManager(), NewTeamDialogFragment.TAG);
				}
				else {
					Toast.makeText(requireContext(), "No user is signed in!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return rootView;
	}

	// Start TeamActivity
	@Override
	public void onTeamClicked(Team team) {
		Intent intent = new Intent(requireActivity(), TeamActivity.class);
		intent.putExtra("id", team.getId());
		startActivity(intent);
	}

	// Delete team or send an email about it
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
						if((FirebaseAuth.getInstance().getCurrentUser() != null) && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(team.getCreator())) {
							new AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to delete team \"" + team.getName() + "\"?")
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											delete(team);
										}
									})
									.setNegativeButton("No", null)
									.show();
						}
						else {
							Toast.makeText(requireContext(), "Invalid user! Access denied!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.show();
	}

	// Update favorite status of the team
	@Override
	public void onTeamStarClicked(final Team team, boolean favorite) {
		if(FirebaseAuth.getInstance().getCurrentUser() != null) {
			// Insert into or delete from the user's list in Firebase
			if(favorite) {
				String id = team.getId();
				String favorite_team_id = team.getId();
				String favorite_type = "team";

				Favorite newFavorite = new Favorite(id, favorite_team_id, null, favorite_type);

				onlineFavorites.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)")).child(newFavorite.getId()).setValue(newFavorite);

				Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
			}
			else {
				onlineFavorites.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)")).child(team.getId()).removeValue();

				Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(requireContext(), "No user is signed in!", Toast.LENGTH_SHORT).show();
		}
	}

	// Send an email to the current user that contains every information about the team
	private void sendEmail(final Team team) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		if(FirebaseAuth.getInstance().getCurrentUser() != null) {
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] {FirebaseAuth.getInstance().getCurrentUser().getEmail()});
		}
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

	// Delete team if there is no ranking connected to it in the database
	// Delete it from the favorites too
	private void delete(final Team team) {
		new AsyncTask<Void, Void, List<Ranking>>() {
			@Override
			protected List<Ranking> doInBackground(Void... voids) {
				return rankingViewModel.getAllRankingsByTeamAsync(team.getId());
			}

			@Override
			protected void onPostExecute(List<Ranking> rankings) {
				if(rankings.isEmpty()) {
					onlineFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
								if(snapshot.hasChild(team.getId())) {
									onlineFavorites.child(snapshot.getKey()).child(team.getId()).removeValue();
								}
							}
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {

						}
					});

					onlineTeams.child(team.getId()).removeValue();

					Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(requireContext(), "Team cannot be deleted!", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}
}
