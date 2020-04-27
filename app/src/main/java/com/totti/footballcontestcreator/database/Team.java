package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "teams", indices = {@Index(value = "name", unique = true)})
public class Team {

	// ID of the team
	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	// Creator of the team (email address)
	@ColumnInfo(name = "creator")
	private String creator;

	// Name of the team
	@ColumnInfo(name = "name")
	private String name;

	// Number of trophies of the team
	@ColumnInfo(name = "trophies")
	private int trophies;

	// Number of all-time wins of the team
	@ColumnInfo(name = "all_time_wins")
	private int all_time_wins;

	// Number of all-time draws of the team
	@ColumnInfo(name = "all_time_draws")
	private int all_time_draws;

	// Number of all-time losses of the team
	@ColumnInfo(name = "all_time_losses")
	private int all_time_losses;

	// Comments about the team
	@ColumnInfo(name = "comments")
	private String comments;

	@Ignore
	public Team() {
		// Default constructor required for DataSnapshot.getValue(Team.class)
	}

	public Team(@NonNull String id, String creator, String name, String comments) {
		this.id = id;
		this.creator = creator;
		this.name = name;
		this.trophies = 0;
		this.all_time_wins = 0;
		this.all_time_draws = 0;
		this.all_time_losses = 0;
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

	public int getTrophies() {
		return this.trophies;
	}

	public void setTrophies(int trophies) {
		this.trophies = trophies;
	}

	public int getAll_time_wins() {
		return this.all_time_wins;
	}

	public void setAll_time_wins(int all_time_wins) {
		this.all_time_wins = all_time_wins;
	}

	public int getAll_time_draws() {
		return this.all_time_draws;
	}

	public void setAll_time_draws(int all_time_draws) {
		this.all_time_draws = all_time_draws;
	}

	public int getAll_time_losses() {
		return this.all_time_losses;
	}

	public void setAll_time_losses(int all_time_losses) {
		this.all_time_losses = all_time_losses;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
