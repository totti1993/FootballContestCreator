package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Match;

import java.util.List;

public class MatchViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public MatchViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
	}

	public LiveData<List<Match>> getAllMatchesByTeam(Integer team_id) {
		return appDatabase.matchDao().findAllMatchesByTeam(team_id);
	}

	public LiveData<List<Match>> getAllMatchesByTournament(Integer tournament_id) {
		return appDatabase.matchDao().findAllMatchesByTournament(tournament_id);
	}

	public void insert(final Match match) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.matchDao().insert(match);
				return null;
			}
		}.execute();
	}

	public void update(final Match match) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.matchDao().update(match);
				return null;
			}
		}.execute();
	}

	public void delete(final Match match) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.matchDao().delete(match);
				return null;
			}
		}.execute();
	}
}
