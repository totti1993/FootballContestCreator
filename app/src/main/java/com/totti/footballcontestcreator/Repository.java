package com.totti.footballcontestcreator;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.database.TournamentDao;

import java.util.List;

public class Repository {

	private TournamentDao tournamentDao;

	private LiveData<List<Tournament>> tournaments;

	public Repository(Application application) {
		AppDatabase appDatabase = AppDatabase.getDatabase(application);

		tournamentDao = appDatabase.tournamentDao();
		tournaments = tournamentDao.findAllTournaments();
	}

	public LiveData<List<Tournament>> getAllTournaments() {
		return tournaments;
	}

	public void insert(final Tournament tournament) {
		new AsyncTask<Void, Void, Tournament>() {
			@Override
			protected Tournament doInBackground(Void... voids) {
				tournamentDao.insert(tournament);
				return null;
			}
		}.execute();
	}

	public void update(final Tournament tournament) {
		new AsyncTask<Void, Void, Tournament>() {
			@Override
			protected Tournament doInBackground(Void... voids) {
				tournamentDao.update(tournament);
				return null;
			}
		}.execute();
	}

	public void delete(final Tournament tournament) {
		new AsyncTask<Void, Void, Tournament>() {
			@Override
			protected Tournament doInBackground(Void... voids) {
				tournamentDao.delete(tournament);
				return null;
			}
		}.execute();
	}
}
