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

import com.totti.footballcontestcreator.adapters.TournamentListAdapter;
import com.totti.footballcontestcreator.database.Favorite;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.TournamentActivity;
import com.totti.footballcontestcreator.viewmodels.FavoriteViewModel;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class TournamentListFragment extends Fragment implements TournamentListAdapter.OnTournamentClickedListener {

	private DatabaseReference onlineFavorites;
	private DatabaseReference onlineMatches;
	private DatabaseReference onlineRankings;
	private DatabaseReference onlineTournaments;

	private FavoriteViewModel favoriteViewModel;
	private MatchViewModel matchViewModel;
	private RankingViewModel rankingViewModel;
	private TournamentViewModel tournamentViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		onlineFavorites = FirebaseDatabase.getInstance().getReference("favorites");
		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");
		onlineRankings = FirebaseDatabase.getInstance().getReference("rankings");
		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");

		favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
		matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
		rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);

		final TournamentListAdapter tournamentListAdapter = new TournamentListAdapter(this);

		// Keep all tournaments up to date
		tournamentViewModel.getAllTournaments().observe(getViewLifecycleOwner(), new Observer<List<Tournament>>() {
			@Override
			public void onChanged(final List<Tournament> tournaments) {
				new AsyncTask<Void, Void, List<String>>() {
					@Override
					protected List<String> doInBackground(Void... voids) {
						return favoriteViewModel.getAllFavoriteTournamentsAsync();
					}

					@Override
					protected void onPostExecute(List<String> favorites) {
						tournamentListAdapter.setTournaments(tournaments, favorites);
					}
				}.execute();
			}
		});

		// Keep all favorite tournaments up to date
		favoriteViewModel.getAllFavoriteTournaments().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
			@Override
			public void onChanged(final List<String> favorites) {
				new AsyncTask<Void, Void, List<Tournament>>() {
					@Override
					protected List<Tournament> doInBackground(Void... voids) {
						return tournamentViewModel.getAllTournamentsAsync();
					}

					@Override
					protected void onPostExecute(List<Tournament> tournaments) {
						tournamentListAdapter.setTournaments(tournaments, favorites);
					}
				}.execute();
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(tournamentListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		// Button to add new tournaments
		FloatingActionButton fab = rootView.findViewById(R.id.new_item_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(FirebaseAuth.getInstance().getCurrentUser() != null) {
					new NewTournamentDialogFragment().show(requireActivity().getSupportFragmentManager(), NewTournamentDialogFragment.TAG);
				}
				else {
					Toast.makeText(requireContext(), "No user is signed in!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return rootView;
	}

	// Start TournamentActivity
	@Override
	public void onTournamentClicked(Tournament tournament) {
		Intent intent = new Intent(requireActivity(), TournamentActivity.class);
		intent.putExtra("id", tournament.getId());
		intent.putExtra("tournamentType", tournament.getType());
		startActivity(intent);
	}

	// Delete tournament or send an email about it
	@Override
	public void onTournamentLongClicked(final Tournament tournament) {
		new AlertDialog.Builder(requireContext()).setMessage("What would you like to do?")
				.setPositiveButton("Send email", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendEmail(tournament);
					}
				})
				.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if((FirebaseAuth.getInstance().getCurrentUser() != null) && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(tournament.getCreator())) {
							new AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to delete tournament \"" + tournament.getName() + "\"?")
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											delete(tournament);
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

	// Update favorite status of the tournament
	@Override
	public void onTournamentStarClicked(final Tournament tournament, boolean favorite) {
		if(FirebaseAuth.getInstance().getCurrentUser() != null) {
			// Insert into or delete from the user's list in Firebase
			if(favorite) {
				String id = tournament.getId();
				String favorite_tournament_id = tournament.getId();
				String favorite_type = "tournament";

				Favorite newFavorite = new Favorite(id, null, favorite_tournament_id, favorite_type);

				onlineFavorites.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)")).child(newFavorite.getId()).setValue(newFavorite);

				Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
			}
			else {
				onlineFavorites.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "(dot)")).child(tournament.getId()).removeValue();

				Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(requireContext(), "No user is signed in!", Toast.LENGTH_SHORT).show();
		}
	}

	// Send an email to the current user that contains every information about the tournament
	private void sendEmail(final Tournament tournament) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		if(FirebaseAuth.getInstance().getCurrentUser() != null) {
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] {FirebaseAuth.getInstance().getCurrentUser().getEmail()});
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, "FCC - Every information about tournament \"" + tournament.getName() + "\"");

		final String[] emailText = {""};

		emailText[0] += "¤¤¤¤¤¤\n";
		emailText[0] += "- Name: " + tournament.getName() + "\n";
		emailText[0] += "¤¤¤¤¤¤\n";
		emailText[0] += "- Type: " + tournament.getType() + "\n";
		emailText[0] += "- Number of rounds: " + tournament.getRounds() + "\n";
		emailText[0] += "- Number of teams: " + tournament.getTeams() + "\n";
		emailText[0] += (tournament.getComments() != null && !tournament.getComments().isEmpty()) ? "- Comments: " + tournament.getComments() + "\n\n" : "\n";

		final RankingViewModel rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		new AsyncTask<Void, Void, List<Ranking>>() {
			@Override
			protected List<Ranking> doInBackground(Void... voids) {
				return rankingViewModel.getAllRankingsByTournamentAsync(tournament.getId());
			}

			@Override
			protected void onPostExecute(List<Ranking> rankings) {
				emailText[0] += "- Rankings:\n";
				for(Ranking ranking: rankings) {
					emailText[0] += "    ¤¤¤¤¤¤\n";
					emailText[0] += "    - Team: " + ranking.getTeam_name() + "\n";
					emailText[0] += "    ¤¤¤¤¤¤\n";
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
						return matchViewModel.getAllMatchesByTournamentAsync(tournament.getId());
					}

					@Override
					protected void onPostExecute(List<Match> matches) {
						emailText[0] += "- Results:\n";
						for(Match match: matches) {
							if(match.getFinal_score()) {
								emailText[0] += "    " + match.getHome_name() + " - " + match.getVisitor_name() + " : " + match.getHome_score() + "-" + match.getVisitor_score() + "\n";
								emailText[0] += "        - Match day: " + match.getMatch_day() + "\n";
								emailText[0] += (match.getComments() != null && !match.getComments().isEmpty()) ? "        - Comments: " + match.getComments() + "\n" : "";
							}
						}
						emailText[0] += "\n- Matches:\n";
						for(Match match: matches) {
							if(!match.getFinal_score()) {
								emailText[0] += "    " + match.getHome_name() + " - " + match.getVisitor_name() + " : " + match.getHome_score() + "-" + match.getVisitor_score() + "\n";
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

	// Delete tournament (rankings and matches connected to it also)
	// Delete it from the favorites too
	private void delete(final Tournament tournament) {
		onlineFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
					if(snapshot.hasChild(tournament.getId())) {
						onlineFavorites.child(snapshot.getKey()).child(tournament.getId()).removeValue();
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});

		new AsyncTask<Void, Void, List<Ranking>>() {
			@Override
			protected List<Ranking> doInBackground(Void... voids) {
				return rankingViewModel.getAllRankingsByTournamentAsync(tournament.getId());
			}

			@Override
			protected void onPostExecute(List<Ranking> rankings) {
				for(Ranking ranking : rankings) {
					onlineRankings.child(ranking.getId()).removeValue();
				}
			}
		}.execute();

		new AsyncTask<Void, Void, List<Match>>() {
			@Override
			protected List<Match> doInBackground(Void... voids) {
				return matchViewModel.getAllMatchesByTournamentAsync(tournament.getId());
			}

			@Override
			protected void onPostExecute(List<Match> matches) {
				for(Match match : matches) {
					onlineMatches.child(match.getId()).removeValue();
				}
			}
		}.execute();

		onlineTournaments.child(tournament.getId()).removeValue();

		Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
	}
}
