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

	@Query("SELECT * FROM teams ORDER BY name ASC")
	LiveData<List<Team>> findAllTeams();

	@Query("SELECT * FROM teams ORDER BY favorite DESC, name ASC")
	LiveData<List<Team>> findAllTeamsOrdered();

	@Query("SELECT * FROM teams WHERE id = :id LIMIT 1")
	Team findTeamById(long id);
}
