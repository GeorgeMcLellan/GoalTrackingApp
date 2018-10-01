package com.development.georgemcl.goaltracker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.model.ActionTargetProgression;
import com.development.georgemcl.goaltracker.model.Goal;

@Database(entities = {Goal.class, Action.class, ActionTargetProgression.class}, version = 4, exportSchema = false)
public abstract class GoalRoomDatabase extends RoomDatabase{

    public abstract GoalDao goalDao();

    public abstract ActionDao actionDao();

    public abstract ActionTargetProgressionDao actionTargetProgressionDao();

    private static volatile GoalRoomDatabase INSTANCE;

    public static GoalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GoalRoomDatabase.class, "goal_database")
//                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new DeleteDbAsync(INSTANCE).execute();
                }
            };

    private static class DeleteDbAsync extends AsyncTask<Void, Void, Void> {

        private final GoalDao mGoalDao;
        private final ActionDao mActionDao;

        DeleteDbAsync(GoalRoomDatabase db) {
            mGoalDao = db.goalDao();
            mActionDao = db.actionDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mGoalDao.deleteAll();
            mActionDao.deleteAll();
            return null;
        }
    }

}
