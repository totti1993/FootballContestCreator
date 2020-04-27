package com.totti.footballcontestcreator.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamSelectionListAdapter extends RecyclerView.Adapter<TeamSelectionListAdapter.TeamViewHolder> {

	private ArrayList<ArrayList<Object>> teams;     // List to hold all selectable teams

	public TeamSelectionListAdapter() {
		teams = new ArrayList<>();
	}

	class TeamViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		CheckBox checkBox;

		Team team;

		TeamViewHolder(final View teamView) {
			super(teamView);

			nameTextView = teamView.findViewById(R.id.team_selection_name_textView);
			checkBox = teamView.findViewById(R.id.team_selection_checkBox);

			// Listener: Select / Deselect a team
			checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(team != null && buttonView.isPressed()) {
						for(ArrayList<Object> selection : teams) {
							if(team.equals(selection.get(0))) {
								selection.set(1, isChecked);
								break;
							}
						}
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
		Team team = (Team) teams.get(position).get(0);

		holder.nameTextView.setText(team.getName());

		holder.checkBox.setChecked((Boolean) teams.get(position).get(1));

		holder.team = team;
	}

	@Override
	public int getItemCount() {
		return teams.size();
	}

	// Set teams as unselected for the dialog
	public void setTeams(List<Team> teams) {
		this.teams.clear();
		for(Team team : teams) {
			ArrayList<Object> selection = new ArrayList<>();
			selection.add(team);
			selection.add(false);
			this.teams.add(selection);
		}
		notifyDataSetChanged();
	}

	// Get only the selected teams from the list
	public ArrayList<Team> getSelectedTeams() {
		ArrayList<Team> selectedTeams = new ArrayList<>();
		for(ArrayList<Object> selection : this.teams) {
			if((Boolean) selection.get(1)) {
				selectedTeams.add((Team) selection.get(0));
			}
		}
		return selectedTeams;
	}
}
