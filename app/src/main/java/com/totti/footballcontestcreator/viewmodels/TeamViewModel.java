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
import com.totti.footballcontestcreator.database.Team;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;                // Room database
	private DatabaseReference onlineTeams;          // Reference to Firebase "teams" node
	private ChildEventListener childEventListener;  // Listener to modify "teams" table in Room database

	public TeamViewModel(Application application) {
		super(application);

		appDatabase = AppDatabase.getDatabase(application);
		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");
		childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot team, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.teamDao().insert(team.getValue(Team.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildChanged(@NonNull final DataSnapshot team, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.teamDao().update(team.getValue(Team.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildRemoved(@NonNull final DataSnapshot team) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.teamDao().delete(team.getValue(Team.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot team, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};
		addListenerToOnlineDatabase();
	}

	// Add listener to "teams" node in Firebase
	public void addListenerToOnlineDatabase() {
		onlineTeams.addChildEventListener(childEventListener);
	}

	// Remove listener from "teams" node in Firebase
	public void removeListenerFromOnlineDatabase() {
		onlineTeams.removeEventListener(childEventListener);
	}

	public void insertAll(List<Team> teams) {
		appDatabase.teamDao().insertAll(teams);
	}

	public void deleteAll() {
		appDatabase.teamDao().deleteAll();
	}

	// Observed queries

	public LiveData<List<Team>> getAllTeams() {
		return appDatabase.teamDao().findAllTeams();
	}

	public LiveData<Team> getTeamById(String id) {
		return appDatabase.teamDao().findTeamById(id);
	}

	public LiveData<String> getCommentsById(String id) {
		return appDatabase.teamDao().findCommentsById(id);
	}

	// Async queries

	public List<Team> getAllTeamsAsync() {
		return appDatabase.teamDao().findAllTeamsAsync();
	}

	public Team getTeamByIdAsync(String id) {
		return appDatabase.teamDao().findTeamByIdAsync(id);
	}

	public String getCreatorByIdAsync(String id) {
		return appDatabase.teamDao().findCreatorByIdAsync(id);
	}
}
