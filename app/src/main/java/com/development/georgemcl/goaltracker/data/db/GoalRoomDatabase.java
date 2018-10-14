package com.development.georgemcl.goaltracker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;

/**
 * RoomDatabase implementation, making use of the Dao classes to create an SQL database
 */
@Database(entities = {Goal.class, Action.class}, version = 2, exportSchema = false)
public abstract class GoalRoomDatabase extends RoomDatabase{

    public abstract GoalDao goalDao();

    public abstract ActionDao actionDao();

    private static volatile GoalRoomDatabase INSTANCE;

    /**
     * Provides access to the database
     * Implements Singleton pattern to ensure instance of database is not constantly recreated
     * @param context Application context
     * @return Database
     */

    public static GoalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GoalRoomDatabase.class, "goal_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
