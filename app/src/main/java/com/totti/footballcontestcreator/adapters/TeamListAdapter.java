package com.totti.footballcontestcreator.adapters;

import android.content.Context;
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

	class TeamViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;

		TeamViewHolder(View teamView) {
			super(teamView);

			nameTextView = teamView.findViewById(R.id.TeamNameTextView);
		}
	}

	private List<Team> teams;

	public TeamListAdapter(Context context) {
		teams = new ArrayList<>();
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
