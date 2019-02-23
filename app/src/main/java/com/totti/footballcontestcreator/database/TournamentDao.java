package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TournamentDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	long insert(Tournament tournament);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Tournament tournament);

	@Delete
	void delete(Tournament tournament);

	@Query("SELECT * FROM tournament WHERE id = :id")
	Tournament findTournamentByKey(Integer id);

	@Query("SELECT * FROM tournament WHERE name = :name")
	Tournament findTournamentByName(String name);

	@Query("SELECT * FROM tournament ORDER BY name ASC")
	List<Tournament> getAllTournaments();
}
