package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "matches")
	 /* foreignKeys = {@ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournament_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "home_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
					   @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "visitor_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)} */
public class Match {

	// ID of the match
	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	// Creator of the match (email address)
	@ColumnInfo(name = "creator")
	private String creator;

	// ID of the tournament that contains the match
	@ColumnInfo(name = "tournament_id")
	private String tournament_id;

	// Name of the tournament that contains the match
	@ColumnInfo(name = "tournament_name")
	private String tournament_name;

	// Day when the match is played
	@ColumnInfo(name = "match_day")
	private int match_day;

	// ID of the home team
	@ColumnInfo(name = "home_id")
	private String home_id;

	// Name of the home team
	@ColumnInfo(name = "home_name")
	private String home_name;

	// Score of the home team
	@ColumnInfo(name = "home_score")
	private int home_score;

	// ID of the visitor team
	@ColumnInfo(name = "visitor_id")
	private String visitor_id;

	// Name of the visitor team
	@ColumnInfo(name = "visitor_name")
	private String visitor_name;

	// Score of the visitor team
	@ColumnInfo(name = "visitor_score")
	private int visitor_score;

	// Comments about the match
	@ColumnInfo(name = "comments")
	private String comments;

	// Indicates whether the match is finished or not
	@ColumnInfo(name = "final_score")
	private boolean final_score;

	@Ignore
	public Match() {
		// Default constructor required for DataSnapshot.getValue(Match.class)
	}

	public Match(@NonNull String id, String creator, String tournament_id, String tournament_name, int match_day, String home_id, String home_name, String visitor_id, String visitor_name) {
		this.id = id;
		this.creator = creator;
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

	public int getMatch_day() {
		return this.match_day;
	}

	public void setMatch_day(int match_day) {
		this.match_day = match_day;
	}

	public String getHome_id() {
		return this.home_id;
	}

	public void setHome_id(String home_id) {
		this.home_id = home_id;
	}

	public String getHome_name() {
		return this.home_name;
	}

	public void setHome_name(String home_name) {
		this.home_name = home_name;
	}

	public int getHome_score() {
		return this.home_score;
	}

	public void setHome_score(int home_score) {
		this.home_score = home_score;
	}

	public String getVisitor_id() {
		return this.visitor_id;
	}

	public void setVisitor_id(String visitor_id) {
		this.visitor_id = visitor_id;
	}

	public String getVisitor_name() {
		return this.visitor_name;
	}

	public void setVisitor_name(String visitor_name) {
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
