package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Team;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public TeamViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
	}

	public LiveData<List<Team>> getAllTeams() {
		return appDatabase.teamDao().findAllTeams();
	}

	public LiveData<List<Team>> getAllTeamsOrdered() {
		return appDatabase.teamDao().findAllTeamsOrdered();
	}

	public void insert(final Team team) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.teamDao().insert(team);
				return null;
			}
		}.execute();
	}

	public void update(final Team team) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.teamDao().update(team);
				return null;
			}
		}.execute();
	}

	public void delete(final Team team) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				appDatabase.teamDao().delete(team);
				return null;
			}
		}.execute();
	}
}
