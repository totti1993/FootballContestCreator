package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tournaments", indices = {@Index(value = "name", unique = true)})
public class Tournament {

	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	@ColumnInfo(name = "name")
	private String name;

	@ColumnInfo(name = "type")
	private String type;

	@ColumnInfo(name = "rounds")
	private int rounds;

	@ColumnInfo(name = "teams")
	private int teams;

	@ColumnInfo(name = "comments")
	private String comments;

	@ColumnInfo(name = "favorite")
	private boolean favorite;

	@Ignore
	public Tournament() {
		// Default constructor required for DataSnapshot.getValue(Tournament.class)
	}

	public Tournament(@NonNull String id, String name, String type, int rounds, int teams, String comments) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.rounds = rounds;
		this.teams = teams;
		this.comments = comments;
		this.favorite = false;
	}

	@NonNull
	public String getId() {
		return this.id;
	}

	public void setId(@NonNull String id) {
		this.id = id;
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

	public boolean getFavorite() {
		return this.favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
}
