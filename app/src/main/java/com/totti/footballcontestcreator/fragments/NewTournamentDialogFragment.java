package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.totti.footballcontestcreator.adapters.TeamSelectionListAdapter;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NewTournamentDialogFragment extends DialogFragment {

	public static final String TAG = "NewTournamentDialogFragment";

	private EditText nameEditText;
	private RadioGroup typeRadioGroup;
	private RadioButton typeCRadioButton, typeERadioButton;
	private EditText roundsEditText;
	private RecyclerView teamsRecyclerView;
	private EditText commentsEditText;

	private DatabaseReference onlineMatches;
	private DatabaseReference onlineRankings;
	private DatabaseReference onlineTournaments;

	private String creator;

	private TeamViewModel teamViewModel;

	private TeamSelectionListAdapter teamSelectionListAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");
		onlineRankings = FirebaseDatabase.getInstance().getReference("rankings");
		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");

		creator = FirebaseAuth.getInstance().getCurrentUser().getEmail();

		teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.new_tournament)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(isValid()) {
							// Check if the given tournament name already exists in the database
							onlineTournaments.orderByChild("name").equalTo(nameEditText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
									if(!dataSnapshot.exists()) {
										Tournament tournament = createTournament();

										onlineTournaments.child(tournament.getId()).setValue(tournament);

										createMatchesAndRankings(tournament);
									}
									else {
										Toast.makeText(requireContext(), "Tournament name already in use!", Toast.LENGTH_SHORT).show();
									}
								}

								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {

								}
							});
						}
						else {
							Toast.makeText(requireContext(), "Tournament not created!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.new_tournament_dialog_fragment, null);

		nameEditText = contentView.findViewById(R.id.tournament_name_editText);

		typeRadioGroup = contentView.findViewById(R.id.tournament_type_radioGroup);
		typeCRadioButton = contentView.findViewById(R.id.tournament_typeC_radioButton);
		typeERadioButton = contentView.findViewById(R.id.tournament_typeE_radioButton);

		roundsEditText = contentView.findViewById(R.id.tournament_rounds_editText);

		teamSelectionListAdapter = new TeamSelectionListAdapter();
		new AsyncTask<Void, Void, List<Team>>() {
			@Override
			protected List<Team> doInBackground(Void... voids) {
				return teamViewModel.getAllTeamsAsync();
			}

			@Override
			protected void onPostExecute(List<Team> teams) {
				teamSelectionListAdapter.setTeams(teams);
			}
		}.execute();

		teamsRecyclerView = contentView.findViewById(R.id.tournament_teams_recyclerView);
		teamsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		teamsRecyclerView.setAdapter(teamSelectionListAdapter);

		commentsEditText = contentView.findViewById(R.id.tournament_comments_editText);

		return contentView;
	}

	// Check if all given values are valid
	private boolean isValid() {
		// Check if the name of the tournament is not an empty string
		if(!(nameEditText.getText().length() > 0)) {
			return false;
		}

		// Check if one of the two types is checked
		if(!typeCRadioButton.isChecked() && !typeERadioButton.isChecked()) {
			return false;
		}

		// Check if "rounds" is a positive number
		if(!(roundsEditText.getText().length() > 0) || !(Integer.parseInt(roundsEditText.getText().toString()) > 0)) {
			return false;
		}

		// Check if "rounds" is less than 3 if the type of the tournament is "Elimination"
		if(typeERadioButton.isChecked() && !(Integer.parseInt(roundsEditText.getText().toString()) < 3)) {
			return false;
		}

		// Check if there are at least 2 selected teams
		if(!(teamSelectionListAdapter.getSelectedTeams().size() >= 2)) {
			return false;
		}

		return true;
	}

	// Create a new item under Firebase "tournaments" node
	private Tournament createTournament() {
		String id = onlineTournaments.push().getKey();

		String name = nameEditText.getText().toString();

		String type = null;
		if(typeRadioGroup.getCheckedRadioButtonId() == typeCRadioButton.getId()) {
			type = "Championship";
		}
		else if(typeRadioGroup.getCheckedRadioButtonId() == typeERadioButton.getId()) {
			type = "Elimination";
		}

		int rounds = Integer.parseInt(roundsEditText.getText().toString());

		int teams = teamSelectionListAdapter.getSelectedTeams().size();

		String comments = "Created by: " + creator + "\n\n" + commentsEditText.getText().toString();

		return new Tournament(id, creator, name, type, rounds, teams, comments);
	}

	// Create rankings and matches for the tournament based on the given values
	private void createMatchesAndRankings(Tournament tournament) {
		ArrayList<Team> teams = teamSelectionListAdapter.getSelectedTeams();
		for(Team team : teams) {
			String id = onlineRankings.push().getKey();
			Ranking ranking = new Ranking(id, creator, tournament.getId(), tournament.getName(), team.getId(), team.getName(), teams.indexOf(team) + 1);
			onlineRankings.child(ranking.getId()).setValue(ranking);
		}

		ArrayList<ArrayList<Team>> pairs = createPairs(tournament.getType());

		/*
		 * Championship formulas
		 * ---------------------
		 *     Even number of teams:
		 *         Matches per Matchday = Teams / 2
		 *         Matchdays per Round = Teams - 1
		 *         All matches = Rounds * (Teams - 1) * (Teams / 2)
		 *
		 *     Odd number of teams:
		 *         Matches per Matchday = (Teams - 1) / 2
		 *         Matchdays per Round = Teams
		 *         All matches = Rounds * Teams * ((Teams - 1) / 2)
		 */
		if(tournament.getType().equals("Championship")) {
			int numberOfMatchesPerDay = (tournament.getTeams() % 2 == 0) ? (tournament.getTeams() / 2) : ((tournament.getTeams() - 1) / 2);
			int numberOfMatchDays = (tournament.getTeams() % 2 == 0) ? (tournament.getTeams() - 1) : tournament.getTeams();

			ArrayList<ArrayList<ArrayList<Team>>> matchDaysPerRound = new ArrayList<>();
			for(int i = 0; i < numberOfMatchDays; i++) {
				ArrayList<ArrayList<Team>> matchDay = createScheduleForChampionship(pairs, numberOfMatchesPerDay, ((tournament.getTeams() % 2 != 0) ? teams.get(i) : null));
				matchDaysPerRound.add(matchDay);
			}

			int day = 1;
			for(int i = 1; i <= tournament.getRounds(); i++) {
				for(ArrayList<ArrayList<Team>> matchDay : matchDaysPerRound) {
					for(ArrayList<Team> pair : matchDay) {
						String id = onlineMatches.push().getKey();
						Match newMatch;

						if(i % 2 != 0) {
							newMatch = new Match(id, creator, tournament.getId(), tournament.getName(), day, pair.get(0).getId(), pair.get(0).getName(), pair.get(1).getId(), pair.get(1).getName());
						}
						else {
							newMatch = new Match(id, creator, tournament.getId(), tournament.getName(), day, pair.get(1).getId(), pair.get(1).getName(), pair.get(0).getId(), pair.get(0).getName());
						}

						onlineMatches.child(id).setValue(newMatch);
					}
					day++;
				}
			}
		}
		// Elimination formula can be found in private createPairs function
		else if(tournament.getType().equals("Elimination")) {
			for(int i = 1; i <= tournament.getRounds(); i++) {
				for(ArrayList<Team> pair : pairs) {
					String id = onlineMatches.push().getKey();
					Match newMatch;

					if(i % 2 != 0) {
						newMatch = new Match(id, creator, tournament.getId(), tournament.getName(), i, pair.get(0).getId(), pair.get(0).getName(), pair.get(1).getId(), pair.get(1).getName());
					}
					else {
						newMatch = new Match(id, creator, tournament.getId(), tournament.getName(), i, pair.get(1).getId(), pair.get(1).getName(), pair.get(0).getId(), pair.get(0).getName());
					}

					onlineMatches.child(id).setValue(newMatch);
				}
			}
		}

		Toast.makeText(requireContext(), "Tournament \"" + tournament.getName() + "\" created with " + tournament.getTeams() + " teams!", Toast.LENGTH_SHORT).show();
	}

	/*
	 * Create all possible pairs for Championship
	 * or
	 * Do one random pairing for Elimination
	 */
	private ArrayList<ArrayList<Team>> createPairs(String tournamentType) {
		ArrayList<ArrayList<Team>> pairs = new ArrayList<>();

		ArrayList<Team> teams = teamSelectionListAdapter.getSelectedTeams();

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

	/*
	 * Organize matches by creating a matchday
	 * One team plays max. once a day considering tournaments with an odd number of teams
	 */
	private ArrayList<ArrayList<Team>> createScheduleForChampionship(ArrayList<ArrayList<Team>> pairs, int numberOfMatchesPerDay, Team teamToIgnore) {
		ArrayList<ArrayList<Team>> matchDay = new ArrayList<>();

		/*
		 * Not allowed pairs for each iteration
		 * An iteration is a successfully inserted match and all the pairs that cannot be used
		 */
		ArrayList<ArrayList<ArrayList<Team>>> notUsed = new ArrayList<>();
		for(int i = 0; i < numberOfMatchesPerDay; i++) {
			ArrayList<ArrayList<Team>> item = new ArrayList<>();
			notUsed.add(item);
		}

		// Until all matches are organized in a matchday
		while(matchDay.size() != numberOfMatchesPerDay) {
			boolean successfullyIn = false;

			for(ArrayList<Team> pair: pairs) {
				boolean notAllowedPair = false;

				// A pair is not allowed if previously set as not allowed in the matchday
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
					// A pair is not allowed if a team is ignored in the matchday
					if((teamToIgnore != null) && (pair.get(0).getName().equals(teamToIgnore.getName()) || pair.get(1).getName().equals(teamToIgnore.getName()))) {
						notAllowedPair = true;
					}
					else {
						for(ArrayList<Team> matchDayPair: matchDay) {
							// A pair is not allowed if a team already plays in the matchday
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
				// Add pair as a match to the matchday
				if(!notAllowedPair) {
					matchDay.add(pair);
					pairs.remove(pair);
					successfullyIn = true;
					break;
				}
			}
			/*
			 * If no pairs can be inserted successfully in the matchday
			 *     Add the previously successfully inserted pair to the previous list for not allowed pairs
			 *     Clear the current list for not allowed pairs
			 *     Add the previously successfully inserted pair to the pairs list
			 *     Remove the previously successfully inserted pair from the matchday
			 */
			if(!successfullyIn) {
				notUsed.get(matchDay.size() - 1).add(matchDay.get(matchDay.size() - 1));
				notUsed.get(matchDay.size()).clear();
				pairs.add(matchDay.get(matchDay.size() - 1));
				matchDay.remove(matchDay.size() - 1);
			}
		}

		return matchDay;
	}
}
