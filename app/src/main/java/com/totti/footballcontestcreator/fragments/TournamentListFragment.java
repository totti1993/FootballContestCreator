package com.totti.footballcontestcreator.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import com.totti.footballcontestcreator.adapters.TournamentListAdapter;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class TournamentListFragment extends Fragment implements TournamentListAdapter.OnTournamentClickedListener {

	private TournamentViewModel tournamentViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		final TournamentListAdapter tournamentListAdapter = new TournamentListAdapter(this);

		tournamentViewModel = ViewModelProviders.of(getActivity()).get(TournamentViewModel.class);
		tournamentViewModel.getAllTournamentsOrdered().observe(this, new Observer<List<Tournament>>() {
			@Override
			public void onChanged(@Nullable List<Tournament> tournaments) {
				tournamentListAdapter.setTournaments(tournaments);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(tournamentListAdapter);

		FloatingActionButton fab = rootView.findViewById(R.id.new_item_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new NewTournamentDialogFragment().show(getActivity().getSupportFragmentManager(), NewTournamentDialogFragment.TAG);
			}
		});

		return rootView;
	}

	@Override
	public void onTournamentClicked(Tournament tournament) {
		Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" clicked!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTournamentLongClicked(final Tournament tournament) {
		new AlertDialog.Builder(getContext()).setMessage("Are you sure you want to delete tournament \"" + tournament.getName() + "\"?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tournamentViewModel.delete(tournament);
						Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("No", null)
				.show();
	}

	@Override
	public void onTournamentStarClicked(Tournament tournament) {
		tournamentViewModel.update(tournament);
		if(tournament.getFavorite()) {
			Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}
	}
}
