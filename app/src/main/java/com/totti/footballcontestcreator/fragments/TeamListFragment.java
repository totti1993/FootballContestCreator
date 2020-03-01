package com.totti.footballcontestcreator.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.TeamActivity;
import com.totti.footballcontestcreator.adapters.TeamListAdapter;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

import java.util.List;

public class TeamListFragment extends Fragment implements TeamListAdapter.OnTeamClickedListener {

	private DatabaseReference onlineTeams;

	private TeamViewModel teamViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");

		final TeamListAdapter teamListAdapter = new TeamListAdapter(this);

		teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
		teamViewModel.getAllTeamsOrdered().observe(this, new Observer<List<Team>>() {
			@Override
			public void onChanged(@Nullable List<Team> teams) {
				teamListAdapter.setTeams(teams);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(teamListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

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
		intent.putExtra("id", team.getId());
		startActivity(intent);
	}

	@Override
	public void onTeamLongClicked(final Team team) {
		new AlertDialog.Builder(getContext()).setMessage("Are you sure you want to delete team \"" + team.getName() + "\"?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onlineTeams.child(team.getId()).removeValue();

						Toast.makeText(getContext(), "Team \"" + team.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();

						/*new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... voids) {
								teamViewModel.delete(team);
								return null;
							}

							@Override
							protected void onPostExecute(Void aVoid) {
								Toast.makeText(getContext(), "Team \"" + team.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
							}
						}.execute();*/
					}
				})
				.setNegativeButton("No", null)
				.show();
	}

	@Override
	public void onTeamStarClicked(Team team) {
		onlineTeams.child(team.getId()).child("favorite").setValue(team.getFavorite());

		if(team.getFavorite()) {
			Toast.makeText(getContext(), "Team \"" + team.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getContext(), "Team \"" + team.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}

		/*new AsyncTask<Void, Void, Void>() {
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
		}.execute();*/
	}
}
