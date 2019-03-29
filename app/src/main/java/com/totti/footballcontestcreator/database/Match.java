package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "matches",
		foreignKeys = {@ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "home_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "visitor_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Match {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	private Integer id;

	@ColumnInfo(name = "tournament_id")
	@NonNull
	private Integer tournament_id;

	@ColumnInfo(name = "tournament_round")
	@NonNull
	private Integer tournament_round;

	@ColumnInfo(name = "home_id")
	@NonNull
	private Integer home_id;

	@ColumnInfo(name = "visitor_id")
	@NonNull
	private Integer visitor_id;

	@ColumnInfo(name = "home_score")
	private Integer home_score;

	@ColumnInfo(name = "visitor_score")
	private Integer visitor_score;

	@ColumnInfo(name = "comments")
	private String comments;

	public Match(@NonNull Integer tournament_id, @NonNull Integer tournament_round, @NonNull Integer home_id, @NonNull Integer visitor_id, String comments) {
		this.tournament_id = tournament_id;
		this.tournament_round = tournament_round;
		this.home_id = home_id;
		this.visitor_id = visitor_id;
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
	public Integer getTournament_round() {
		return this.tournament_round;
	}

	public void setTournament_round(@NonNull Integer tournament_round) {
		this.tournament_round = tournament_round;
	}

	@NonNull
	public Integer getHome_id() {
		return this.home_id;
	}

	public void setHome_id(@NonNull Integer home_id) {
		this.home_id = home_id;
	}

	@NonNull
	public Integer getVisitor_id() {
		return this.visitor_id;
	}

	public void setVisitor_id(@NonNull Integer visitor_id) {
		this.visitor_id = visitor_id;
	}

	public Integer getHome_score() {
		return this.home_score;
	}

	public void setHome_score(Integer home_score) {
		this.home_score = home_score;
	}

	public Integer getVisitor_score() {
		return this.visitor_score;
	}

	public void setVisitor_score(Integer visitor_score) {
		this.visitor_score = visitor_score;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
