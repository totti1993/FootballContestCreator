package com.totti.footballcontestcreator.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class CommentsFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.comments_fragment, container, false);

		final String type = this.getArguments().getString("type");
		final long id = this.getArguments().getLong("id");

		final TextView comments = rootView.findViewById(R.id.comments_textView);
		final FloatingActionButton fab = rootView.findViewById(R.id.add_comments_fab);

		if(type != null) {
			if(type.equals("team")) {
				final TeamViewModel teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
				new AsyncTask<Void, Void, Team>() {
					@Override
					protected Team doInBackground(Void... voids) {
						return teamViewModel.getTeamById(id);
					}

					@Override
					protected void onPostExecute(final Team team) {
						comments.setText(team.getComments());
						fab.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Bundle args = new Bundle();
								args.putString("type", type);
								args.putLong("id", id);
								args.putString("name", team.getName());
								args.putInt("trophies", team.getTrophies());
								args.putInt("all_time_wins", team.getAll_time_wins());
								args.putInt("all_time_draws", team.getAll_time_draws());
								args.putInt("all_time_losses", team.getAll_time_losses());
								args.putString("comments", team.getComments());
								args.putBoolean("favorite", team.getFavorite());
								args.putBoolean("selected", team.getSelected());
								CommentsDialogFragment commentsDialogFragment = new CommentsDialogFragment();
								commentsDialogFragment.setArguments(args);
								commentsDialogFragment.show(getActivity().getSupportFragmentManager(), CommentsDialogFragment.TAG);
							}
						});
					}
				}.execute();
			}
			else if(type.equals("tournament")) {
				final TournamentViewModel tournamentViewModel = ViewModelProviders.of(getActivity()).get(TournamentViewModel.class);
				new AsyncTask<Void, Void, Tournament>() {
					@Override
					protected Tournament doInBackground(Void... voids) {
						return tournamentViewModel.getTournamentById(id);
					}

					@Override
					protected void onPostExecute(final Tournament tournament) {
						comments.setText(tournament.getComments());
						fab.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Bundle args = new Bundle();
								args.putString("type", type);
								args.putLong("id", id);
								args.putString("name", tournament.getName());
								args.putString("type_2", tournament.getType());
								args.putInt("rounds", tournament.getRounds());
								args.putInt("teams", tournament.getTeams());
								args.putString("comments", tournament.getComments());
								args.putBoolean("favorite", tournament.getFavorite());
								CommentsDialogFragment commentsDialogFragment = new CommentsDialogFragment();
								commentsDialogFragment.setArguments(args);
								commentsDialogFragment.show(getActivity().getSupportFragmentManager(), CommentsDialogFragment.TAG);
							}
						});
					}
				}.execute();
			}
		}

		return rootView;
	}
}
