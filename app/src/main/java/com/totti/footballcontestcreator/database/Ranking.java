package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "rankings",
		foreignKeys = {@ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "team_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Ranking {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	private Integer id;

	@ColumnInfo(name = "tournament_id")
	@NonNull
	private Integer tournament_id;

	@ColumnInfo(name = "team_id")
	@NonNull
	private Integer team_id;

	@ColumnInfo(name = "place")
	@NonNull
	private Integer place;

	@ColumnInfo(name = "points")
	@NonNull
	private Integer points;

	@ColumnInfo(name = "wins")
	@NonNull
	private Integer wins;

	@ColumnInfo(name = "draws")
	@NonNull
	private Integer draws;

	@ColumnInfo(name = "losses")
	@NonNull
	private Integer losses;

	@ColumnInfo(name = "goals_for")
	@NonNull
	private Integer goals_for;

	@ColumnInfo(name = "goals_against")
	@NonNull
	private Integer goals_against;

	@ColumnInfo(name = "comments")
	private String comments;

	public Ranking(@NonNull Integer tournament_id, @NonNull Integer team_id, @NonNull Integer place, String comments) {
		this.tournament_id = tournament_id;
		this.team_id = team_id;
		this.place = place;
		this.points = 0;
		this.wins = 0;
		this.draws = 0;
		this.losses = 0;
		this.goals_for = 0;
		this.goals_against = 0;
		this.comments = comments;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NonNull
	public Integer getTournament_id() {
		return this.tournament_id;
	}

	public void setTournament_id(@NonNull Integer tournament_id) {
		this.tournament_id = tournament_id;
	}

	@NonNull
	public Integer getTeam_id() {
		return this.team_id;
	}

	public void setTeam_id(@NonNull Integer team_id) {
		this.team_id = team_id;
	}

	@NonNull
	public Integer getPlace() {
		return this.place;
	}

	public void setPlace(@NonNull Integer place) {
		this.place = place;
	}

	@NonNull
	public Integer getPoints() {
		return this.points;
	}

	public void setPoints(@NonNull Integer points) {
		this.points = points;
	}

	@NonNull
	public Integer getWins() {
		return this.wins;
	}

	public void setWins(@NonNull Integer wins) {
		this.wins = wins;
	}

	@NonNull
	public Integer getDraws() {
		return this.draws;
	}

	public void setDraws(@NonNull Integer draws) {
		this.draws = draws;
	}

	@NonNull
	public Integer getLosses() {
		return this.losses;
	}

	public void setLosses(@NonNull Integer losses) {
		this.losses = losses;
	}

	@NonNull
	public Integer getGoals_for() {
		return this.goals_for;
	}

	public void setGoals_for(@NonNull Integer goals_for) {
		this.goals_for = goals_for;
	}

	@NonNull
	public Integer getGoals_against() {
		return this.goals_against;
	}

	public void setGoals_against(@NonNull Integer goals_against) {
		this.goals_against = goals_against;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
