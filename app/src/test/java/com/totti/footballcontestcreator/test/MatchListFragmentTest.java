package com.totti.footballcontestcreator.test;

import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.fragments.MatchListFragment;

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
public class MatchListFragmentTest {

	private ArrayList<Ranking> rankings;        // All rankings
	private MatchListFragment matchListFragment;

	@Parameterized.Parameters
	public static Collection inputParams() {
		// Only an even number of rankings can be a parameter for the function under test
		List<Ranking> rankings_1 = new ArrayList<>();
		rankings_1.add(new Ranking("1", "Test user", "13", "Tournament 1", "16", "Team 1", 1));
		rankings_1.add(new Ranking("2", "Test user", "13", "Tournament 1", "17", "Team 2", 2));
		List<Ranking> rankings_2 = new ArrayList<>();
		rankings_2.add(new Ranking("3", "Test user", "14", "Tournament 2", "18", "Team 3", 1));
		rankings_2.add(new Ranking("4", "Test user", "14", "Tournament 2", "19", "Team 4", 2));
		rankings_2.add(new Ranking("5", "Test user", "14", "Tournament 2", "20", "Team 5", 3));
		rankings_2.add(new Ranking("6", "Test user", "14", "Tournament 2", "21", "Team 6", 4));
		List<Ranking> rankings_3 = new ArrayList<>();
		rankings_3.add(new Ranking("7", "Test user", "15", "Tournament 3", "22", "Team 7", 1));
		rankings_3.add(new Ranking("8", "Test user", "15", "Tournament 3", "23", "Team 8", 2));
		rankings_3.add(new Ranking("9", "Test user", "15", "Tournament 3", "24", "Team 9", 3));
		rankings_3.add(new Ranking("10", "Test user", "15", "Tournament 3", "25", "Team 10", 4));
		rankings_3.add(new Ranking("11", "Test user", "15", "Tournament 3", "26", "Team 11", 5));
		rankings_3.add(new Ranking("12", "Test user", "15", "Tournament 3", "27", "Team 12", 6));

		return Arrays.asList(rankings_1, rankings_2, rankings_3);
	}

	public MatchListFragmentTest(List<Ranking> rankings) {
		this.rankings = new ArrayList<>(rankings);
	}

	@Before
	public void init() {
		matchListFragment = new MatchListFragment();
	}

	// Test: Check number of generated pairs
	@Test
	public void generateNextRound_OnlyForTesting_SizeTest() {
		System.out.println("Expected number of generated pairs: " + (rankings.size() / 2));

		assertEquals("Check number of generated pairs", (rankings.size() / 2), matchListFragment.generateNextRound_OnlyForTesting(rankings).size());
	}

	// Test: Check content of generated pairs
	@Test
	public void generateNextRound_OnlyForTesting_ContentTest() {
		// It is impossible to tell the exact pairs because of the shuffles
		System.out.print("Expected rankings: ");
		for(Ranking ranking : rankings) {
			System.out.print(ranking.getTeam_name() + ", ");
		}

		ArrayList<Ranking> rankingsCopy = new ArrayList<>(rankings);
		boolean sameRankings = true;
		for(ArrayList<Ranking> pair : matchListFragment.generateNextRound_OnlyForTesting(rankings)) {
			for(Ranking ranking : pair) {
				if(!rankingsCopy.contains(ranking)) {
					sameRankings = false;
					break;
				}
			}
			if(!sameRankings) {
				break;
			}
		}

		assertTrue("Check content of generated pairs", sameRankings);
	}
}
