package com.totti.footballcontestcreator.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorite.class, Match.class, Ranking.class, Team.class, Tournament.class}, version = 32)
public abstract class AppDatabase extends RoomDatabase {

	private static AppDatabase INSTANCE;

	public abstract FavoriteDao favoriteDao();
	public abstract MatchDao matchDao();
	public abstract RankingDao rankingDao();
	public abstract TeamDao teamDao();
	public abstract TournamentDao tournamentDao();

	// Build Room database
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
