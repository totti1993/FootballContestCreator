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

import com.totti.footballcontestcreator.adapters.TournamentListAdapter;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.TournamentActivity;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class TournamentListFragment extends Fragment implements TournamentListAdapter.OnTournamentClickedListener {

	private DatabaseReference onlineTournaments;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");

		final TournamentListAdapter tournamentListAdapter = new TournamentListAdapter(this);

		TournamentViewModel tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);
		tournamentViewModel.getAllTournamentsOrdered().observe(getViewLifecycleOwner(), new Observer<List<Tournament>>() {
			@Override
			public void onChanged(@Nullable List<Tournament> tournaments) {
				tournamentListAdapter.setTournaments(tournaments);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(tournamentListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		FloatingActionButton fab = rootView.findViewById(R.id.new_item_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new NewTournamentDialogFragment().show(requireActivity().getSupportFragmentManager(), NewTournamentDialogFragment.TAG);
			}
		});

		return rootView;
	}

	@Override
	public void onTournamentClicked(Tournament tournament) {
		Intent intent = new Intent(requireActivity(), TournamentActivity.class);
		intent.putExtra("id", tournament.getId());
		intent.putExtra("tournamentType", tournament.getType());
		startActivity(intent);
	}

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
						new AlertDialog.Builder(requireContext()).setMessage("Are you sure you want to delete tournament \"" + tournament.getName() + "\"?")
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										onlineTournaments.child(tournament.getId()).removeValue();

										Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
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
	public void onTournamentStarClicked(Tournament tournament) {
		onlineTournaments.child(tournament.getId()).child("favorite").setValue(tournament.getFavorite());

		if(tournament.getFavorite()) {
			Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendEmail(final Tournament tournament) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"tothlaszlo.930618@gmail.com"});
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
}
