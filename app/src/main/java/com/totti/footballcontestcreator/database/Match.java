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
	private long id;

	@ColumnInfo(name = "tournament_id")
	private long tournament_id;

	@ColumnInfo(name = "tournament_name")
	@NonNull
	private String tournament_name;

	@ColumnInfo(name = "match_day")
	private int match_day;

	@ColumnInfo(name = "home_id")
	private long home_id;

	@ColumnInfo(name = "home_name")
	@NonNull
	private String home_name;

	@ColumnInfo(name = "home_score")
	private int home_score;

	@ColumnInfo(name = "visitor_id")
	private long visitor_id;

	@ColumnInfo(name = "visitor_name")
	@NonNull
	private String visitor_name;

	@ColumnInfo(name = "visitor_score")
	private int visitor_score;

	@ColumnInfo(name = "comments")
	private String comments;

	@ColumnInfo(name = "final_score")
	private boolean final_score;

	public Match(long tournament_id, @NonNull String tournament_name, int match_day, long home_id, @NonNull String home_name, long visitor_id, @NonNull String visitor_name) {
		this.tournament_id = tournament_id;
		this.tournament_name = tournament_name;
		this.match_day = match_day;
		this.home_id = home_id;
		this.home_name = home_name;
		this.home_score = 0;
		this.visitor_id = visitor_id;
		this.visitor_name = visitor_name;
		this.visitor_score = 0;
		this.final_score = false;
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

	@NonNull
	public String getTournament_name() {
		return this.tournament_name;
	}

	public void setTournament_name(@NonNull String tournament_name) {
		this.tournament_name = tournament_name;
	}

	public int getMatch_day() {
		return this.match_day;
	}

	public void setMatch_day(int match_day) {
		this.match_day = match_day;
	}

	public long getHome_id() {
		return this.home_id;
	}

	public void setHome_id(long home_id) {
		this.home_id = home_id;
	}

	@NonNull
	public String getHome_name() {
		return this.home_name;
	}

	public void setHome_name(@NonNull String home_name) {
		this.home_name = home_name;
	}

	public int getHome_score() {
		return this.home_score;
	}

	public void setHome_score(int home_score) {
		this.home_score = home_score;
	}

	public long getVisitor_id() {
		return this.visitor_id;
	}

	public void setVisitor_id(long visitor_id) {
		this.visitor_id = visitor_id;
	}

	@NonNull
	public String getVisitor_name() {
		return this.visitor_name;
	}

	public void setVisitor_name(@NonNull String visitor_name) {
		this.visitor_name = visitor_name;
	}

	public int getVisitor_score() {
		return this.visitor_score;
	}

	public void setVisitor_score(int visitor_score) {
		this.visitor_score = visitor_score;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean getFinal_score() {
		return this.final_score;
	}

	public void setFinal_score(boolean final_score) {
		this.final_score = final_score;
	}
}
