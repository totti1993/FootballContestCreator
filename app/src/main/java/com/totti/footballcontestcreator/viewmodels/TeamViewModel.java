package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.totti.footballcontestcreator.Repository;
import com.totti.footballcontestcreator.database.Team;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {

	private Repository repository;

	private LiveData<List<Team>> teams;

	public TeamViewModel(Application application) {
		super(application);
		repository = new Repository(application);
		teams = repository.getAllTeams();
	}

	public LiveData<List<Team>> getAllTeams() {
		return teams;
	}

	public void insert(Team team) {
		repository.insert(team);
	}

	public void update(Team team) {
		repository.update(team);
	}

	public void delete(Team team) {
		repository.delete(team);
	}
}
