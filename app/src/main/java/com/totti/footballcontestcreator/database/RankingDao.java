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
public interface RankingDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Ranking ranking);

	@Update(onConflict = OnConflictStrategy.IGNORE)
	void update(Ranking ranking);

	@Delete
	void delete(Ranking ranking);

	@Query("SELECT * FROM rankings WHERE team_id = :team_id")
	LiveData<List<Ranking>> findAllRankingsByTeam(long team_id);

	@Query("SELECT * FROM rankings WHERE tournament_id = :tournament_id ORDER BY active DESC, points DESC, goal_difference DESC, goals_for DESC, goals_against ASC, wins DESC, draws DESC, losses ASC, team_name ASC")
	LiveData<List<Ranking>> findAllRankingsByTournamentOrdered(long tournament_id);

	@Query("SELECT * FROM rankings WHERE id = :id LIMIT 1")
	Ranking findRankingById(long id);

	@Query("SELECT * FROM rankings WHERE tournament_id = :tournament_id AND team_id = :team_id LIMIT 1")
	Ranking findRankingByTournamentAndTeam(long tournament_id, long team_id);
}
