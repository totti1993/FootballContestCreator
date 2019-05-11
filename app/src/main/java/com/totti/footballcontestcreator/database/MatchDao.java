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
public interface MatchDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Match match);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Match match);

	@Delete
	void delete(Match match);

	@Query("SELECT * FROM matches WHERE (home_id = :team_id OR visitor_id = :team_id) AND final_score = :final_score")
	LiveData<List<Match>> findAllMatchesByTeamAndFinalScore(long team_id, boolean final_score);

	@Query("SELECT * FROM matches WHERE tournament_id = :tournament_id AND final_score = :final_score")
	LiveData<List<Match>> findAllMatchesByTournamentAndFinalScore(long tournament_id, boolean final_score);

	@Query("SELECT * FROM matches WHERE id = :id LIMIT 1")
	Match findMatchById(long id);
}
