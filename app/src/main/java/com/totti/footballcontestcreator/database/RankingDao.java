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
public interface RankingDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Ranking ranking);

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insertAll(List<Ranking> rankings);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Ranking ranking);

	@Delete
	void delete(Ranking ranking);

	@Query("DELETE FROM rankings")
	void deleteAll();

	@Query("SELECT * FROM rankings WHERE tournament_id LIKE :tournament_id ORDER BY active DESC, points DESC, goal_difference DESC, goals_for DESC, goals_against ASC, wins DESC, draws DESC, losses ASC, team_name ASC")
	LiveData<List<Ranking>> findAllRankingsByTournamentOrdered(String tournament_id);

	@Query("SELECT * FROM rankings WHERE tournament_id LIKE :tournament_id AND active = 1")
	List<Ranking> findAllActiveRankingsByTournamentAsync(String tournament_id);

	@Query("SELECT * FROM rankings WHERE tournament_id LIKE :tournament_id AND team_id LIKE :team_id LIMIT 1")
	Ranking findRankingByTournamentAndTeamAsync(String tournament_id, String team_id);

	@Query("SELECT * FROM rankings WHERE team_id LIKE :team_id")
	LiveData<List<Ranking>> findAllRankingsByTeam(String team_id);

	@Query("SELECT * FROM rankings WHERE id LIKE :id LIMIT 1")
	Ranking findRankingById(String id);
}
