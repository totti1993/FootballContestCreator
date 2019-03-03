package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tournament", indices = {@Index(value = "name", unique = true)})
public class Tournament {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	@NonNull
	private Long id;

	@ColumnInfo(name = "name")
	@NonNull
	private String name;

	@ColumnInfo(name = "type")
	@NonNull
	private String type;

	/*@ColumnInfo(name = "teams")
	@NonNull
	private Integer teams;*/

	@ColumnInfo(name = "rounds")
	@NonNull
	private Integer rounds;

	@ColumnInfo(name = "comments")
	private String comments;

	public Tournament(@NonNull String name, @NonNull String type, /*@NonNull Integer teams,*/ @NonNull Integer rounds, String comments) {
		this.name = name;
		this.type = type;
		//this.teams = teams;
		this.rounds = rounds;
		this.comments = comments;
	}

	@NonNull
	public Long getId() {
		return this.id;
	}

	public void setId(@NonNull Long id) {
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

	/*
	@NonNull
	public Integer getTeams() {
		return this.teams;
	}

	public void setTeams(@NonNull Integer teams) {
		this.teams = teams;
	}*/

	@NonNull
	public Integer getRounds() {
		return this.rounds;
	}

	public void setRounds(@NonNull Integer rounds) {
		this.rounds = rounds;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
