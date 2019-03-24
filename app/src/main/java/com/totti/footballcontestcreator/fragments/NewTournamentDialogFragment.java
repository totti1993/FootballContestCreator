package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.adapters.TeamSelectionListAdapter;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class NewTournamentDialogFragment extends DialogFragment implements TeamSelectionListAdapter.OnTeamClickedListener {

	public static final String TAG = "NewTournamentDialogFragment";

	private EditText nameEditText;
	private RadioGroup typeRadioGroup;
	private RadioButton typeCRadioButton, typeERadioButton;
	private EditText roundsEditText;
	private RecyclerView teamsRecyclerView;
	private EditText commentsEditText;

	private TournamentViewModel tournamentViewModel;
	private TeamViewModel teamViewModel;

	private TeamSelectionListAdapter teamSelectionListAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tournamentViewModel = ViewModelProviders.of(getActivity()).get(TournamentViewModel.class);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.new_tournament)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if (isValid()) {
							tournamentViewModel.insert(getTournament());
							// insert ranking table here
							Toast.makeText(getContext(), "Tournament \"" + getTournament().getName() + "\" created with " + getTournament().getTeams().toString() + " team(s)!", Toast.LENGTH_SHORT).show();
						}
						else {
							Toast.makeText(getContext(), "Tournament not created!", Toast.LENGTH_SHORT).show();
						}

						for(Team team : teamSelectionListAdapter.getTeams()) {
							if(team.getSelected()) {
								team.setSelected(false);
								teamViewModel.update(team);
							}
						}
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						for(Team team : teamSelectionListAdapter.getTeams()) {
							if(team.getSelected()) {
								team.setSelected(false);
								teamViewModel.update(team);
							}
						}
					}
				})
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.new_tournament_dialog_fragment, null);

		nameEditText = contentView.findViewById(R.id.tournament_name_editText);

		typeRadioGroup = contentView.findViewById(R.id.tournament_type_radioGroup);
		typeCRadioButton = contentView.findViewById(R.id.tournament_typeC_radioButton);
		typeERadioButton = contentView.findViewById(R.id.tournament_typeE_radioButton);

		roundsEditText = contentView.findViewById(R.id.tournament_rounds_editText);

		teamSelectionListAdapter = new TeamSelectionListAdapter(this);
		teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
		teamViewModel.getAllTeams().observe(this, new Observer<List<Team>>() {
			@Override
			public void onChanged(@Nullable List<Team> teams) {
				teamSelectionListAdapter.setTeams(teams);
			}
		});
		teamsRecyclerView = contentView.findViewById(R.id.tournament_teams_recyclerView);
		teamsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		teamsRecyclerView.setAdapter(teamSelectionListAdapter);

		commentsEditText = contentView.findViewById(R.id.tournament_comments_editText);

		return contentView;
	}

	private boolean isValid() {
		return nameEditText.getText().length() > 0;
	}

	private Tournament getTournament() {
		String name = nameEditText.getText().toString();

		String type = null;
		if(typeRadioGroup.getCheckedRadioButtonId() == typeCRadioButton.getId()) {
			type = "Championship";
		}
		else if(typeRadioGroup.getCheckedRadioButtonId() == typeERadioButton.getId()) {
			type = "Elimination";
		}

		Integer rounds = Integer.parseInt(roundsEditText.getText().toString());

		Integer teams = 0;
		for(Team team : teamSelectionListAdapter.getTeams()) {
			if(team.getSelected()) {
				teams++;
			}
		}

		String comments = commentsEditText.getText().toString();

		return new Tournament(name, type, rounds, teams, comments);
	}

	@Override
	public void onCheckBoxClicked(Team team) {
		teamViewModel.update(team);
	}
}
