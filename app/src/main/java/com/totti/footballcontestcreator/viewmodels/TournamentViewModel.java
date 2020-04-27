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
import com.totti.footballcontestcreator.database.Tournament;

import java.util.List;

public class TournamentViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;                // Room database
	private DatabaseReference onlineTournaments;    // Reference to Firebase "tournaments" node
	private ChildEventListener childEventListener;  // Listener to modify "tournaments" table in Room database

	public TournamentViewModel(Application application) {
		super(application);
		appDatabase = AppDatabase.getDatabase(application);
		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");
		childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot tournament, @Nullable String s) {
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
		};
		addListenerToOnlineDatabase();
	}

	// Add listener to "tournaments" node in Firebase
	public void addListenerToOnlineDatabase() {
		onlineTournaments.addChildEventListener(childEventListener);
	}

	// Remove listener from "tournaments" node in Firebase
	public void removeListenerFromOnlineDatabase() {
		onlineTournaments.removeEventListener(childEventListener);
	}

	public void insertAll(List<Tournament> tournaments) {
		appDatabase.tournamentDao().insertAll(tournaments);
	}

	public void deleteAll() {
		appDatabase.tournamentDao().deleteAll();
	}

	// Observed queries

	public LiveData<List<Tournament>> getAllTournaments() {
		return appDatabase.tournamentDao().findAllTournaments();
	}

	public LiveData<Tournament> getTournamentById(String id) {
		return appDatabase.tournamentDao().findTournamentById(id);
	}

	public LiveData<String> getCommentsById(String id) {
		return appDatabase.tournamentDao().findCommentsById(id);
	}

	// Async queries

	public List<Tournament> getAllTournamentsAsync() {
		return appDatabase.tournamentDao().findAllTournamentsAsync();
	}

	public Tournament getTournamentByIdAsync(String id) {
		return appDatabase.tournamentDao().findTournamentByIdAsync(id);
	}

	public String getCreatorByIdAsync(String id) {
		return appDatabase.tournamentDao().findCreatorByIdAsync(id);
	}
}
