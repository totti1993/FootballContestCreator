package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rankings")
	 /* foreignKeys = {@ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "team_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)} */
public class Ranking {

	// ID of the ranking
	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	// Creator of the ranking (email address)
	@ColumnInfo(name = "creator")
	private String creator;

	// ID of the tournament that contains the ranking
	@ColumnInfo(name = "tournament_id")
	private String tournament_id;

	// Name of the tournament that contains the ranking
	@ColumnInfo(name = "tournament_name")
	private String tournament_name;

	// ID of the team that has the ranking
	@ColumnInfo(name = "team_id")
	private String team_id;

	// Name of the team that has the ranking
	@ColumnInfo(name = "team_name")
	private String team_name;

	// Current place of the team in the tournament
	@ColumnInfo(name = "place")
	private int place;

	// Number of points of the team in the tournament
	@ColumnInfo(name = "points")
	private int points;

	// Number of wins of the team in the tournament
	@ColumnInfo(name = "wins")
	private int wins;

	// Number of draws of the team in the tournament
	@ColumnInfo(name = "draws")
	private int draws;

	// Number of losses of the team in the tournament
	@ColumnInfo(name = "losses")
	private int losses;

	// Number of goals scored by the team in the tournament
	@ColumnInfo(name = "goals_for")
	private int goals_for;

	// Number of goals conceded by the team in the tournament
	@ColumnInfo(name = "goals_against")
	private int goals_against;

	// Difference of the scored goals and the conceded goals
	@ColumnInfo(name = "goal_difference")
	private int goal_difference;

	// Comments about the ranking
	@ColumnInfo(name = "comments")
	private String comments;

	// Indicates whether the team is eliminated or not
	@ColumnInfo(name = "active")
	private boolean active;

	@Ignore
	public Ranking() {
		// Default constructor required for DataSnapshot.getValue(Ranking.class)
	}

	public Ranking(@NonNull String id, String creator, String tournament_id, String tournament_name, String team_id, String team_name, int place) {
		this.id = id;
		this.creator = creator;
		this.tournament_id = tournament_id;
		this.tournament_name = tournament_name;
		this.team_id = team_id;
		this.team_name = team_name;
		this.place = place;
		this.points = 0;
		this.wins = 0;
		this.draws = 0;
		this.losses = 0;
		this.goals_for = 0;
		this.goals_against = 0;
		this.goal_difference = 0;
		this.active = true;
	}

	@NonNull
	public String getId() {
		return this.id;
	}

	public void setId(@NonNull String id) {
		this.id = id;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getTournament_id() {
		return this.tournament_id;
	}

	public void setTournament_id(String tournament_id) {
		this.tournament_id = tournament_id;
	}

	public String getTournament_name() {
		return this.tournament_name;
	}

	public void setTournament_name(String tournament_name) {
		this.tournament_name = tournament_name;
	}

	public String getTeam_id() {
		return this.team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

	public String getTeam_name() {
		return this.team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public int getPlace() {
		return this.place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getWins() {
		return this.wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getDraws() {
		return this.draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public int getLosses() {
		return this.losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getGoals_for() {
		return this.goals_for;
	}

	public void setGoals_for(int goals_for) {
		this.goals_for = goals_for;
	}

	public int getGoals_against() {
		return this.goals_against;
	}

	public void setGoals_against(int goals_against) {
		this.goals_against = goals_against;
	}

	public int getGoal_difference() {
		return this.goal_difference;
	}

	public void setGoal_difference(int goal_difference) {
		this.goal_difference = goal_difference;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
