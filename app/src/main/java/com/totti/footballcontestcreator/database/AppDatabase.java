package com.totti.footballcontestcreator.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Tournament.class}, version = 1)
@TypeConverters(value = {Tournament.Type.class})
public abstract class AppDatabase extends RoomDatabase {

	public abstract TournamentDao tournamentDao();
}
