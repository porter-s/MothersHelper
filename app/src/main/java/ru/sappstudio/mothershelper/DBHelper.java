package ru.sappstudio.mothershelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 01.03.17.
 */
public class DBHelper extends SQLiteOpenHelper {

    String LOG_TAG = "DBHelper";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table Sleap ("
                + "id integer primary key autoincrement,"
                + "year_yyyy_s integer," + "month_MM_s integer," + "day_dd_s integer,"+"time_HH_s integer,"+"time_mm_s integer,"
                + "year_yyyy_e integer," + "month_MM_e integer," + "day_dd_e integer,"+"time_HH_e integer,"+"time_mm_e integer,"
                +"status string"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
