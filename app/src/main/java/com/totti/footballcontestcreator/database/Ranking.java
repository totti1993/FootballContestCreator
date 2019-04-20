package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "rankings",
		foreignKeys = {@ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "team_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Ranking {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	private long id;

	@ColumnInfo(name = "tournament_id")
	private long tournament_id;

	@ColumnInfo(name = "team_id")
	private long team_id;

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

	@ColumnInfo(name = "comments")
	private String comments;

	@ColumnInfo(name = "active")
	private boolean active;

	public Ranking(long tournament_id, long team_id, int place) {
		this.tournament_id = tournament_id;
		this.team_id = team_id;
		this.place = place;
		this.points = 0;
		this.wins = 0;
		this.draws = 0;
		this.losses = 0;
		this.goals_for = 0;
		this.goals_against = 0;
		this.active = true;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTournament_id() {
		return this.tournament_id;
	}

	public void setTournament_id(long tournament_id) {
		this.tournament_id = tournament_id;
	}

	public long getTeam_id() {
		return this.team_id;
	}

	public void setTeam_id(long team_id) {
		this.team_id = team_id;
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
