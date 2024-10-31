package com.example.clock.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.clock.entity.myClock;

import java.util.ArrayList;
import java.util.List;

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

    private static ClockDatabaseHelper instance;

    private static String TAG = "CLockDatabaseHelper";
    //单例模式
    public static synchronized ClockDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG,"无本地数据库，已创建数据库:" + TAG);
            instance = new ClockDatabaseHelper(context.getApplicationContext());
        }else{
            Log.d(TAG,"已存在本地数据库，使用本地数据库" + TAG);
        }
        return instance;
    }

    private ClockDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"创建了数据库表:" + TABLE_CLOCK);
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
    public void insertClock(myClock clock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, clock.getID());
        values.put(COLUMN_HOUR, clock.getTimeHour());
        values.put(COLUMN_MINUTE, clock.getTimeMinute());
        values.put(COLUMN_TIME_WIDE, clock.getTimeWide());
        values.put(COLUMN_REPEAT_TIMES, clock.getRepeatTimes_ToString());
        values.put(COLUMN_IFUSE, clock.getIfuse());
        values.put(COLUMN_INFO, clock.getInfo());

        db.insertWithOnConflict(TABLE_CLOCK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG,"已添加myclock对象:" + clock.getID());
        db.close();
    }

    public List<myClock> getAllClocksAsList() {
        List<myClock> clocks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询所有行
        Cursor cursor = db.query(TABLE_CLOCK, null, null, null, null, null, null);

        // 遍历结果集，将每行转换为 myClock 对象并添加到列表中
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int hour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE));
                String timeWide = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME_WIDE));
                String repeatTimes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPEAT_TIMES));
                int ifUse = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IFUSE));
                String info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO));

                // 创建 myClock 对象并添加到列表
                myClock clock = new myClock(id, hour, minute, timeWide, repeatTimes, ifUse, info);
                Log.d(TAG,"已获取myclock对象:" + id);
                clocks.add(clock);
            } while (cursor.moveToNext());
        }

        // 关闭 cursor 和数据库连接
        cursor.close();
        db.close();

        return clocks;
    }

    public myClock getMyClock(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLOCK + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{id});

        myClock clock = null; // 初始化为 null
        if (cursor != null && cursor.moveToFirst()) { // 确保 Cursor 不为 null 且移动到第一条记录
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE));
            String time_wide = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME_WIDE));
            String repeat_times = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPEAT_TIMES));
            int ifuse = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IFUSE));
            String info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO));

            // 创建 myClock 对象
            clock = new myClock(id, hour, minute, time_wide, repeat_times, ifuse, info);
        }

        cursor.close(); // 确保关闭 Cursor
        return clock; // 返回 myClock 对象（可能为 null）
    }


    // 更新数据
    public void updateClock(myClock clock, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUR, clock.getTimeHour());
        values.put(COLUMN_MINUTE, clock.getTimeMinute());
        values.put(COLUMN_TIME_WIDE, clock.getTimeWide());
        values.put(COLUMN_REPEAT_TIMES, clock.getRepeatTimes_ToString());
        values.put(COLUMN_IFUSE, clock.getIfuse());
        values.put(COLUMN_INFO, clock.getInfo());

        db.update(TABLE_CLOCK, values, COLUMN_ID + " = ?", new String[]{id});
        Log.d(TAG,"已更新myclock对象:" + id);
        db.close();
    }

    // 更新 myClock 的 ifuse 属性
    public void updateClockIfuse(String id, boolean ifuse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 将布尔值转换为整数（SQLite不支持布尔类型）
        values.put(COLUMN_IFUSE, ifuse ? 1 : 0);

        // 执行更新操作
        db.update(TABLE_CLOCK, values, COLUMN_ID + " = ?", new String[]{id});
        Log.d(TAG,"已更新myclock对象:" + id + " ifuse属性为：" + ifuse);
        db.close();
    }


    // 删除数据
    public void deleteClock(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLOCK, COLUMN_ID + " = ?", new String[]{id});
        Log.d(TAG,"已删除myclock对象:" + id);
        db.close();
    }
}
