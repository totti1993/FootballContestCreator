package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.List;

public class NewTournamentDialogFragment extends DialogFragment implements TeamSelectionListAdapter.OnTeamSelectedListener {

	private EditText nameEditText;
	private RadioGroup typeRadioGroup;
	private RadioButton typeCRadioButton, typeERadioButton;
	private EditText roundsEditText;
	private RecyclerView teamsRecyclerView;
	private EditText commentsEditText;

	private TeamSelectionListAdapter teamSelectionListAdapter;

	private TeamViewModel teamViewModel;

	public static final String TAG = "NewTournamentDialogFragment";

	public interface NewTournamentDialogListener {
		void onTournamentCreated(Tournament newTournament, List<Team> selectedTeams);
	}

	private NewTournamentDialogListener listener;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentActivity activity = getActivity();
		if (activity instanceof NewTournamentDialogListener) {
			listener = (NewTournamentDialogListener) activity;
		} else {
			throw new RuntimeException("Activity must implement the NewTournamentDialogListener interface!");
		}
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
							List<Team> selectedTeams = new ArrayList<>();
							for(Team team : teamSelectionListAdapter.getTeams()) {
								if(team.getSelected()) {
									selectedTeams.add(team);
								}
							}
							listener.onTournamentCreated(getTournament(), selectedTeams);
						}
						else {
							Toast.makeText(getActivity(), "Tournament not created!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_tournament, null);
		nameEditText = contentView.findViewById(R.id.TournamentNameEditText);

		typeRadioGroup = contentView.findViewById(R.id.TournamentTypeRadioGroup);
		typeCRadioButton = contentView.findViewById(R.id.TournamentTypeCRadioButton);
		typeERadioButton = contentView.findViewById(R.id.TournamentTypeERadioButton);

		roundsEditText = contentView.findViewById(R.id.TournamentRoundsEditText);

		teamsRecyclerView = contentView.findViewById(R.id.TournamentTeamsRecyclerView);
		teamsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		teamSelectionListAdapter = new TeamSelectionListAdapter(this);
		teamViewModel = ViewModelProviders.of(this).get(TeamViewModel.class);
		teamViewModel.getAllTeams().observe(this, new Observer<List<Team>>() {
			@Override
			public void onChanged(@Nullable List<Team> teams) {
				teamSelectionListAdapter.setTeams(teams);
			}
		});
		teamsRecyclerView.setAdapter(teamSelectionListAdapter);

		commentsEditText = contentView.findViewById(R.id.TournamentCommentsEditText);

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
