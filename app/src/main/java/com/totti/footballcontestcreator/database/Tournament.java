package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tournaments", indices = {@Index(value = "name", unique = true)})
public class Tournament {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	private long id;

	@ColumnInfo(name = "name")
	@NonNull
	private String name;

	@ColumnInfo(name = "type")
	@NonNull
	private String type;

	@ColumnInfo(name = "rounds")
	private int rounds;

	@ColumnInfo(name = "teams")
	private int teams;

	@ColumnInfo(name = "comments")
	private String comments;

	@ColumnInfo(name = "favorite")
	private boolean favorite;

	public Tournament(@NonNull String name, @NonNull String type, int rounds, int teams, String comments) {
		this.name = name;
		this.type = type;
		this.rounds = rounds;
		this.teams = teams;
		this.comments = comments;
		this.favorite = false;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NonNull
	public String getName() {
		return this.name;
	}

	public void setName(@NonNull String name) {
		this.name = name;
	}

	@NonNull
	public String getType() {
		return this.type;
	}

	public void setType(@NonNull String type) {
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
