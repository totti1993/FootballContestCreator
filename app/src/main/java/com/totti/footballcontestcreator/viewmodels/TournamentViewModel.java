package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.List;

public class TournamentViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;
	private LiveData<List<Tournament>> tournamentsOrdered;

	public TournamentViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
		tournamentsOrdered = appDatabase.tournamentDao().findAllTournamentsOrdered();
	}

	public long insert(Tournament tournament) {
		return appDatabase.tournamentDao().insert(tournament);
	}

	public void update(Tournament tournament) {
		appDatabase.tournamentDao().update(tournament);
	}

	public void delete(Tournament tournament) {
		appDatabase.tournamentDao().delete(tournament);
	}

	public LiveData<List<Tournament>> getAllTournamentsOrdered() {
		return tournamentsOrdered;
	}
}
