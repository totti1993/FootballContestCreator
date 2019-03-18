package com.totti.footballcontestcreator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentListAdapter extends RecyclerView.Adapter<TournamentListAdapter.TournamentViewHolder> {

	private List<Tournament> tournaments;

	public interface OnTournamentSelectedListener {
		void onTournamentClicked(Tournament tournament);
		void onTournamentLongClicked(Tournament tournament);
		void onTournamentStarClicked(Tournament tournament);
	}

	private OnTournamentSelectedListener listener;

	public TournamentListAdapter(OnTournamentSelectedListener listener) {
		tournaments = new ArrayList<>();
		this.listener = listener;
	}

	class TournamentViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		TextView typeTextView;
		ToggleButton favoriteToggleButton;

		Tournament tournament;

		TournamentViewHolder(final View tournamentView) {
			super(tournamentView);

			nameTextView = tournamentView.findViewById(R.id.TournamentNameTextView);
			typeTextView = tournamentView.findViewById(R.id.TournamentTypeTextView);
			favoriteToggleButton = tournamentView.findViewById(R.id.TournamentFavoriteToggleButton);

			tournamentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(tournament != null) {
						listener.onTournamentClicked(tournament);
					}
				}
			});

			tournamentView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(tournament != null) {
						listener.onTournamentLongClicked(tournament);
						return true;
					}
					return false;
				}
			});

			favoriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(tournament != null && buttonView.isPressed()) {
						tournament.setFavorite(isChecked);
						listener.onTournamentStarClicked(tournament);
					}
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
		if(tournament.getFavorite()) {
			holder.favoriteToggleButton.setChecked(true);
			holder.favoriteToggleButton.setBackgroundResource(R.drawable.ic_star_green);
		}
		else {
			holder.favoriteToggleButton.setChecked(false);
			holder.favoriteToggleButton.setBackgroundResource(R.drawable.ic_star_border_grey);
		}
		holder.tournament = tournament;
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
