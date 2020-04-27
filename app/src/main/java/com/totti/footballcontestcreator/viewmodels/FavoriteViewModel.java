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
import com.totti.footballcontestcreator.database.Favorite;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

	private AppDatabase appDatabase;                // Room database
	private DatabaseReference onlineFavorites;      // Reference to Firebase "favorites" node
	private ChildEventListener childEventListener;  // Listener to modify "favorites" table in Room database

	private String userEmail;

	public FavoriteViewModel(Application application) {
		super(application);

		userEmail = null;

		appDatabase = AppDatabase.getDatabase(application);
		onlineFavorites = FirebaseDatabase.getInstance().getReference("favorites");
		childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull final DataSnapshot favorite, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.favoriteDao().insert(favorite.getValue(Favorite.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildChanged(@NonNull final DataSnapshot favorite, @Nullable String s) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.favoriteDao().update(favorite.getValue(Favorite.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildRemoved(@NonNull final DataSnapshot favorite) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						appDatabase.favoriteDao().delete(favorite.getValue(Favorite.class));
						return null;
					}
				}.execute();
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot favorite, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		};
	}

	// Add listener to the user's favorites node in Firebase
	public void addListenerToOnlineDatabase(String userEmail) {
		if(this.userEmail == null) {
			onlineFavorites.child(userEmail).addChildEventListener(childEventListener);
			this.userEmail = userEmail;
		}
	}

	// Remove listener from the user's favorites node in Firebase
	public void removeListenerFromOnlineDatabase() {
		if(userEmail != null) {
			onlineFavorites.child(userEmail).removeEventListener(childEventListener);
			userEmail = null;
		}
	}

	public void insertAll(List<Favorite> favorites) {
		appDatabase.favoriteDao().insertAll(favorites);
	}

	public void deleteAll() {
		appDatabase.favoriteDao().deleteAll();
	}

	// Observed queries

	public LiveData<List<String>> getAllFavoriteTeams() {
		return appDatabase.favoriteDao().findAllFavoriteTeams();
	}

	public LiveData<List<String>> getAllFavoriteTournaments() {
		return appDatabase.favoriteDao().findAllFavoriteTournaments();
	}

	// Async queries

	public List<String> getAllFavoriteTeamsAsync() {
		return appDatabase.favoriteDao().findAllFavoriteTeamsAsync();
	}

	public List<String> getAllFavoriteTournamentsAsync() {
		return appDatabase.favoriteDao().findAllFavoriteTournamentsAsync();
	}
}
