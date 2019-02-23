package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

@Entity(tableName = "tournament"/*, indices = {@Index(value = "name", unique = true)}*/)
public class Tournament {

	public enum Type {
		CHAMPIONSHIP,
		ELIMINATION;

		@TypeConverter
		public static Type toType(int ordinal) {
			Type returnType = null;

			for(Type type : Type.values()) {
				if(type.ordinal() == ordinal) {
					returnType = type;
					break;
				}
			}

			return returnType;
		}

		@TypeConverter
		public static int toInt(Type type) {
			return type.ordinal();
		}
	}

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	private Long id;

	@ColumnInfo(name = "name")
	private String name;

	@ColumnInfo(name = "type")
	private Type type;

	@ColumnInfo(name = "teams")
	private Integer teams;

	@ColumnInfo(name = "rounds")
	private Integer rounds;

	@ColumnInfo(name = "comments")
	private String comments;

	public Tournament(String name, Type type, /*Integer teams,*/ Integer rounds, String comments) {
		this.name = name;
		this.type = type;
		//this.teams = teams;
		this.rounds = rounds;
		this.comments = comments;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getTeams() {
		return this.teams;
	}

	public void setTeams(Integer teams) {
		this.teams = teams;
	}

	public Integer getRounds() {
		return this.rounds;
	}

	public void setRounds(Integer rounds) {
		this.rounds = rounds;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
