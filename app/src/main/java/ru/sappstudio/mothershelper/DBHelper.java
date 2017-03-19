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
        Log.e(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table Sleap ("
                + " id integer primary key autoincrement , "
                + "time_s bigint , " + "time_e bigint , " + "mess string, "
                +" status string "+");");
        db.execSQL("create table Food ("
                + " id integer primary key autoincrement , "
                + "time_s bigint , " + "time_e bigint , " + "mess string, "
                +" status string "+");");
        db.execSQL("create table Koliki ("
                + " id integer primary key autoincrement , "
                + "time bigint , " + "mess string, "
                +" status string "+");");
        db.execSQL("create table Walk ("
                + " id integer primary key autoincrement , "
                + "time_s bigint , " + "time_e bigint , " + "mess string, "
                +" status string "+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //проверяете какая версия сейчас и делаете апдейт
        db.execSQL("DROP TABLE IF EXISTS Sleap");
        onCreate(db);
    }
}
