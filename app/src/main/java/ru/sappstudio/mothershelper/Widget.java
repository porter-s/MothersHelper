package ru.sappstudio.mothershelper;

/**
 * Created by USER on 25.03.2017.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class Widget extends AppWidgetProvider {

    final String LOG_TAG = "Widget";
    final static String ACTION_SLEAP = "ru.sappstudio.mothershelper.change_sleap";
    final static String ACTION_FOOD = "ru.sappstudio.mothershelper.change_foog";
    final static String ACTION_KOLIKI = "ru.sappstudio.mothershelper.change_koliki";
    final static String ACTION_WALK = "ru.sappstudio.mothershelper.change_walk";

    DBHelper dbHelper;
    long unixSeconds = System.currentTimeMillis() / 1000L; // секунды
    Date date = new Date(unixSeconds*1000L); // *1000 получаем миллисекунды

    SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
    int year_yyyy = Integer.valueOf(sdf_yyyy.format(date));

    SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
    int month_MM = Integer.valueOf(sdf_MM.format(date));

    SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
    int day_dd = Integer.valueOf(sdf_dd.format(date));

    SimpleDateFormat sdf_HH = new SimpleDateFormat("HH");
    int time_HH = Integer.valueOf(sdf_HH.format(date));

    SimpleDateFormat sdf_mm = new SimpleDateFormat("mm");
    int time_mm = Integer.valueOf(sdf_mm.format(date));
//-----------------------------------------------------------------------------//
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.e(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
        // обновляем все экземпляры
        for (int i : appWidgetIds) {

//            appWidgetManager.notifyAppWidgetViewDataChanged(i, R.id.lvWEvents);
            updateWidget(context, appWidgetManager, i);
            Log.e(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.e(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.e(LOG_TAG, "onDisabled");
    }

    static void updateWidget(Context ctx, AppWidgetManager appWidgetManager,
                             int widgetID) {
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(),
                R.layout.widget);

        setList(widgetView, ctx, widgetID);

        // Обновление виджета (Sleap)
        Intent countIntent = new Intent(ctx, Widget.class);
        countIntent.setAction(ACTION_SLEAP);
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getBroadcast(ctx, widgetID, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.btnWSleap, pIntent);

        // Обновление виджета (Food)
        countIntent = new Intent(ctx, Widget.class);
        countIntent.setAction(ACTION_FOOD);
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        pIntent = PendingIntent.getBroadcast(ctx, widgetID, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.btnWFood, pIntent);

        // Обновление виджета (Koliki)
        countIntent = new Intent(ctx, Widget.class);
        countIntent.setAction(ACTION_KOLIKI);
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        pIntent = PendingIntent.getBroadcast(ctx, widgetID, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.btnWKoliki, pIntent);

        // Обновление виджета (Walk)
        countIntent = new Intent(ctx, Widget.class);
        countIntent.setAction(ACTION_WALK);
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        pIntent = PendingIntent.getBroadcast(ctx, widgetID, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.btnWWalk, pIntent);

        // Обновляем виджет
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetID, R.id.lvWEvents);
        appWidgetManager.updateAppWidget(widgetID, widgetView);
       // Log.e(LOG_TAG,"klick");
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Проверяем, что это intent от нажатия на Sleap
        if (intent.getAction().equalsIgnoreCase(ACTION_SLEAP)) {

//            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(LOG_TAG,"*** Click Sleap ***");

                dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor c = db.query("Sleap", null, null, null, null, null, null);
                int mess = c.getColumnIndex("mess");
                if(mess<0){
                    dbHelper.onUpgrade(db,1,1);
                    Log.e(LOG_TAG,"Error DB - new DB");
                }else Log.e(LOG_TAG,"*** DB done ***");
                db.close();

                clickSleap(context);
                // Обновляем виджет
                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }

        // Проверяем, что это intent от нажатия на Food
        if (intent.getAction().equalsIgnoreCase(ACTION_FOOD)) {

//            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(LOG_TAG,"*** Click Food ***");

                dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor c = db.query("Food", null, null, null, null, null, null);
                int mess = c.getColumnIndex("mess");
                if(mess<0){
                    dbHelper.onUpgrade(db,1,1);
                    Log.e(LOG_TAG,"Error DB - new DB");
                }else Log.e(LOG_TAG,"*** DB done ***");
                db.close();

                clickFood(context);
                // Обновляем виджет
                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }

        // Проверяем, что это intent от нажатия на Koliki
        if (intent.getAction().equalsIgnoreCase(ACTION_KOLIKI)) {

//            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(LOG_TAG,"*** Click Koliki ***");

                dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor c = db.query("Koliki", null, null, null, null, null, null);
                int mess = c.getColumnIndex("mess");
                if(mess<0){
                    dbHelper.onUpgrade(db,1,1);
                    Log.e(LOG_TAG,"Error DB - new DB");
                }else Log.e(LOG_TAG,"*** DB done ***");
                db.close();

                clickKoliki(context);
                // Обновляем виджет
                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }

        // Проверяем, что это intent от нажатия на Walk
        if (intent.getAction().equalsIgnoreCase(ACTION_WALK)) {

//            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(LOG_TAG,"*** Click Sleap ***");

                dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor c = db.query("Walk", null, null, null, null, null, null);
                int mess = c.getColumnIndex("mess");
                if(mess<0){
                    dbHelper.onUpgrade(db,1,1);
                    Log.e(LOG_TAG,"Error DB - new DB");
                }else Log.e(LOG_TAG,"*** DB done ***");
                db.close();

                clickWalk(context);
                // Обновляем виджет
                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }
    }

    //-------------------------------------------------------------------------//
    static void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, LVWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        rv.setRemoteAdapter(R.id.lvWEvents, adapter);
    }

    public void clickSleap(Context context)    //обновляем дату(и время)
    {
//        RemoteViews widgetView = new RemoteViews(context.getPackageName(),
//                R.layout.widget);
       // widgetView.setImageViewBitmap(R.id.btnWSleap,);
        //widgetView.setIcon(R.id.btnWSleap,"drawable",);
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        Log.e(LOG_TAG, "--- Rows in sleap: ---");
        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query("Sleap", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int time_s = c.getColumnIndex("time_s");
            int time_e = c.getColumnIndex("time_e");
            int status_ColIndex = c.getColumnIndex("status");

//            do {
            // получаем значения по номерам столбцов и пишем все в лог
//                Log.e(LOG_TAG,
//                        "ID = " + c.getInt(idColIndex) +
//                                ", year_yyyy = " + c.getInt(year_yyyy_ColIndex) +
//                                ", month_MM = " + c.getInt(month_MM_ColIndex)+
//                                ", day_dd = " + c.getInt(day_dd_ColIndex)+
//                                ", time_HHmm = " + c.getInt(time_HHmm_ColIndex)+
//                                ", status = " + c.getString(status_ColIndex));
            // переход на следующую строку
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
//            } while (c.moveToNext());
            c.moveToLast();
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);
            if(c.getString(status_ColIndex).equals("start"))
            {
                Log.e(LOG_TAG,"Расчет = "+String.valueOf(unixSeconds - c.getLong(time_s)));

                if(unixSeconds - c.getLong(time_s)>300)
                {
//                    btnSleap.setImageResource(R.drawable.krovat_w);
                    cv.put("time_e", unixSeconds);
                    cv.put("status", "stop");

                    // вставляем запись и получаем ее ID
                    // обновляем по id
                    Log.e(LOG_TAG, "--- Update mytable: ---");

                    int updCount = db.update("Sleap", cv, "id = ?",
                            new String[] {c.getString(idColIndex)});
                    Log.e(LOG_TAG, "updated rows count = " + updCount);

                    Toast toast = Toast.makeText(context,
                            "Проснулась!)", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(context,
                            "Не прошло и 5 минут.\n  Отмена действия!)", Toast.LENGTH_LONG);
                    toast.show();
                    // удаляем последнюю запись
                    int clearCount = db.delete("Sleap", "id = "+String.valueOf(c.getInt(idColIndex)),null);
                    Log.e(LOG_TAG, "deleted rows count = " + clearCount + " id = "+String.valueOf(c.getInt(idColIndex)));
//                    btnSleap.setImageResource(R.drawable.krovat_w);

//                    c.moveToFirst();
//                    do {
//                        // получаем значения по номерам столбцов и пишем все в лог
//                        Log.e(LOG_TAG,
//                                "ID = " + c.getInt(idColIndex) +
//                                        ", time_s = " + c.getLong(time_s) +
//                                        ", status = " + c.getString(status_ColIndex));
//                        // переход на следующую строку
//                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
//                    } while (c.moveToNext());
                }
            }else
            {
                Log.e(LOG_TAG, "DB = null");
//                btnSleap.setImageResource(R.drawable.krovat);
                Log.e(LOG_TAG, "--- Insert in Sleap: ---");
                updateDate();
//                Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                cv.put("time_s", unixSeconds);
                cv.put("status", "start");

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Sleap", null, cv);
                Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                Toast toast = Toast.makeText(context,
                        "Уснула...", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Log.e(LOG_TAG, "DB = null");
//            btnSleap.setImageResource(R.drawable.krovat);
            Log.e(LOG_TAG, "--- Insert in Sleap: ---");
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

            cv.put("time_s", unixSeconds);
            cv.put("status", "start");

            // вставляем запись и получаем ее ID
            long rowID = db.insert("Sleap", null, cv);
            Log.e(LOG_TAG, "row inserted, ID = " + rowID);
            Toast toast = Toast.makeText(context,
                    "Уснула...", Toast.LENGTH_LONG);
            toast.show();
        }


        c.close();
    }

    public void clickFood(Context context)
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        Log.e(LOG_TAG, "--- Rows in Food: ---");
        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query("Food", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int timeColIndex = c.getColumnIndex("time");
            int status_ColIndex = c.getColumnIndex("status");
            c.moveToLast();
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);
            if(c.getString(status_ColIndex)!=null)
            {
                //Log.e(LOG_TAG,"Расчет = "+String.valueOf(unixSeconds - c.getLong(time)));
                if(unixSeconds - c.getLong(timeColIndex)>300)
                {
                    //Log.e(LOG_TAG, "DB = null");
//                    btnFood.setImageResource(R.drawable.but);
                    Log.e(LOG_TAG, "--- Insert in Food: ---");
                    updateDate();
//                  Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                    cv.put("time", unixSeconds);
                    cv.put("status", "start");

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("Food", null, cv);
                    Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                    Toast toast = Toast.makeText(context,
                            "Кушаем!", Toast.LENGTH_LONG);
                    toast.show();
//                    btnFood.setImageResource(R.drawable.but_w);
                }else{
//                    btnFood.setImageResource(R.drawable.but);
                    Toast toast = Toast.makeText(context,
                            "Не прошло и 5 минут.\n  Отмена действия!)", Toast.LENGTH_LONG);
                    toast.show();
                    // удаляем последнюю запись
                    int clearCount = db.delete("Food", "id = "+String.valueOf(c.getInt(idColIndex)),null);
                    Log.e(LOG_TAG, "deleted rows count = " + clearCount + " id = "+String.valueOf(c.getInt(idColIndex)));
//                    btnFood.setImageResource(R.drawable.but_w);
                }
            }else
            {
                Log.e(LOG_TAG, "DB = null");
//                btnFood.setImageResource(R.drawable.but);
                Log.e(LOG_TAG, "--- Insert in Food: ---");
                updateDate();
//                Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                cv.put("time", unixSeconds);
                cv.put("status", "start");

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Food", null, cv);
                Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                Toast toast = Toast.makeText(context,
                        "Кушаем!", Toast.LENGTH_LONG);
                toast.show();
//                btnFood.setImageResource(R.drawable.but_w);
            }
        } else {
            Log.e(LOG_TAG, "DB = null");
//            btnFood.setImageResource(R.drawable.but);
            Log.e(LOG_TAG, "--- Insert in Food: ---");
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

            cv.put("time", unixSeconds);
            cv.put("status", "start");

            // вставляем запись и получаем ее ID
            long rowID = db.insert("Food", null, cv);
            Log.e(LOG_TAG, "row inserted, ID = " + rowID);
            Toast toast = Toast.makeText(context,
                    "Кушаем!", Toast.LENGTH_LONG);
            toast.show();
//            btnFood.setImageResource(R.drawable.but_w);
        }


        c.close();
    }

    public void clickKoliki(Context context)
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        Log.e(LOG_TAG, "--- Rows in Koliki: ---");
        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query("Koliki", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int timeColIndex = c.getColumnIndex("time");
            int status_ColIndex = c.getColumnIndex("status");
            c.moveToLast();
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);
            if(c.getString(status_ColIndex)!=null)
            {
                //Log.e(LOG_TAG,"Расчет = "+String.valueOf(unixSeconds - c.getLong(time)));
                if(unixSeconds - c.getLong(timeColIndex)>300)
                {
                    //Log.e(LOG_TAG, "DB = null");
                    //btnKoliki.setImageResource(R.drawable.koliki);
                    Log.e(LOG_TAG, "--- Insert in Koliki: ---");
                    updateDate();
//                  Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                    cv.put("time", unixSeconds);
                    cv.put("status", "start");

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("Koliki", null, cv);
                    Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                    Toast toast = Toast.makeText(context,
                            "Колик:(", Toast.LENGTH_LONG);
                    toast.show();
//                    btnKoliki.setImageResource(R.drawable.koliki_w);
                }else{
//                    btnKoliki.setImageResource(R.drawable.koliki);
                    Toast toast = Toast.makeText(context,
                            "Не прошло и 5 минут.\n  Отмена действия!)", Toast.LENGTH_LONG);
                    toast.show();
                    // удаляем последнюю запись
                    int clearCount = db.delete("Koliki", "id = "+String.valueOf(c.getInt(idColIndex)),null);
                    Log.e(LOG_TAG, "deleted rows count = " + clearCount + " id = "+String.valueOf(c.getInt(idColIndex)));
//                    btnKoliki.setImageResource(R.drawable.koliki_w);
                }
            }else
            {
                Log.e(LOG_TAG, "DB = null");
//                btnKoliki.setImageResource(R.drawable.koliki);
                Log.e(LOG_TAG, "--- Insert in Koliki: ---");
                updateDate();
//                Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                cv.put("time", unixSeconds);
                cv.put("status", "start");

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Koliki", null, cv);
                Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                Toast toast = Toast.makeText(context,
                        "Колик:(", Toast.LENGTH_LONG);
                toast.show();
//                btnKoliki.setImageResource(R.drawable.koliki_w);
            }
        } else {
            Log.e(LOG_TAG, "DB = null");
//            btnKoliki.setImageResource(R.drawable.koliki);
            Log.e(LOG_TAG, "--- Insert in Koliki: ---");
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

            cv.put("time", unixSeconds);
            cv.put("status", "start");

            // вставляем запись и получаем ее ID
            long rowID = db.insert("Koliki", null, cv);
            Log.e(LOG_TAG, "row inserted, ID = " + rowID);
            Toast toast = Toast.makeText(context,
                    "Колик:(", Toast.LENGTH_LONG);
            toast.show();
//            btnKoliki.setImageResource(R.drawable.koliki_w);
        }


        c.close();
    }

    public void clickWalk(Context context)
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        Log.e(LOG_TAG, "--- Rows in sleap: ---");
        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query("Walk", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int time_s = c.getColumnIndex("time_s");
            int time_e = c.getColumnIndex("time_e");
            int status_ColIndex = c.getColumnIndex("status");

//            do {
            // получаем значения по номерам столбцов и пишем все в лог
//                Log.e(LOG_TAG,
//                        "ID = " + c.getInt(idColIndex) +
//                                ", year_yyyy = " + c.getInt(year_yyyy_ColIndex) +
//                                ", month_MM = " + c.getInt(month_MM_ColIndex)+
//                                ", day_dd = " + c.getInt(day_dd_ColIndex)+
//                                ", time_HHmm = " + c.getInt(time_HHmm_ColIndex)+
//                                ", status = " + c.getString(status_ColIndex));
            // переход на следующую строку
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
//            } while (c.moveToNext());
            c.moveToLast();
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);
            if(c.getString(status_ColIndex).equals("start"))
            {
                Log.e(LOG_TAG,"Расчет = "+String.valueOf(unixSeconds - c.getLong(time_s)));

                if(unixSeconds - c.getLong(time_s)>300)
                {
//                    btnWalk.setImageResource(R.drawable.kolaska_w);
                    cv.put("time_e", unixSeconds);
                    cv.put("status", "stop");

                    // вставляем запись и получаем ее ID
                    // обновляем по id
                    Log.e(LOG_TAG, "--- Update Walk: ---");

                    int updCount = db.update("Walk", cv, "id = ?",
                            new String[] {c.getString(idColIndex)});
                    Log.e(LOG_TAG, "updated rows count = " + updCount);

                    Toast toast = Toast.makeText(context,
                            "Погуляли!)", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(context,
                            "Не прошло и 5 минут.\n  Отмена действия!)", Toast.LENGTH_LONG);
                    toast.show();
                    // удаляем последнюю запись
                    int clearCount = db.delete("Walk", "id = "+String.valueOf(c.getInt(idColIndex)),null);
                    Log.e(LOG_TAG, "deleted rows count = " + clearCount + " id = " + String.valueOf(c.getInt(idColIndex)));
//                    btnWalk.setImageResource(R.drawable.kolaska_w);

//                    c.moveToFirst();
//                    do {
//                        // получаем значения по номерам столбцов и пишем все в лог
//                        Log.e(LOG_TAG,
//                                "ID = " + c.getInt(idColIndex) +
//                                        ", time_s = " + c.getLong(time_s) +
//                                        ", status = " + c.getString(status_ColIndex));
//                        // переход на следующую строку
//                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
//                    } while (c.moveToNext());
                }
            }else
            {
                Log.e(LOG_TAG, "DB = null");
//                btnWalk.setImageResource(R.drawable.kolaska);
                Log.e(LOG_TAG, "--- Insert in Walk: ---");
                updateDate();
//                Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                cv.put("time_s", unixSeconds);
                cv.put("status", "start");

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Walk", null, cv);
                Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                Toast toast = Toast.makeText(context,
                        "Гуляем...", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Log.e(LOG_TAG, "DB = null");
//            btnWalk.setImageResource(R.drawable.kolaska);
            Log.e(LOG_TAG, "--- Insert in Walk: ---");
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

            cv.put("time_s", unixSeconds);
            cv.put("status", "start");

            // вставляем запись и получаем ее ID
            long rowID = db.insert("Walk", null, cv);
            Log.e(LOG_TAG, "row inserted, ID = " + rowID);
            Toast toast = Toast.makeText(context,
                    "Гуляем...", Toast.LENGTH_LONG);
            toast.show();
        }


        c.close();
    }

    public void updateDate()    //обновляем дату(и время)
    {
        unixSeconds = System.currentTimeMillis() / 1000L; // секунды
        date = new Date(unixSeconds*1000L); // *1000 получаем миллисекунды

        year_yyyy = Integer.valueOf(sdf_yyyy.format(date));
        month_MM = Integer.valueOf(sdf_MM.format(date));
        day_dd = Integer.valueOf(sdf_dd.format(date));
        time_HH = Integer.valueOf(sdf_HH.format(date));
        time_mm = Integer.valueOf(sdf_mm.format(date));
    }

}
