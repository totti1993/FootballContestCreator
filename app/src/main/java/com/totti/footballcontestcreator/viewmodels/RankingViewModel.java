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
import com.totti.footballcontestcreator.database.Ranking;

import java.util.List;

public class RankingViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;

	public RankingViewModel(Application application) {
		super(application);

		appDatabase = AppDatabase.getDatabase(application);

		DatabaseReference onlineRankings = FirebaseDatabase.getInstance().getReference("rankings");
		onlineRankings.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot ranking, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.rankingDao().insert(ranking.getValue(Ranking.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildChanged(@NonNull final DataSnapshot ranking, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.rankingDao().update(ranking.getValue(Ranking.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildRemoved(@NonNull final DataSnapshot ranking) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.rankingDao().delete(ranking.getValue(Ranking.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot ranking, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	public void insertAll(List<Ranking> rankings) {
		appDatabase.rankingDao().insertAll(rankings);
	}

	public void deleteAll() {
		appDatabase.rankingDao().deleteAll();
	}

	public LiveData<List<Ranking>> getAllRankingsByTournamentOrdered(String tournament_id) {
		return appDatabase.rankingDao().findAllRankingsByTournamentOrdered(tournament_id);
	}

	public List<Ranking> getAllActiveRankingsByTournamentAsync(String tournament_id) {
		return appDatabase.rankingDao().findAllActiveRankingsByTournamentAsync(tournament_id);
	}

	public Ranking getRankingByTournamentAndTeamAsync(String tournament_id, String team_id) {
		return appDatabase.rankingDao().findRankingByTournamentAndTeamAsync(tournament_id, team_id);
	}

	public LiveData<List<Ranking>> getAllRankingsByTeam(String team_id) {
		return appDatabase.rankingDao().findAllRankingsByTeam(team_id);
	}

	public List<Ranking> getAllRankingsByTeamAsync(String team_id) {
		return appDatabase.rankingDao().findAllRankingsByTeamAsync(team_id);
	}

	public LiveData<List<Ranking>> getAllRankingsByTournament(String tournament_id) {
		return appDatabase.rankingDao().findAllRankingsByTournament(tournament_id);
	}

	public List<Ranking> getAllRankingsByTournamentAsync(String tournament_id) {
		return appDatabase.rankingDao().findAllRankingsByTournamentAsync(tournament_id);
	}

	public Ranking getRankingById(String id) {
		return appDatabase.rankingDao().findRankingById(id);
	}
}
