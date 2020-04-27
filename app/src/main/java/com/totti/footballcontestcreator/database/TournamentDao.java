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
public interface TournamentDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Tournament tournament);

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insertAll(List<Tournament> tournaments);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Tournament tournament);

	@Delete
	void delete(Tournament tournament);

	@Query("DELETE FROM tournaments")
	void deleteAll();

	// Observed queries

	@Query("SELECT * FROM tournaments ORDER BY name ASC")
	LiveData<List<Tournament>> findAllTournaments();

	@Query("SELECT * FROM tournaments WHERE id LIKE :id LIMIT 1")
	LiveData<Tournament> findTournamentById(String id);

	@Query("SELECT comments FROM tournaments WHERE id LIKE :id LIMIT 1")
	LiveData<String> findCommentsById(String id);

	// Async queries

	@Query("SELECT * FROM tournaments ORDER BY name ASC")
	List<Tournament> findAllTournamentsAsync();

	@Query("SELECT * FROM tournaments WHERE id LIKE :id LIMIT 1")
	Tournament findTournamentByIdAsync(String id);

	@Query("SELECT creator FROM tournaments WHERE id LIKE :id LIMIT 1")
	String findCreatorByIdAsync(String id);
}
