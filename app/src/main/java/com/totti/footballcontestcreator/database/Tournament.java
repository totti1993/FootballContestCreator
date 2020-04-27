package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tournaments", indices = {@Index(value = "name", unique = true)})
public class Tournament {

	// ID of the tournament
	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	// Creator of the tournament (email address)
	@ColumnInfo(name = "creator")
	private String creator;

	// Name of the tournament
	@ColumnInfo(name = "name")
	private String name;

	// Type of the tournament: "Championship" or "Elimination"
	@ColumnInfo(name = "type")
	private String type;

	// Number of rounds of the tournament
	@ColumnInfo(name = "rounds")
	private int rounds;

	// Number of teams of the tournament
	@ColumnInfo(name = "teams")
	private int teams;

	// Comments about the tournament
	@ColumnInfo(name = "comments")
	private String comments;

	@Ignore
	public Tournament() {
		// Default constructor required for DataSnapshot.getValue(Tournament.class)
	}

	public Tournament(@NonNull String id, String creator, String name, String type, int rounds, int teams, String comments) {
		this.id = id;
		this.creator = creator;
		this.name = name;
		this.type = type;
		this.rounds = rounds;
		this.teams = teams;
		this.comments = comments;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRounds() {
		return this.rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public int getTeams() {
		return this.teams;
	}

	public void setTeams(int teams) {
		this.teams = teams;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
