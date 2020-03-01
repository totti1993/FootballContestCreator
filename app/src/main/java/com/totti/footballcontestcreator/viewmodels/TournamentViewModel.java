package com.totti.footballcontestcreator.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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
import com.totti.footballcontestcreator.database.Tournament;

import java.util.List;

public class TournamentViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;
	private DatabaseReference onlineTournaments;

	public TournamentViewModel(Application application) {
		super(application);

		appDatabase = AppDatabase.getDatabase(application);

		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");
		onlineTournaments.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot tournament, @Nullable String s) {
				Log.d("asd", "onChildAdded called on Tournament");
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.tournamentDao().insert(tournament.getValue(Tournament.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildChanged(@NonNull final DataSnapshot tournament, @Nullable String s) {
				Log.d("asd", "onChildChanged called on Tournament");
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.tournamentDao().update(tournament.getValue(Tournament.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildRemoved(@NonNull final DataSnapshot tournament) {
				Log.d("asd", "onChildRemoved called on Tournament");
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.tournamentDao().delete(tournament.getValue(Tournament.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot tournament, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	public void insertAll(List<Tournament> tournaments) {
		appDatabase.tournamentDao().insertAll(tournaments);
	}

	public void deleteAll() {
		appDatabase.tournamentDao().deleteAll();
	}

	public LiveData<List<Tournament>> getAllTournamentsOrdered() {
		return appDatabase.tournamentDao().findAllTournamentsOrdered();
	}

	public LiveData<Tournament> getTournamentById(String id) {
		return appDatabase.tournamentDao().findTournamentById(id);
	}

	public Tournament getTournamentByIdAsync(String id) {
		return appDatabase.tournamentDao().findTournamentByIdAsync(id);
	}

	public LiveData<String> getCommentsById(String id) {
		return appDatabase.tournamentDao().findCommentsById(id);
	}
}
