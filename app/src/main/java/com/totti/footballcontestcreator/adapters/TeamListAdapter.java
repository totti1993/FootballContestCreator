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
import com.totti.footballcontestcreator.database.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

	private ArrayList<ArrayList<Object>> teams;     // List to hold all teams

	public interface OnTeamClickedListener {
		void onTeamClicked(Team team);
		void onTeamLongClicked(Team team);
		void onTeamStarClicked(Team team, boolean favorite);
	}

	private OnTeamClickedListener listener;

	public TeamListAdapter(OnTeamClickedListener listener) {
		teams = new ArrayList<>();
		this.listener = listener;
	}

	class TeamViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		ToggleButton favoriteToggleButton;

		Team team;

		TeamViewHolder(final View teamView) {
			super(teamView);

			nameTextView = teamView.findViewById(R.id.team_name_textView);
			favoriteToggleButton = teamView.findViewById(R.id.team_favorite_toggleButton);

			// Listener: Click on a team
			teamView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(team != null) {
						listener.onTeamClicked(team);
					}
				}
			});

			// Listener: Long click on a team
			teamView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(team != null) {
						listener.onTeamLongClicked(team);
						return true;
					}
					return false;
				}
			});

			// Listener: Click on a star next to a team
			favoriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(team != null && buttonView.isPressed()) {
						for(ArrayList<Object> teamWithFavorite : teams) {
							if(team.equals(teamWithFavorite.get(0))) {
								teamWithFavorite.set(1, isChecked);
								break;
							}
						}
						listener.onTeamStarClicked(team, isChecked);
					}
				}
			});
		}
	}

	@NonNull
	@Override
	public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View teamView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_list_item, parent, false);
		return new TeamViewHolder(teamView);
	}

	@Override
	public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
		Team team = (Team) teams.get(position).get(0);

		holder.nameTextView.setText(team.getName());

		holder.favoriteToggleButton.setChecked((Boolean) teams.get(position).get(1));
		holder.favoriteToggleButton.setBackgroundResource(((Boolean) teams.get(position).get(1)) ? R.drawable.ic_star_green : R.drawable.ic_star_border_grey);

		holder.team = team;
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	// Set favorite and non-favorite teams for the fragment
	public void setTeams(List<Team> teams, List<String> favorites) {
		this.teams.clear();
		for(Team team : teams) {
			if(favorites.contains(team.getId())) {
				ArrayList<Object> teamWithFavorite = new ArrayList<>();
				teamWithFavorite.add(team);
				teamWithFavorite.add(true);
				this.teams.add(teamWithFavorite);
			}
		}
		for(Team team : teams) {
			if(!favorites.contains(team.getId())) {
				ArrayList<Object> teamWithoutFavorite = new ArrayList<>();
				teamWithoutFavorite.add(team);
				teamWithoutFavorite.add(false);
				this.teams.add(teamWithoutFavorite);
			}
		}
		notifyDataSetChanged();
	}
}
