package com.development.georgemcl.goaltracker.data.db;


import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.Goal;

/**
 * RoomDatabase implementation, making use of the Dao classes to create an SQL database
 */
@Database(entities = {Goal.class, Action.class}, version = 3, exportSchema = false)
public abstract class GoalRoomDatabase extends RoomDatabase {

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
