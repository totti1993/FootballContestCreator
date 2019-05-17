package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Ranking;

import java.util.List;

public class RankingViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public RankingViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
	}

	public void insert(Ranking ranking) {
		appDatabase.rankingDao().insert(ranking);
	}

	public void update(Ranking ranking) {
		appDatabase.rankingDao().update(ranking);
	}

	public void delete(Ranking ranking) {
		appDatabase.rankingDao().delete(ranking);
	}

	public LiveData<List<Ranking>> getAllRankingsByTeam(long team_id) {
		return appDatabase.rankingDao().findAllRankingsByTeam(team_id);
	}

	public LiveData<List<Ranking>> getAllRankingsByTournamentOrdered(long tournament_id) {
		return appDatabase.rankingDao().findAllRankingsByTournamentOrdered(tournament_id);
	}

	public Ranking getRankingById(long id) {
		return appDatabase.rankingDao().findRankingById(id);
	}

	public Ranking getRankingByTournamentAndTeam(long tournament_id, long team_id) {
		return appDatabase.rankingDao().findRankingByTournamentAndTeam(tournament_id, team_id);
	}

	public List<Ranking> getAllActiveRankingsByTournament(long tournament_id) {
		return appDatabase.rankingDao().findAllActiveRankingsByTournament(tournament_id);
	}
}
