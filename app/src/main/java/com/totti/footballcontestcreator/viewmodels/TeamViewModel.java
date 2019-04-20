package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Team;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;
	private LiveData<List<Team>> teams;
	private LiveData<List<Team>> teamsOrdered;

	public TeamViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
		teams = appDatabase.teamDao().findAllTeams();
		teamsOrdered = appDatabase.teamDao().findAllTeamsOrdered();
	}

	public void insert(Team team) {
		appDatabase.teamDao().insert(team);
	}

	public void update(Team team) {
		appDatabase.teamDao().update(team);
	}

	public void delete(Team team) {
		appDatabase.teamDao().delete(team);
	}

	public LiveData<List<Team>> getAllTeams() {
		return teams;
	}

	public LiveData<List<Team>> getAllTeamsOrdered() {
		return teamsOrdered;
	}
}
