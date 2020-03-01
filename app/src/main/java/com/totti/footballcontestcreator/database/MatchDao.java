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
public interface MatchDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Match match);

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insertAll(List<Match> matches);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Match match);

	@Delete
	void delete(Match match);

	@Query("DELETE FROM matches")
	void deleteAll();

	@Query("SELECT * FROM matches WHERE (home_id LIKE :team_id OR visitor_id LIKE :team_id) AND final_score = :final_score")
	LiveData<List<Match>> findAllMatchesByTeamAndFinalScore(String team_id, boolean final_score);

	@Query("SELECT * FROM matches WHERE tournament_id LIKE :tournament_id AND final_score = :final_score")
	LiveData<List<Match>> findAllMatchesByTournamentAndFinalScore(String tournament_id, boolean final_score);

	@Query("SELECT * FROM matches WHERE tournament_id LIKE :tournament_id AND final_score = :final_score")
	List<Match> findAllMatchesByTournamentAndFinalScoreAsync(String tournament_id, boolean final_score);

	@Query("SELECT * FROM matches WHERE tournament_id LIKE :tournament_id AND home_id LIKE :home_id AND visitor_id LIKE :visitor_id LIMIT 1")
	Match findMatchByTournamentAndTeamsInEliminationAsync(String tournament_id, String home_id, String visitor_id);

	@Query("SELECT * FROM matches WHERE id LIKE :id LIMIT 1")
	Match findMatchByIdAsync(String id);
}
