package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Match;

import java.util.List;

public class MatchViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public MatchViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
	}

	public void insert(Match match) {
		appDatabase.matchDao().insert(match);
	}

	public void update(Match match) {
		appDatabase.matchDao().update(match);
	}

	public void delete(Match match) {
		appDatabase.matchDao().delete(match);
	}

	public LiveData<List<Match>> getAllMatchesByTeam(int team_id) {
		return appDatabase.matchDao().findAllMatchesByTeam(team_id);
	}

	public LiveData<List<Match>> getAllMatchesByTournament(int tournament_id) {
		return appDatabase.matchDao().findAllMatchesByTournament(tournament_id);
	}
}
