package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.totti.footballcontestcreator.Repository;
import com.totti.footballcontestcreator.database.Tournament;

import java.util.List;

public class TournamentViewModel extends AndroidViewModel {

	private Repository repository;

	private LiveData<List<Tournament>> tournaments;

	public TournamentViewModel(Application application) {
		super(application);
		repository = new Repository(application);
		tournaments = repository.getAllTournaments();
	}

	public LiveData<List<Tournament>> getAllTournaments() {
		return tournaments;
	}

	public void insert(Tournament tournament) {
		repository.insert(tournament);
	}

	public void update(Tournament tournament) {
		repository.update(tournament);
	}

	public void delete(Tournament tournament) {
		repository.delete(tournament);
	}
}
