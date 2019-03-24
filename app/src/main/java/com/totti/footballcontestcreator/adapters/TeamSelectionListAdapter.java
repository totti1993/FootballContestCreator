package com.totti.footballcontestcreator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamSelectionListAdapter extends RecyclerView.Adapter<TeamSelectionListAdapter.TeamViewHolder> {

	private List<Team> teams;

	public interface OnTeamClickedListener {
		void onCheckBoxClicked(Team team);
	}

	private OnTeamClickedListener listener;

	public TeamSelectionListAdapter(OnTeamClickedListener listener) {
		teams = new ArrayList<>();
		this.listener = listener;
	}

	class TeamViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		CheckBox checkBox;

		Team team;

		TeamViewHolder(final View teamView) {
			super(teamView);

			nameTextView = teamView.findViewById(R.id.team_selection_name_textView);
			checkBox = teamView.findViewById(R.id.team_selection_checkBox);

			checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(team != null && buttonView.isPressed()) {
						team.setSelected(isChecked);
						listener.onCheckBoxClicked(team);
					}
				}
			});
		}
	}

	@NonNull
	@Override
	public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View teamView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_selection_list_item, parent, false);
		return new TeamViewHolder(teamView);
	}

	@Override
	public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
		Team team = teams.get(position);
		holder.nameTextView.setText(team.getName());
		holder.checkBox.setChecked(team.getSelected());
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

	public List<Team> getTeams() {
		return teams;
	}
}
