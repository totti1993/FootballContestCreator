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
import com.totti.footballcontestcreator.database.Team;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;
	private DatabaseReference onlineTeams;

	public TeamViewModel(Application application) {
		super(application);

		appDatabase = AppDatabase.getDatabase(application);

		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");
		onlineTeams.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot team, @Nullable String s) {
				/*String id = team.getKey();
				String name = team.child("name").getValue(String.class);
				String comments = team.child("comments").getValue(String.class);

				Team insertedTeam = new Team(id, name, comments);*/

				Log.d("asd", "onChildAdded called on Team");
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
				Log.d("asd", "onChildChanged called on Team");
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
				Log.d("asd", "onChildRemoved called on Team");
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
		});
	}

	public void insertAll(List<Team> teams) {
		appDatabase.teamDao().insertAll(teams);
	}

	public void deleteAll() {
		appDatabase.teamDao().deleteAll();
	}

	public LiveData<List<Team>> getAllTeamsOrdered() {
		return appDatabase.teamDao().findAllTeamsOrdered();
	}

	public List<Team> getAllTeamsAsync() {
		return appDatabase.teamDao().findAllTeamsAsync();
	}

	public LiveData<Team> getTeamById(String id) {
		return appDatabase.teamDao().findTeamById(id);
	}

	public Team getTeamByIdAsync(String id) {
		return appDatabase.teamDao().findTeamByIdAsync(id);
	}

	public LiveData<String> getCommentsById(String id) {
		return appDatabase.teamDao().findCommentsById(id);
	}
}
