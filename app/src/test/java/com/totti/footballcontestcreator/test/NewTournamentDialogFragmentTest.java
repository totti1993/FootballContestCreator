package com.totti.footballcontestcreator.test;

import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.fragments.NewTournamentDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class NewTournamentDialogFragmentTest {

	private String tournamentType;          // Type of tournament: "Championship" or "Elimination"
	private ArrayList<Team> teams;          // All teams
	private int numberOfMatchesPerDay;      // Number of matches per day
	private Team teamToIgnore;              // Team to ignore in a Championship matchday
	private NewTournamentDialogFragment newTournamentDialogFragment;

	@Parameterized.Parameters
	public static Collection inputParams() {
		Team a = new Team("1", "Test user", "a", "Team a");
		Team b = new Team("2", "Test user", "b", "Team b");
		Team c = new Team("3", "Test user", "c", "Team c");
		Team d = new Team("4", "Test user", "d", "Team d");
		Team e = new Team("5", "Test user", "e", "Team e");
		Team f = new Team("6", "Test user", "f", "Team f");
		Team g = new Team("7", "Test user", "g", "Team g");
		Team h = new Team("8", "Test user", "h", "Team h");

		List<Team> teams_1 = new ArrayList<>();
		teams_1.add(a);
		teams_1.add(b);
		teams_1.add(c);
		List<Team> teams_2 = new ArrayList<>();
		teams_2.add(a);
		teams_2.add(b);
		teams_2.add(c);
		teams_2.add(d);
		List<Team> teams_3 = new ArrayList<>();
		teams_3.add(a);
		teams_3.add(b);
		teams_3.add(c);
		teams_3.add(d);
		teams_3.add(e);
		List<Team> teams_4 = new ArrayList<>();
		teams_4.add(a);
		teams_4.add(b);
		teams_4.add(c);
		teams_4.add(d);
		teams_4.add(e);
		teams_4.add(f);
		List<Team> teams_5 = new ArrayList<>();
		teams_5.add(a);
		teams_5.add(b);
		teams_5.add(c);
		teams_5.add(d);
		teams_5.add(e);
		teams_5.add(f);
		teams_5.add(g);
		List<Team> teams_6 = new ArrayList<>();
		teams_6.add(a);
		teams_6.add(b);
		teams_6.add(c);
		teams_6.add(d);
		teams_6.add(e);
		teams_6.add(f);
		teams_6.add(g);
		teams_6.add(h);

		return Arrays.asList(new Object[][] {
				{"Championship", teams_1, 1, a},
				{"Championship", teams_2, 2, null},
				{"Championship", teams_3, 2, c},
				{"Championship", teams_4, 3, null},
				{"Championship", teams_5, 3, g},
				{"Championship", teams_6, 4, null},
				{"Elimination", teams_1, 1, null},      // Not
				{"Elimination", teams_2, 2, null},      // tested
				{"Elimination", teams_3, 1, null},      // for
				{"Elimination", teams_4, 2, null},      // createScheduleForChampionship_SizeTest()
				{"Elimination", teams_5, 3, null},      // and
				{"Elimination", teams_6, 4, null}       // createScheduleForChampionship_ContentTest()
		});
	}

	public NewTournamentDialogFragmentTest(String tournamentType, ArrayList<Team> teams, int numberOfMatchesPerDay, Team teamToIgnore) {
		this.tournamentType = tournamentType;
		this.teams = new ArrayList<>(teams);
		this.numberOfMatchesPerDay = numberOfMatchesPerDay;
		this.teamToIgnore = teamToIgnore;
	}

	@Before
	public void init() {
		newTournamentDialogFragment = new NewTournamentDialogFragment();
	}

	// Test: Check number of created pairs
	@Test
	public void createPairs_SizeTest() {
		if(tournamentType.equals("Championship")) {
			System.out.println("Expected number of created pairs: " + (int)(((double)teams.size() / 2) * (1 + teams.size()) - teams.size()));

			assertEquals("Check number of created pairs", (int)(((double)teams.size() / 2) * (1 + teams.size()) - teams.size()), newTournamentDialogFragment.createPairs_OnlyForTesting(tournamentType, teams).size());
		}
		else if(tournamentType.equals("Elimination")) {
			System.out.println("Expected number of created pairs: " + numberOfMatchesPerDay);

			assertEquals("Check number of created pairs", numberOfMatchesPerDay, newTournamentDialogFragment.createPairs_OnlyForTesting(tournamentType, teams).size());
		}
	}

	// Test: Check content of created pairs
	@Test
	public void createPairs_ContentTest() {
		// It is impossible to tell the exact pairs because of the shuffles
		System.out.print("Expected teams: ");
		for(Team team : teams) {
			System.out.print(team.getName() + ", ");
		}

		if(tournamentType.equals("Championship")) {
			boolean sameTeams = true;
			for(Team team : teams) {
				int counter = 0;
				for(ArrayList<Team> pair : newTournamentDialogFragment.createPairs_OnlyForTesting(tournamentType, teams)) {
					for(Team t : pair) {
						if(team.equals(t)) {
							counter++;
							break;
						}
					}
				}
				if(counter != (teams.size() - 1)) {
					sameTeams = false;
				}
				if(!sameTeams) {
					break;
				}
			}

			assertTrue("Check content of created pairs", sameTeams);
		}
		else if(tournamentType.equals("Elimination")) {
			ArrayList<Team> selectedTeams = new ArrayList<>();
			boolean distinctTeams = true;
			for(ArrayList<Team> pair : newTournamentDialogFragment.createPairs_OnlyForTesting(tournamentType, teams)) {
				for(Team t : pair) {
					if(selectedTeams.contains(t)) {
						distinctTeams = false;
						break;
					}
					else {
						selectedTeams.add(t);
					}
				}
				if(!distinctTeams) {
					break;
				}
			}

			assertTrue("Check content of created pairs", distinctTeams);
		}
	}

	// Test: Check number of created matches in the matchday
	@Test
	public void createScheduleForChampionship_SizeTest() {
		if(tournamentType.equals("Championship")) {
			System.out.println("Expected number of created matches in the matchday: " + numberOfMatchesPerDay);

			assertEquals("Check number of created matches in the matchday", numberOfMatchesPerDay, newTournamentDialogFragment.createScheduleForChampionship_OnlyForTesting(newTournamentDialogFragment.createPairs_OnlyForTesting(tournamentType, teams), numberOfMatchesPerDay, teamToIgnore).size());
		}
	}

	// Test: Check content of created matches in the matchday
	@Test
	public void createScheduleForChampionship_ContentTest() {
		if(tournamentType.equals("Championship")) {
			// It is impossible to tell the exact pairs because of the shuffles
			System.out.print("Expected teams: ");
			for(Team team : teams) {
				System.out.print(team.getName() + ", ");
			}

			ArrayList<Team> allTeams = new ArrayList<>();
			if(teamToIgnore != null) {
				allTeams.add(teamToIgnore);
			}
			boolean distinctTeams = true;
			for(ArrayList<Team> pair : newTournamentDialogFragment.createScheduleForChampionship_OnlyForTesting(newTournamentDialogFragment.createPairs_OnlyForTesting(tournamentType, teams), numberOfMatchesPerDay, teamToIgnore)) {
				for(Team t : pair) {
					if(allTeams.contains(t)) {
						distinctTeams = false;
						break;
					}
					else {
						allTeams.add(t);
					}
				}
				if(!distinctTeams) {
					break;
				}
			}

			assertTrue("Check content of created matches in the matchday", distinctTeams);
		}
	}
}
