package com.totti.footballcontestcreator.test;

import com.totti.footballcontestcreator.adapters.TeamSelectionListAdapter;
import com.totti.footballcontestcreator.database.Team;

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
public class TeamSelectionListAdapterTest {

	private List<Team> teams;       // All teams
	private int[] selected;         // Indexes of selected teams
	private TeamSelectionListAdapter teamSelectionListAdapter;

	@Parameterized.Parameters
	public static Collection inputParams() {
		List<Team> teams = new ArrayList<>();
		teams.add(new Team("1", "Test user", "Team 1", "First"));
		teams.add(new Team("2", "Test user", "Team 2", "Second"));
		teams.add(new Team("3", "Test user", "Team 3", "Third"));
		teams.add(new Team("4", "Test user", "Team 4", "Fourth"));
		teams.add(new Team("5", "Test user", "Team 5", "Fifth"));

		return Arrays.asList(new Object[][] {
				{teams, new int[] {}},
				{teams, new int[] {0, 1, 2, 3, 4}},
				{teams, new int[] {1, 3}}
		});
	}

	public TeamSelectionListAdapterTest(List<Team> teams, int[] selected) {
		this.teams = teams;
		this.selected = selected;
	}

	@Before
	public void init() {
		teamSelectionListAdapter = new TeamSelectionListAdapter();
		teamSelectionListAdapter.updateTeams_OnlyForTesting(teams, selected);   // Set all teams, selected too
	}

	// Test: Check number of selected teams
	@Test
	public void getSelectedTeams_SizeTest() {
		System.out.println("Expected number of selected teams: " + selected.length);

		assertEquals("Check number of selected teams", selected.length, teamSelectionListAdapter.getSelectedTeams().size());
	}

	// Test: Check content of selected teams
	@Test
	public void getSelectedTeams_ContentTest() {
		System.out.print("Expected selected teams: ");
		for(int i : selected) {
			System.out.print(i + ", ");
		}

		boolean sameSelected = true;
		ArrayList<Team> selectedTeams = teamSelectionListAdapter.getSelectedTeams();
		for(int i = 0; i < selectedTeams.size(); i++) {
			if(teams.indexOf(selectedTeams.get(i)) != selected[i]) {
				sameSelected = false;
				break;
			}
		}

		assertTrue("Check content of selected teams", sameSelected);
	}
}
