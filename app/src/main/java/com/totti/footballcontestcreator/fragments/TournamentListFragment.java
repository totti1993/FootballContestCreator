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
import com.totti.footballcontestcreator.TournamentActivity;
import com.totti.footballcontestcreator.adapters.TournamentListAdapter;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class TournamentListFragment extends Fragment implements TournamentListAdapter.OnTournamentClickedListener {

	private DatabaseReference onlineTournaments;

	private TournamentViewModel tournamentViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);

		final TournamentListAdapter tournamentListAdapter = new TournamentListAdapter(this);

		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");

		tournamentViewModel = ViewModelProviders.of(getActivity()).get(TournamentViewModel.class);
		tournamentViewModel.getAllTournamentsOrdered().observe(this, new Observer<List<Tournament>>() {
			@Override
			public void onChanged(@Nullable List<Tournament> tournaments) {
				tournamentListAdapter.setTournaments(tournaments);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.item_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(tournamentListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

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
		Intent intent = new Intent(getActivity(), TournamentActivity.class);
		intent.putExtra("id", tournament.getId());
		intent.putExtra("tournamentType", tournament.getType());
		startActivity(intent);
	}

	@Override
	public void onTournamentLongClicked(final Tournament tournament) {
		new AlertDialog.Builder(getContext()).setMessage("Are you sure you want to delete tournament \"" + tournament.getName() + "\"?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onlineTournaments.child(tournament.getId()).removeValue();

						Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();

						/*new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... voids) {
								tournamentViewModel.delete(tournament);
								return null;
							}

							@Override
							protected void onPostExecute(Void aVoid) {
								Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
							}
						}.execute();*/
					}
				})
				.setNegativeButton("No", null)
				.show();
	}

	@Override
	public void onTournamentStarClicked(Tournament tournament) {
		onlineTournaments.child(tournament.getId()).child("favorite").setValue(tournament.getFavorite());

		if(tournament.getFavorite()) {
			Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}

		/*new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				tournamentViewModel.update(tournament);
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				if(tournament.getFavorite()) {
					Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();*/
	}
}
