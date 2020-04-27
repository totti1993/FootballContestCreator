package com.totti.footballcontestcreator.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentListAdapter extends RecyclerView.Adapter<TournamentListAdapter.TournamentViewHolder> {

	private ArrayList<ArrayList<Object>> tournaments;       // List to hold all tournaments

	public interface OnTournamentClickedListener {
		void onTournamentClicked(Tournament tournament);
		void onTournamentLongClicked(Tournament tournament);
		void onTournamentStarClicked(Tournament tournament, boolean favorite);
	}

	private OnTournamentClickedListener listener;

	public TournamentListAdapter(OnTournamentClickedListener listener) {
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

			nameTextView = tournamentView.findViewById(R.id.tournament_name_textView);
			typeTextView = tournamentView.findViewById(R.id.tournament_type_textView);
			favoriteToggleButton = tournamentView.findViewById(R.id.tournament_favorite_toggleButton);

			// Listener: Click on a tournament
			tournamentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(tournament != null) {
						listener.onTournamentClicked(tournament);
					}
				}
			});

			// Listener: Long click on a tournament
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

			// Listener: Click on a star next to a tournament
			favoriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(tournament != null && buttonView.isPressed()) {
						for(ArrayList<Object> tournamentWithFavorite : tournaments) {
							if(tournament.equals(tournamentWithFavorite.get(0))) {
								tournamentWithFavorite.set(1, isChecked);
								break;
							}
						}
						listener.onTournamentStarClicked(tournament, isChecked);
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
		Tournament tournament = (Tournament) tournaments.get(position).get(0);

		holder.nameTextView.setText(tournament.getName());

		holder.typeTextView.setText(tournament.getType());

		holder.favoriteToggleButton.setChecked((Boolean) tournaments.get(position).get(1));
		holder.favoriteToggleButton.setBackgroundResource(((Boolean) tournaments.get(position).get(1)) ? R.drawable.ic_star_green : R.drawable.ic_star_border_grey);

		holder.tournament = tournament;
	}

	@Override
	public int getItemCount() {
		return tournaments.size();
	}

	// Set favorite and non-favorite tournaments for the fragment
	public void setTournaments(List<Tournament> tournaments, List<String> favorites) {
		this.tournaments.clear();
		for(Tournament tournament : tournaments) {
			if(favorites.contains(tournament.getId())) {
				ArrayList<Object> tournamentWithFavorite = new ArrayList<>();
				tournamentWithFavorite.add(tournament);
				tournamentWithFavorite.add(true);
				this.tournaments.add(tournamentWithFavorite);
			}
		}
		for(Tournament tournament : tournaments) {
			if(!favorites.contains(tournament.getId())) {
				ArrayList<Object> tournamentWithoutFavorite = new ArrayList<>();
				tournamentWithoutFavorite.add(tournament);
				tournamentWithoutFavorite.add(false);
				this.tournaments.add(tournamentWithoutFavorite);
			}
		}
		notifyDataSetChanged();
	}
}
