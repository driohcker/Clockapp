package com.example.clock.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clock.entity.myClock;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ClockDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "clock.db";
    private static final int DATABASE_VERSION = 1;

    // 表名
    private static final String TABLE_CLOCK = "clock";

    // 列名
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_TIME_WIDE = "time_wide";
    private static final String COLUMN_REPEAT_TIMES = "repeat_times";
    private static final String COLUMN_IFUSE = "ifuse";
    private static final String COLUMN_INFO = "info";

    public ClockDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CLOCK + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_HOUR + " INTEGER, " +
                COLUMN_MINUTE + " INTEGER, " +
                COLUMN_TIME_WIDE + " TEXT, " +
                COLUMN_REPEAT_TIMES + " TEXT, " +
                COLUMN_IFUSE + " INTEGER, " +
                COLUMN_INFO + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOCK);
        onCreate(db);
    }

    // 插入数据
    public long insertClock(myClock clock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, clock.getID());
        values.put(COLUMN_HOUR, clock.getTimeHour());
        values.put(COLUMN_MINUTE, clock.getTimeMinute());
        values.put(COLUMN_TIME_WIDE, clock.getTimeWide());
        values.put(COLUMN_REPEAT_TIMES, clock.getRepeatTimes_ToString());
        values.put(COLUMN_IFUSE, clock.getIfuse());
        values.put(COLUMN_INFO, clock.getInfo());

        long id = db.insert(TABLE_CLOCK, null, values);
        db.close();
        return id;
    }

    // 查询数据
    public Cursor getAllClocks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CLOCK, null, null, null, null, null, null);
    }
    public myClock getMyClock(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM clock_table WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{id});

        int hour = cursor.getInt(cursor.getColumnIndexOrThrow("hour"));
        int minute = cursor.getInt(cursor.getColumnIndexOrThrow("minute"));
        String time_wide = cursor.getString(cursor.getColumnIndexOrThrow("time_wide"));
        String repeat_times = cursor.getString(cursor.getColumnIndexOrThrow("repeat_times"));
        int ifuse = cursor.getInt(cursor.getColumnIndexOrThrow("ifuse"));
        String info = cursor.getString(cursor.getColumnIndexOrThrow("info"));

        return new myClock(id, hour, minute, time_wide, repeat_times, ifuse, info);
    }

    // 更新数据
    public int updateClock(myClock clock, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUR, clock.getTimeHour());
        values.put(COLUMN_MINUTE, clock.getTimeMinute());
        values.put(COLUMN_TIME_WIDE, clock.getTimeWide());
        values.put(COLUMN_REPEAT_TIMES, clock.getRepeatTimes_ToString());
        values.put(COLUMN_IFUSE, clock.getIfuse());
        values.put(COLUMN_INFO, clock.getInfo());

        return db.update(TABLE_CLOCK, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // 删除数据
    public void deleteClock(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLOCK, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
