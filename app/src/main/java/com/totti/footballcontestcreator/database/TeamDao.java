package com.totti.footballcontestcreator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeamDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Team team);

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insertAll(List<Team> teams);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Team team);

	@Delete
	void delete(Team team);

	@Query("DELETE FROM teams")
	void deleteAll();

	// Observed queries

	@Query("SELECT * FROM teams ORDER BY name ASC")
	LiveData<List<Team>> findAllTeams();

	@Query("SELECT * FROM teams WHERE id LIKE :id LIMIT 1")
	LiveData<Team> findTeamById(String id);

	@Query("SELECT comments FROM teams WHERE id LIKE :id LIMIT 1")
	LiveData<String> findCommentsById(String id);

	// Async queries

	@Query("SELECT * FROM teams ORDER BY name ASC")
	List<Team> findAllTeamsAsync();

	@Query("SELECT * FROM teams WHERE id LIKE :id LIMIT 1")
	Team findTeamByIdAsync(String id);

	@Query("SELECT creator FROM teams WHERE id LIKE :id LIMIT 1")
	String findCreatorByIdAsync(String id);
}
