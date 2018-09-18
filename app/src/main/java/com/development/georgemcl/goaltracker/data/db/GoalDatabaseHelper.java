package com.development.georgemcl.goaltracker.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "GoalDatabaseHelper";
    private static GoalDatabaseHelper sInstance;

    private static final String DATABASE_NAME  =  "Goal Database";
    private static final int DATABASE_VERSION  = 1;

    private static final String TABLE_GOAL = "goal";

    //Goal table column names
    private static final String KEY_GOAL_ID = "_id";
    private static final String KEY_GOAL_NAME = "name";
    private static final String KEY_GOAL_DESCRIPTION = "description";
    private static final String KEY_GOAL_COMPLETION_DATE = "completion_date";
    private static final String KEY_GOAL_PARENT_GOAL_ID = "parent_goal_id";


    private static final String TABLE_ACITON = "action";
    private static final String KEY_ACITON_NAME = "name";
    private static final String KEY_ACTION_PARENT_GOAL_ID = "parent_goal_id";

    /**
     * Singleton Pattern implementation.
     */
    public static synchronized GoalDatabaseHelper getIntance(Context context) {
        if (sInstance == null) {
            sInstance = new GoalDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    /**
     * Private constructor, consistent with Singleton Pattern.
     * Prevents direct initialization.
     */
    private GoalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
