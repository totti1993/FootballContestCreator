package com.totti.footballcontestcreator.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "teams", indices = {@Index(value = "name", unique = true)})
public class Team {

	@ColumnInfo(name = "id")
	@PrimaryKey
	@NonNull
	private String id;

	@ColumnInfo(name = "name")
	private String name;

	@ColumnInfo(name = "trophies")
	private int trophies;

	@ColumnInfo(name = "all_time_wins")
	private int all_time_wins;

	@ColumnInfo(name = "all_time_draws")
	private int all_time_draws;

	@ColumnInfo(name = "all_time_losses")
	private int all_time_losses;

	@ColumnInfo(name = "comments")
	private String comments;

	@ColumnInfo(name = "favorite")
	private boolean favorite;

	@ColumnInfo(name = "selected")
	private boolean selected;

	@Ignore
	public Team() {
		// Default constructor required for DataSnapshot.getValue(Team.class)
	}

	public Team(@NonNull String id, String name, String comments) {
		this.id = id;
		this.name = name;
		this.trophies = 0;
		this.all_time_wins = 0;
		this.all_time_draws = 0;
		this.all_time_losses = 0;
		this.comments = comments;
		this.favorite = false;
		this.selected = false;
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

	public boolean getFavorite() {
		return this.favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public boolean getSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
