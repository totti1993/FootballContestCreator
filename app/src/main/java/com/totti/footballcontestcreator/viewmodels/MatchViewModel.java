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

	public LiveData<List<Match>> getAllMatchesByTeamAndFinalScore(long team_id, boolean final_score) {
		return appDatabase.matchDao().findAllMatchesByTeamAndFinalScore(team_id, final_score);
	}

	public LiveData<List<Match>> getAllMatchesByTournamentAndFinalScore(long tournament_id, boolean final_score) {
		return appDatabase.matchDao().findAllMatchesByTournamentAndFinalScore(tournament_id, final_score);
	}

	public Match getMatchById(long id) {
		return appDatabase.matchDao().findMatchById(id);
	}

	public List<Match> getAllMatchesByTournamentAndFinalScoreAsync(long tournament_id, boolean final_score) {
		return appDatabase.matchDao().findAllMatchesByTournamentAndFinalScoreAsync(tournament_id, final_score);
	}

	public Match getMatchByTournamentAndTeamsInElimination(long tournament_id, long home_id, long visitor_id) {
		return appDatabase.matchDao().findMatchByTournamentAndTeamsInElimination(tournament_id, home_id, visitor_id);
	}
}
