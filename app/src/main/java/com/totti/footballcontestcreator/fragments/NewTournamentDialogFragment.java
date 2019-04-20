package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NewTournamentDialogFragment extends DialogFragment implements TeamSelectionListAdapter.OnTeamClickedListener {

	public static final String TAG = "NewTournamentDialogFragment";

	private EditText nameEditText;
	private RadioGroup typeRadioGroup;
	private RadioButton typeCRadioButton, typeERadioButton;
	private EditText roundsEditText;
	private RecyclerView teamsRecyclerView;
	private EditText commentsEditText;

	private MatchViewModel matchViewModel;
	private RankingViewModel rankingViewModel;
	private TeamViewModel teamViewModel;
	private TournamentViewModel tournamentViewModel;

	private TeamSelectionListAdapter teamSelectionListAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		matchViewModel = ViewModelProviders.of(getActivity()).get(MatchViewModel.class);
		rankingViewModel = ViewModelProviders.of(getActivity()).get(RankingViewModel.class);
		teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
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
						if(isValid()) {
							final Tournament tournament = getTournament();

							new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... voids) {
									tournament.setId(tournamentViewModel.insert(tournament));
									return null;
								}

								@Override
								protected void onPostExecute(Void aVoid) {
									createMatchesAndRankings(tournament);
								}
							}.execute();
						}
						else {
							Toast.makeText(getContext(), "Tournament not created!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
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
		if(!(nameEditText.getText().length() > 0)) {
			return false;
		}

		if(!typeCRadioButton.isChecked() && !typeERadioButton.isChecked()) {
			return false;
		}

		if(!(roundsEditText.getText().length() > 0) || !(Integer.parseInt(roundsEditText.getText().toString()) > 0)) {
			return false;
		}

		int selectedTeams = 0;
		for(Team team : teamSelectionListAdapter.getTeams()) {
			if(team.getSelected()) {
				selectedTeams++;
				if(selectedTeams >= 2) {
					break;
				}
			}
		}
		if(!(selectedTeams >= 2)) {
			return false;
		}

		return true;
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

		int rounds = Integer.parseInt(roundsEditText.getText().toString());

		int teams = 0;
		for(Team team : teamSelectionListAdapter.getTeams()) {
			if(team.getSelected()) {
				teams++;
			}
		}

		String comments = commentsEditText.getText().toString();

		return new Tournament(name, type, rounds, teams, comments);
	}

	private void createMatchesAndRankings(final Tournament tournament) {
		ArrayList<Team> teams = new ArrayList<>();
		for(final Team team : teamSelectionListAdapter.getTeams()) {
			if(team.getSelected()) {
				teams.add(team);
				new AsyncTask<ArrayList<Team>, Void, Void>() {
					@Override
					protected Void doInBackground(ArrayList<Team>... teams) {
						rankingViewModel.insert(new Ranking(tournament.getId(), team.getId(), teams[0].indexOf(team) + 1));
						return null;
					}
				}.execute(teams);
			}
		}

		ArrayList<ArrayList<Team>> pairs = createPairs(tournament.getType());

		if(tournament.getType().equals("Championship")) {
			int numberOfMatchesPerDay = (tournament.getTeams() % 2 == 0) ? (tournament.getTeams() / 2) : ((tournament.getTeams() - 1) / 2);
			int numberOfMatchDays = (tournament.getTeams() % 2 == 0) ? (tournament.getTeams() - 1) : tournament.getTeams();

			ArrayList<ArrayList<ArrayList<Team>>> matchDaysPerRound = new ArrayList<>();
			for(int j = 0; j < numberOfMatchDays; j++) {
				ArrayList<ArrayList<Team>> matchDay = createScheduleForChampionship(pairs, numberOfMatchesPerDay, ((tournament.getTeams() % 2 != 0) ? teams.get(j) : null));
				matchDaysPerRound.add(matchDay);
			}

			int day = 1;
			for(int j = 1; j <= tournament.getRounds(); j++) {
				for(ArrayList<ArrayList<Team>> matchDay : matchDaysPerRound) {
					for(final ArrayList<Team> match : matchDay) {
						if(j % 2 != 0) {
							new AsyncTask<Integer, Void, Void>() {
								@Override
								protected Void doInBackground(Integer... day) {
									matchViewModel.insert(new Match(tournament.getId(), day[0], match.get(0).getId(), match.get(1).getId()));
									return null;
								}
							}.execute(day);
						}
						else {
							new AsyncTask<Integer, Void, Void>() {
								@Override
								protected Void doInBackground(Integer... day) {
									matchViewModel.insert(new Match(tournament.getId(), day[0], match.get(1).getId(), match.get(0).getId()));
									return null;
								}
							}.execute(day);
						}
					}
					day++;
				}
			}
		}
		else if(tournament.getType().equals("Elimination")) {
			for(int j = 1; j <= tournament.getRounds(); j++) {
				for(final ArrayList<Team> match : pairs) {
					if(j % 2 != 0) {
						new AsyncTask<Integer, Void, Void>() {
							@Override
							protected Void doInBackground(Integer... day) {
								matchViewModel.insert(new Match(tournament.getId(), day[0], match.get(0).getId(), match.get(1).getId()));
								return null;
							}
						}.execute(j);
					}
					else {
						new AsyncTask<Integer, Void, Void>() {
							@Override
							protected Void doInBackground(Integer... day) {
								matchViewModel.insert(new Match(tournament.getId(), day[0], match.get(1).getId(), match.get(0).getId()));
								return null;
							}
						}.execute(j);
					}
				}
			}
		}

		Toast.makeText(getContext(), "Tournament \"" + tournament.getName() + "\" created with " + tournament.getTeams() + " teams!", Toast.LENGTH_SHORT).show();

		for(final Team team : teamSelectionListAdapter.getTeams()) {
			if(team.getSelected()) {
				team.setSelected(false);
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						teamViewModel.update(team);
						return null;
					}
				}.execute();
			}
		}
	}

	private ArrayList<ArrayList<Team>> createPairs(String tournamentType) {
		ArrayList<ArrayList<Team>> pairs = new ArrayList<>();

		// Collection of teams participating in the tournament
		ArrayList<Team> teams = new ArrayList<>();
		for(Team team : teamSelectionListAdapter.getTeams()) {
			if(team.getSelected()) {
				teams.add(team);
			}
		}

		if(tournamentType.equals("Championship")) {

			// Match generator for a round
			for(int i = 0; i < teams.size() - 1; i++) {
				for(int j = i + 1; j < teams.size(); j++) {
					ArrayList<Team> newPair = new ArrayList<>();
					newPair.add(teams.get(i));
					newPair.add(teams.get(j));
					Collections.shuffle(newPair);
					pairs.add(newPair);
				}
			}
		}
		else if(tournamentType.equals("Elimination")) {

			// The highest power of 2 that is less than or equal to the number of teams
			int highestPower = 1;
			while(highestPower <= teams.size()) {
				highestPower *= 2;
			}
			highestPower /= 2;

			// The number of teams who must play a qualifier round
			int numberOfQualifiables = 2 * (teams.size() - highestPower);

			// The number of matches in the first round of the tournament based on the existence of the qualifier round
			int numberOfMatches = (numberOfQualifiables > 0) ? (numberOfQualifiables / 2) : (teams.size() / 2);

			// Match generator for a round
			Random rand = new Random();
			for(int i = 0; i < numberOfMatches; i++) {
				Team firstTeam = teams.get(rand.nextInt(teams.size()));
				teams.remove(firstTeam);
				Team secondTeam = teams.get(rand.nextInt(teams.size()));
				teams.remove(secondTeam);

				ArrayList<Team> newPair = new ArrayList<>();
				newPair.add(firstTeam);
				newPair.add(secondTeam);
				Collections.shuffle(newPair);
				pairs.add(newPair);
			}
		}

		Collections.shuffle(pairs);
		return pairs;
	}

	private ArrayList<ArrayList<Team>> createScheduleForChampionship(ArrayList<ArrayList<Team>> pairs, int numberOfMatchesPerDay, Team teamToIgnore) {
		ArrayList<ArrayList<Team>> matchDay = new ArrayList<>();

		ArrayList<ArrayList<ArrayList<Team>>> notUsed = new ArrayList<>();
		for(int i = 0; i < numberOfMatchesPerDay; i++) {
			ArrayList<ArrayList<Team>> item = new ArrayList<>();
			notUsed.add(item);
		}

		while(matchDay.size() != numberOfMatchesPerDay) {
			boolean successfullyIn = false;

			for(ArrayList<Team> pair: pairs) {
				boolean notAllowedPair = false;

				if(!notUsed.get(matchDay.size()).isEmpty()) {
					for(ArrayList<Team> notUsedPair : notUsed.get(matchDay.size())) {
						if(pair.get(0).getName().equals(notUsedPair.get(0).getName()) &&
								pair.get(1).getName().equals(notUsedPair.get(1).getName())) {
							notAllowedPair = true;
							break;
						}
					}
				}
				if(!notAllowedPair) {
					if((teamToIgnore != null) && (pair.get(0).getName().equals(teamToIgnore.getName()) || pair.get(1).getName().equals(teamToIgnore.getName()))) {
						notAllowedPair = true;
					}
					else {
						for(ArrayList<Team> matchDayPair: matchDay) {
							if(pair.get(0).getName().equals(matchDayPair.get(0).getName()) ||
									pair.get(0).getName().equals(matchDayPair.get(1).getName()) ||
									pair.get(1).getName().equals(matchDayPair.get(0).getName()) ||
									pair.get(1).getName().equals(matchDayPair.get(1).getName())) {
								notAllowedPair = true;
								break;
							}
						}
					}
				}
				if(!notAllowedPair) {
					matchDay.add(pair);
					pairs.remove(pair);
					successfullyIn = true;
					break;
				}
			}
			if(!successfullyIn) {
				notUsed.get(matchDay.size() - 1).add(matchDay.get(matchDay.size() - 1));
				notUsed.get(matchDay.size()).clear();
				pairs.add(matchDay.get(matchDay.size() - 1));
				matchDay.remove(matchDay.size() - 1);
			}
		}

		return matchDay;
	}

	@Override
	public void onCheckBoxClicked(final Team team) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				teamViewModel.update(team);
				return null;
			}
		}.execute();
	}
}
