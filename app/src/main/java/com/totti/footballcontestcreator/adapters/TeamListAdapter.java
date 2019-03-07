package com.totti.footballcontestcreator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

	private List<Team> teams;

	public interface OnTeamSelectedListener {
		void onTeamClicked(String team);
		void onTeamLongClicked(Team team);
	}

	private OnTeamSelectedListener listener;

	public TeamListAdapter(OnTeamSelectedListener listener) {
		teams = new ArrayList<>();
		this.listener = listener;
	}

	class TeamViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;

		TeamViewHolder(final View teamView) {
			super(teamView);

			nameTextView = teamView.findViewById(R.id.TeamNameTextView);

			teamView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listener != null) {
						listener.onTeamClicked(nameTextView.getText().toString());
					}
				}
			});

			teamView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(listener != null) {
						for(Team team : teams) {
							if(team.getName().equals(nameTextView.getText().toString())) {
								listener.onTeamLongClicked(team);
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
	public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View teamView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_list_item, parent, false);
		return new TeamViewHolder(teamView);
	}

	@Override
	public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
		Team team = teams.get(position);
		holder.nameTextView.setText(team.getName());
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
