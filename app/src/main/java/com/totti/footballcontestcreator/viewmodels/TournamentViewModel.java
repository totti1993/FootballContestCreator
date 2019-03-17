package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.List;

public class TournamentViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public TournamentViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
	}

	public LiveData<List<Tournament>> getAllTournaments() {
		return appDatabase.tournamentDao().findAllTournaments();
	}

	public void insert(final Tournament tournament) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.tournamentDao().insert(tournament);
				return null;
			}
		}.execute();
	}

	public void update(final Tournament tournament) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.tournamentDao().update(tournament);
				return null;
			}
		}.execute();
	}

	public void delete(final Tournament tournament) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.tournamentDao().delete(tournament);
				return null;
			}
		}.execute();
	}
}
