package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.database.AppDatabase;
import com.totti.footballcontestcreator.database.Match;

import java.util.List;

public class MatchViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;                // Room database
	private DatabaseReference onlineMatches;        // Reference to Firebase "matches" node
	private ChildEventListener childEventListener;  // Listener to modify "matches" table in Room database

	private LiveData<List<Match>> selectedMatches;  // LiveData required for the match filter

	public MatchViewModel(Application application) {
		super(application);

		selectedMatches = null;

		appDatabase = AppDatabase.getDatabase(application);
		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");
		childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot match, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.matchDao().insert(match.getValue(Match.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildChanged(@NonNull final DataSnapshot match, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.matchDao().update(match.getValue(Match.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildRemoved(@NonNull final DataSnapshot match) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.matchDao().delete(match.getValue(Match.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot match, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};
		addListenerToOnlineDatabase();
	}

	// Add listener to "matches" node in Firebase
	public void addListenerToOnlineDatabase() {
		onlineMatches.addChildEventListener(childEventListener);
	}

	// Remove listener from "matches" node in Firebase
	public void removeListenerFromOnlineDatabase() {
		onlineMatches.removeEventListener(childEventListener);
	}

	public void insertAll(List<Match> matches) {
		appDatabase.matchDao().insertAll(matches);
	}

	public void deleteAll() {
		appDatabase.matchDao().deleteAll();
	}

	// Observed queries

	public LiveData<List<Match>> getAllMatchesByTeamTournamentAndFinalScore(String team_id, String tournament_id, boolean final_score) {
		if(team_id != null) {
			if(tournament_id != null) {
				selectedMatches = appDatabase.matchDao().findAllMatchesByTeamTournamentAndFinalScore(team_id, tournament_id, final_score);
			}
			else {
				selectedMatches = appDatabase.matchDao().findAllMatchesByTeamAndFinalScore(team_id, final_score);
			}
		}
		else{
			if(tournament_id != null) {
				selectedMatches = appDatabase.matchDao().findAllMatchesByTournamentAndFinalScore(tournament_id, final_score);
			}
			else {
				return selectedMatches;
			}
		}
		return selectedMatches;
	}

	// Async queries

	public List<Match> getAllMatchesByTeamAsync(String team_id) {
		return appDatabase.matchDao().findAllMatchesByTeamAsync(team_id);
	}

	public List<Match> getAllMatchesByTournamentAsync(String tournament_id) {
		return appDatabase.matchDao().findAllMatchesByTournamentAsync(tournament_id);
	}

	public List<Match> getAllMatchesByTournamentAndFinalScoreAsync(String tournament_id, boolean final_score) {
		return appDatabase.matchDao().findAllMatchesByTournamentAndFinalScoreAsync(tournament_id, final_score);
	}

	public Match getMatchByIdAsync(String id) {
		return appDatabase.matchDao().findMatchByIdAsync(id);
	}

	public Match getMatchByTournamentAndTeamsInEliminationAsync(String tournament_id, String home_id, String visitor_id) {
		return appDatabase.matchDao().findMatchByTournamentAndTeamsInEliminationAsync(tournament_id, home_id, visitor_id);
	}
}
