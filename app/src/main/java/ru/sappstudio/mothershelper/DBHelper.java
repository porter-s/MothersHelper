package ru.sappstudio.mothershelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by user on 01.03.17.
 */
public class DBHelper extends SQLiteOpenHelper {

    String LOG_TAG = "DBHelper";
    private static String DB_PATH = "/data/data/ru.sappstudio.mothershelper/databases/";
    private static String DB_NAME = "myDB";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, 1);
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
                + "time bigint , " + "mess string, "
                +" status string "+");");
        db.execSQL("create table Koliki ("
                + " id integer primary key autoincrement , "
                + "time bigint , " + "mess string, "
                +" status string "+");");
        db.execSQL("create table Walk ("
                + " id integer primary key autoincrement , "
                + "time_s bigint , " + "time_e bigint , " + "mess string, "
                + " status string " + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //проверяете какая версия сейчас и делаете апдейт
        db.execSQL("DROP TABLE IF EXISTS Sleap");
        db.execSQL("DROP TABLE IF EXISTS Food");
        db.execSQL("DROP TABLE IF EXISTS Koliki");
        db.execSQL("DROP TABLE IF EXISTS Walk");
        onCreate(db);
    }

//    private boolean checkDataBase(){
//        SQLiteDatabase checkDB = null;
//
//        try{
//            String myPath =DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//        }catch(SQLiteException e){
//            onUpgrade(checkDB,1,1);//база еще не существует
//        }
//        if(checkDB != null){
//            checkDB.close();
//        }
//        return checkDB != null ? true : false;
//    }
//    public void createDataBase() throws IOException {
//        boolean dbExist = checkDataBase();
//
//        if(dbExist){
//            //ничего не делать - база уже есть
//        }else{
//            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
//            this.getReadableDatabase();
//        }
//    }
}
