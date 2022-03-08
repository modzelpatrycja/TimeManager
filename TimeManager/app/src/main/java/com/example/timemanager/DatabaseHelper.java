package com.example.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Long.parseLong;

public class DatabaseHelper extends SQLiteOpenHelper implements TaskHolder {

    private Context context;
    private static final String DATABASE_NAME = "TaskList.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TIMEGOAL = "timeGoal";
    private static final String COLUMN_TIMESPENT = "timeSpent";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_DAYNUMBER = "dayNumber";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_TOTAL_TIME_SPENT = "totalTimeSpent";
    private static final String CURRENT_MONDAY = "currentModay";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TIMEGOAL + " INTEGER, " +
                COLUMN_TIMESPENT + " INTEGER, " +
                COLUMN_DAY + " INTEGER, " +
                COLUMN_DAYNUMBER + " INTEGER, " +
                COLUMN_STATE + " TEXT, " +
                COLUMN_TOTAL_TIME_SPENT + " INTEGER, " +
                CURRENT_MONDAY + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTaskToDatabase(String name, long timeGoal, long timeSpent, String day, int dayNumber, String state, long totalTimeSpent, String currentMonday) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_TIMEGOAL, timeGoal);
        cv.put(COLUMN_TIMESPENT, timeSpent);
        cv.put(COLUMN_DAY, day);
        cv.put(COLUMN_DAYNUMBER, dayNumber);
        cv.put(COLUMN_STATE, state);
        cv.put(COLUMN_TOTAL_TIME_SPENT, totalTimeSpent);
        cv.put(CURRENT_MONDAY, currentMonday);
        db.insert(TABLE_NAME, null, cv);
        Toast.makeText(context, name + " has been added", Toast.LENGTH_SHORT).show();
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String name, long timeGoal, long timeSpent, String day, int dayNumber, String state, long totalTimeSpent, String currentMonday) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_TIMEGOAL, timeGoal);
        cv.put(COLUMN_TIMESPENT, timeSpent);
        cv.put(COLUMN_DAY, day);
        cv.put(COLUMN_DAYNUMBER, dayNumber);
        cv.put(COLUMN_STATE, state);
        cv.put(COLUMN_TOTAL_TIME_SPENT, totalTimeSpent);
        cv.put(CURRENT_MONDAY, currentMonday);
        db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
    }


    void deleteRow(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "name=?", new String[]{name});
        Toast.makeText(context, name + " has been deleted", Toast.LENGTH_SHORT).show();
    }

    void deleteRowId(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void addToTaskArray(Context cx) {
        DatabaseHelper db;
        db = new DatabaseHelper(cx);
        Cursor cursor = db.readAllData();
        tasksWithTimer.clear();
        tasksInPlanner.clear();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currMonday = df.format(c.getTime());
        for (int i = 0; i < days.length; i++) {
            tasksInPlanner.add(new DayTasks(days[i], false, 1, Integer.toString(i)));
        }
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(4).equals("-")) {
                    tasksWithTimer.add(new Task(cursor.getString(0), cursor.getString(1), parseLong(cursor.getString(2)), parseLong(cursor.getString(3)), parseLong(cursor.getString(7)), currMonday));
                } else {
                    tasksInPlanner.get(Integer.parseInt(cursor.getString(5))).addToPlannerWithState(cursor.getString(1), Boolean.parseBoolean(cursor.getString(6)));
                }
            }
        }

    }

}
