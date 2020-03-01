package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rankings",
		foreignKeys = {@ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "team_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Ranking {

	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	@ColumnInfo(name = "tournament_id")
	private String tournament_id;

	@ColumnInfo(name = "tournament_name")
	private String tournament_name;

	@ColumnInfo(name = "team_id")
	private String team_id;

	@ColumnInfo(name = "team_name")
	private String team_name;

	@ColumnInfo(name = "place")
	private int place;

	@ColumnInfo(name = "points")
	private int points;

	@ColumnInfo(name = "wins")
	private int wins;

	@ColumnInfo(name = "draws")
	private int draws;

	@ColumnInfo(name = "losses")
	private int losses;

	@ColumnInfo(name = "goals_for")
	private int goals_for;

	@ColumnInfo(name = "goals_against")
	private int goals_against;

	@ColumnInfo(name = "goal_difference")
	private int goal_difference;

	@ColumnInfo(name = "comments")
	private String comments;

	@ColumnInfo(name = "active")
	private boolean active;

	@Ignore
	public Ranking() {
		// Default constructor required for DataSnapshot.getValue(Ranking.class)
	}

	public Ranking(@NonNull String id, String tournament_id, String tournament_name, String team_id, String team_name, int place) {
		this.id = id;
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
