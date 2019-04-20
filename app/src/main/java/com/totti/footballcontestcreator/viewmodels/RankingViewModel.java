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

	public LiveData<List<Ranking>> getAllRankingsByTeam(int team_id) {
		return appDatabase.rankingDao().findAllRankingsByTeam(team_id);
	}

	public LiveData<List<Ranking>> getAllRankingsByTournament(int tournament_id) {
		return appDatabase.rankingDao().findAllRankingsByTournament(tournament_id);
	}
}
