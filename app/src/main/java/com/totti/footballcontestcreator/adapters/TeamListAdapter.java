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
import com.totti.footballcontestcreator.database.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

	private List<Team> teams;

	public interface OnTeamSelectedListener {
		void onTeamClicked(Team team);
		void onTeamLongClicked(Team team);
		void onTeamStarClicked(Team team);
	}

	private OnTeamSelectedListener listener;

	public TeamListAdapter(OnTeamSelectedListener listener) {
		teams = new ArrayList<>();
		this.listener = listener;
	}

	class TeamViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		ToggleButton favoriteToggleButton;

		Team team;

		TeamViewHolder(final View teamView) {
			super(teamView);

			nameTextView = teamView.findViewById(R.id.TeamNameTextView);
			favoriteToggleButton = teamView.findViewById(R.id.TeamFavoriteToggleButton);

			teamView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(team != null) {
						listener.onTeamClicked(team);
					}
				}
			});

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

			favoriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(team != null && buttonView.isPressed()) {
						team.setFavorite(isChecked);
						listener.onTeamStarClicked(team);
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
		Team team = teams.get(position);
		holder.nameTextView.setText(team.getName());
		if(team.getFavorite()) {
			holder.favoriteToggleButton.setChecked(true);
			holder.favoriteToggleButton.setBackgroundResource(R.drawable.ic_star_green);
		}
		else {
			holder.favoriteToggleButton.setChecked(false);
			holder.favoriteToggleButton.setBackgroundResource(R.drawable.ic_star_border_grey);
		}
		holder.team = team;
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	public void setTeams(List<Team> teams) {
		this.teams.clear();
		this.teams.addAll(teams);
		notifyDataSetChanged();
	}
}
