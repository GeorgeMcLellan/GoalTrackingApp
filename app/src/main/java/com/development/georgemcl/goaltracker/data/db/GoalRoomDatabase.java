package com.development.georgemcl.goaltracker.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.development.georgemcl.goaltracker.model.Goal;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
public abstract class GoalRoomDatabase extends RoomDatabase{

    public abstract GoalDao goalDao();

    private static volatile GoalRoomDatabase INSTANCE;

    public static GoalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GoalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GoalRoomDatabase.class, "goal_database")
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

//    private static RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen (@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//                    new PopulateDbAsync(INSTANCE).execute();
//                }
//            };
//
//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final GoalDao mDao;
//
//        PopulateDbAsync(GoalRoomDatabase db) {
//            mDao = db.goalDao();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
//            Goal goal = new Goal("goal 1", "1/1/1");
//            mDao.insert(goal);
//            goal = new Goal("goal 2", "2/2/2");
//            mDao.insert(goal);
//            return null;
//        }
//    }

}
