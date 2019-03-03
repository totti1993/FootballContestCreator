package com.totti.footballcontestcreator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentListAdapter extends RecyclerView.Adapter<TournamentListAdapter.TournamentViewHolder> {

	class TournamentViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		TextView typeTextView;

		TournamentViewHolder(View tournamentView) {
			super(tournamentView);

			nameTextView = tournamentView.findViewById(R.id.TournamentNameTextView);
			typeTextView = tournamentView.findViewById(R.id.TournamentTypeTextView);
		}
	}

	private List<Tournament> tournaments;

	public TournamentListAdapter(Context context) {
		tournaments = new ArrayList<>();
	}

	@NonNull
	@Override
	public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View tournamentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tournament_list_item, parent, false);
		return new TournamentViewHolder(tournamentView);
	}

	@Override
	public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
		Tournament tournament = tournaments.get(position);
		holder.nameTextView.setText(tournament.getName());
		holder.typeTextView.setText(tournament.getType());
	}

	@Override
	public int getItemCount() {
		return tournaments.size();
	}

	public void setTournaments(List<Tournament> tournaments) {
		this.tournaments.clear();
		this.tournaments.addAll(tournaments);
		notifyDataSetChanged();
	}
}
