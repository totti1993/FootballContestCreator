package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Tournament.class, Team.class}, version = 14)
public abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase INSTANCE;

	public abstract TournamentDao tournamentDao();
	public abstract TeamDao teamDao();

	public static AppDatabase getDatabase(Context context) {
		if(INSTANCE == null) {
			synchronized (AppDatabase.class) {
				if(INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "fcc")
							   .fallbackToDestructiveMigration().build();
				}
			}
		}

		return INSTANCE;
	}
}
