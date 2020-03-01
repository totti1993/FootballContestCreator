package com.totti.footballcontestcreator.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Match.class, Ranking.class, Team.class, Tournament.class}, version = 28)
public abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase INSTANCE;

	public abstract MatchDao matchDao();
	public abstract RankingDao rankingDao();
	public abstract TeamDao teamDao();
	public abstract TournamentDao tournamentDao();

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
