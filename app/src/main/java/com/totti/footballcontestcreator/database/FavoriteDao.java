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
public interface FavoriteDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Favorite favorite);

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insertAll(List<Favorite> favorites);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Favorite favorite);

	@Delete
	void delete(Favorite favorite);

	@Query("DELETE FROM favorites")
	void deleteAll();

	// Observed queries

	@Query("SELECT favorite_team_id FROM favorites WHERE favorite_type LIKE 'team'")
	LiveData<List<String>> findAllFavoriteTeams();

	@Query("SELECT favorite_tournament_id FROM favorites WHERE favorite_type LIKE 'tournament'")
	LiveData<List<String>> findAllFavoriteTournaments();

	// Async queries

	@Query("SELECT favorite_team_id FROM favorites WHERE favorite_type LIKE 'team'")
	List<String> findAllFavoriteTeamsAsync();

	@Query("SELECT favorite_tournament_id FROM favorites WHERE favorite_type LIKE 'tournament'")
	List<String> findAllFavoriteTournamentsAsync();
}
