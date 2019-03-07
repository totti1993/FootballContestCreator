package com.totti.footballcontestcreator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentListAdapter extends RecyclerView.Adapter<TournamentListAdapter.TournamentViewHolder> {

	private List<Tournament> tournaments;

	public interface OnTournamentSelectedListener {
		void onTournamentClicked(String tournament);
		void onTournamentLongClicked(Tournament tournament);
	}

	private OnTournamentSelectedListener listener;

	public TournamentListAdapter(OnTournamentSelectedListener listener) {
		tournaments = new ArrayList<>();
		this.listener = listener;
	}

	class TournamentViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		TextView typeTextView;

		TournamentViewHolder(View tournamentView) {
			super(tournamentView);

			nameTextView = tournamentView.findViewById(R.id.TournamentNameTextView);
			typeTextView = tournamentView.findViewById(R.id.TournamentTypeTextView);

			tournamentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listener != null) {
						listener.onTournamentClicked(nameTextView.getText().toString());
					}
				}
			});

			tournamentView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(listener != null) {
						for(Tournament tournament : tournaments) {
							if(tournament.getName().equals(nameTextView.getText().toString())) {
								listener.onTournamentLongClicked(tournament);
								break;
							}
						}
						return true;
					}
					return false;
				}
			});
		}
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
