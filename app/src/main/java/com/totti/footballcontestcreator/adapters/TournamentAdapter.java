package com.totti.footballcontestcreator.adapters;

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

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {

	private final List<Tournament> tournaments;

	private TournamentClickListener listener;

	public TournamentAdapter(TournamentClickListener listener) {
		tournaments = new ArrayList<>();
		this.listener = listener;
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
		holder.typeTextView.setText(tournament.getType().name());
	}

	@Override
	public int getItemCount() {
		return tournaments.size();
	}

	public void addItem(Tournament tournament) {
		tournaments.add(tournament);
		notifyItemInserted(tournaments.size() - 1);
	}

	public void update(List<Tournament> tournaments) {
		this.tournaments.clear();
		this.tournaments.addAll(tournaments);
		notifyDataSetChanged();
	}

	public interface TournamentClickListener {
		void onTournamentChanged(Tournament tournament);
	}

	class TournamentViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		TextView typeTextView;

		TournamentViewHolder(View tournamentView) {
			super(tournamentView);

			nameTextView = tournamentView.findViewById(R.id.TournamentNameTextView);
			typeTextView = tournamentView.findViewById(R.id.TournamentTypeTextView);
		}
	}
}
