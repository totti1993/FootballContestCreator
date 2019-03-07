package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "team", indices = {@Index(value = "name", unique = true)})
public class Team {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private Integer id;

	@ColumnInfo(name = "name")
	@NonNull
	private String name;

	@ColumnInfo(name = "trophies")
	@NonNull
	private Integer trophies;

	@ColumnInfo(name = "all_time_wins")
	@NonNull
	private Integer all_time_wins;

	@ColumnInfo(name = "all_time_draws")
	@NonNull
	private Integer all_time_draws;

	@ColumnInfo(name = "all_time_losses")
	@NonNull
	private Integer all_time_losses;

	@ColumnInfo(name = "comments")
	private String comments;

	public Team(@NonNull String name, String comments) {
		this.name = name;
		this.trophies = 0;
		this.all_time_wins = 0;
		this.all_time_draws = 0;
		this.all_time_losses = 0;
		this.comments = comments;
	}

	@NonNull
	public Integer getId() {
		return this.id;
	}

	public void setId(@NonNull Integer id) {
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
	public Integer getTrophies() {
		return this.trophies;
	}

	public void setTrophies(@NonNull Integer trophies) {
		this.trophies = trophies;
	}

	@NonNull
	public Integer getAll_time_wins() {
		return this.all_time_wins;
	}

	public void setAll_time_wins(@NonNull Integer all_time_wins) {
		this.all_time_wins = all_time_wins;
	}

	@NonNull
	public Integer getAll_time_draws() {
		return this.all_time_draws;
	}

	public void setAll_time_draws(@NonNull Integer all_time_draws) {
		this.all_time_draws = all_time_draws;
	}

	@NonNull
	public Integer getAll_time_losses() {
		return this.all_time_losses;
	}

	public void setAll_time_losses(@NonNull Integer all_time_losses) {
		this.all_time_losses = all_time_losses;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
