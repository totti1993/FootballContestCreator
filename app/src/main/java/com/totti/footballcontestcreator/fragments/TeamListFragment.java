package com.totti.footballcontestcreator.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.TeamActivity;
import com.totti.footballcontestcreator.adapters.TeamListAdapter;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

import java.util.List;

public class TeamListFragment extends Fragment implements TeamListAdapter.OnTeamClickedListener {

	private TeamViewModel teamViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		final TeamListAdapter teamListAdapter = new TeamListAdapter(this);

		teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
		teamViewModel.getAllTeamsOrdered().observe(this, new Observer<List<Team>>() {
			@Override
			public void onChanged(@Nullable List<Team> teams) {
				teamListAdapter.setTeams(teams);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(teamListAdapter);

		FloatingActionButton fab = rootView.findViewById(R.id.new_item_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new NewTeamDialogFragment().show(getActivity().getSupportFragmentManager(), NewTeamDialogFragment.TAG);
			}
		});

		return rootView;
	}

	@Override
	public void onTeamClicked(Team team) {
		Intent intent = new Intent(getActivity(), TeamActivity.class);
		intent.putExtra("teamId", team.getId());
		startActivity(intent);
	}

	@Override
	public void onTeamLongClicked(final Team team) {
		new AlertDialog.Builder(getContext()).setMessage("Are you sure you want to delete team \"" + team.getName() + "\"?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... voids) {
								teamViewModel.delete(team);
								return null;
							}

							@Override
							protected void onPostExecute(Void aVoid) {
								Toast.makeText(getContext(), "Team \"" + team.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
							}
						}.execute();
					}
				})
				.setNegativeButton("No", null)
				.show();
	}

	@Override
	public void onTeamStarClicked(final Team team) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				teamViewModel.update(team);
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				if(team.getFavorite()) {
					Toast.makeText(getContext(), "Team \"" + team.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getContext(), "Team \"" + team.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}
}
