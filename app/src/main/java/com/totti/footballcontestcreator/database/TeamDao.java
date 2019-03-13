package com.totti.footballcontestcreator.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TeamDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Team team);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Team team);

	@Delete
	void delete(Team team);

	@Query("SELECT * FROM team WHERE id = :id")
	Team findTeamByKey(Integer id);

	@Query("SELECT * FROM team WHERE name = :name")
	Team findTeamByName(String name);

	@Query("SELECT * FROM team ORDER BY name ASC")
	LiveData<List<Team>> findAllTeams();
}