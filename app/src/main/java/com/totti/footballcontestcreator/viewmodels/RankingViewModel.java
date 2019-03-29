package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Ranking;

import java.util.List;

public class RankingViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public RankingViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
	}

	public LiveData<List<Ranking>> getAllRankingsByTeam(Integer team_id) {
		return appDatabase.rankingDao().findAllRankingsByTeam(team_id);
	}

	public LiveData<List<Ranking>> getAllRankingsByTournament(Integer tournament_id) {
		return appDatabase.rankingDao().findAllRankingsByTournament(tournament_id);
	}

	public void insert(final Ranking ranking) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.rankingDao().insert(ranking);
				return null;
			}
		}.execute();
	}

	public void update(final Ranking ranking) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.rankingDao().update(ranking);
				return null;
			}
		}.execute();
	}

	public void delete(final Ranking ranking) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.rankingDao().delete(ranking);
				return null;
			}
		}.execute();
	}
}
