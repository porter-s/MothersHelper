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
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class Widget extends AppWidgetProvider {

    final String LOG_TAG = "Widget";
    final static String ACTION_CHANGE = "ru.sappstudio.mothershelper.change_count";
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

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.e(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // обновляем все экземпляры
        for (int i : appWidgetIds) {
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

        // Обновление виджета (Sleap)
        Intent countIntent = new Intent(ctx, Widget.class);
        countIntent.setAction(ACTION_CHANGE);
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getBroadcast(ctx, widgetID, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.btnWSleap, pIntent);

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
       // Log.e(LOG_TAG,"klick");
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // Проверяем, что это intent от нажатия на третью зону
        if (intent.getAction().equalsIgnoreCase(ACTION_CHANGE)) {

//            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(LOG_TAG,"Click");
//                // Читаем значение счетчика, увеличиваем на 1 и записываем
//                SharedPreferences sp = context.getSharedPreferences(
//                        ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
//                int cnt = sp.getInt(ConfigActivity.WIDGET_COUNT + mAppWidgetId,  0);
//                sp.edit().putInt(ConfigActivity.WIDGET_COUNT + mAppWidgetId,
//                        ++cnt).commit();

                dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor c = db.query("Sleap", null, null, null, null, null, null);
                int mess = c.getColumnIndex("mess");
                if(mess<0){
                    dbHelper.onUpgrade(db,1,1);
                    Log.e(LOG_TAG,"Error DB - new DB");
                }else Log.e(LOG_TAG,"DB done");
                db.close();

                clickSleap(context);
                // Обновляем виджет
                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }
    }

    public void clickSleap(Context context)    //обновляем дату(и время)
    {
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
//    public void initData(String _tableName, String _actionStart, String _actionStop, ImageButton _button, int _idImageActiv, int _idImagePassiv, int _idIconLv, int _idFonLvOn, int _idFonLvOff)    //обновляем данные
//    {
//        // создаем объект для данных
//        ContentValues cv = new ContentValues();
//        // подключаемся к БД
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        //dbHelper.onCreate(db);
//
//        //загружаем Sleap//
//
//        // делаем запрос всех данных из таблицы sleap, получаем Cursor
//        Cursor c = db.query(_tableName, null, null, null, null, null, null);
//
//        // ставим позицию курсора на первую строку выборки
//        // если в выборке нет строк, вернется false
//        if(_tableName.equals("Koliki")||_tableName.equals("Food"))
//        {
//            if (c.moveToFirst()) {
//                // определяем номера столбцов по имени в выборке
//                int idColIndex = c.getColumnIndex("id");
//                int time = c.getColumnIndex("time");
//                int mess = c.getColumnIndex("mess");
//                int status_ColIndex = c.getColumnIndex("status");
//                updateDate();
//                c.moveToLast();
//
//                long kol = c.getLong(time);
//
//                String _yyyy, _MM, _dd, _HH, _mm;
//
//                if (getTimeFormat(kol, "yyyy").equals("0000")) _yyyy = "";
//                else  _yyyy = getTimeFormat(kol, "yyyy");
//
//
//                if (getTimeFormat(kol, "MM").equals("00")) _MM = "";
//                else _MM = getTimeFormat(kol, "MM");
//
//                if (getTimeFormat(kol, "dd").equals("00")) _dd = "";
//                else _dd = getTimeFormat(kol, "dd");
//
//                if (getTimeFormat(kol, "HH").equals("00")) _HH = "";
//                else _HH = getTimeFormat(kol, "HH");
//
//                _mm = getTimeFormat(kol, "mm") + " мин";
//                if (differenceUnixSeconds(unixSeconds,c.getLong(time))<300){
//                    eventArrayList.add(new LVEvent(_tableName,
//                            _idIconLv, _idFonLvOn,
//                            c.getString(idColIndex),
//                            c.getLong(time), c.getLong(time),
//                            _actionStart,
//                            getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"),
//                            "",
//                            _dd+"."+_MM+"."+_yyyy,
//                            c.getString(mess),
//                            R.drawable.status_start));
//                }else{
//                    eventArrayList.add(new LVEvent(_tableName,
//                            _idIconLv, _idFonLvOn,
//                            c.getString(idColIndex),
//                            c.getLong(time), c.getLong(time),
//                            _actionStart,
//                            getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"),
//                            "",
//                            _dd+"."+_MM+"."+_yyyy,
//                            c.getString(mess),
//                            R.drawable.status_stop));
//                }
//
//            }
//        }else
//        if (c.moveToFirst()) {
//            // определяем номера столбцов по имени в выборке
//            int idColIndex = c.getColumnIndex("id");
//            int time_s = c.getColumnIndex("time_s");
//            int time_e = c.getColumnIndex("time_e");
//            int mess = c.getColumnIndex("mess");
//            int status_ColIndex = c.getColumnIndex("status");
//            updateDate();
//            // do {
//
//            //  } while (c.moveToNext());
//            c.moveToLast();
//            //---------------Записываем последнюю запись-------------
//            if (c.getString(status_ColIndex).equals("start")){
//
//                long kol = differenceUnixSeconds(unixSeconds,c.getLong(time_s));
//
//                String _yyyy,_MM,_dd,_HH,_mm;
//
//                if (getTimeFormat(kol,"yyyy").equals("0000")) _yyyy = "";
//                else if(Integer.valueOf(getTimeFormat(kol,"yyyy"))<10) _yyyy = getTimeFormat(kol,"yyyy") + " г. ";
//                else _yyyy = getTimeFormat(kol,"yyyy") + " л ";
//
//                if (getTimeFormat(kol,"MM").equals("00")) _MM = "";
//                else _MM = getTimeFormat(kol,"MM") + " мес ";
//
//                if (getTimeFormat(kol,"dd").equals("00")) _dd = "";
//                else _dd = getTimeFormat(kol,"dd") + " д ";
//
//                if (getTimeFormat(kol,"HH").equals("00")) _HH = "";
//                else _HH = getTimeFormat(kol,"HH") + " ч ";
//
//                _mm = getTimeFormat(kol,"mm")+" мин";
//                eventArrayList.add(new LVEvent(_tableName,
//                        _idIconLv, _idFonLvOn,
//                        c.getString(idColIndex),
//                        c.getLong(time_s),c.getLong(time_e),
//                        _actionStart,
//                        _yyyy+_MM+_dd+_HH+_mm,
//                        getTimeFormat(c.getLong(time_s),"HH")+"-"+getTimeFormat(c.getLong(time_s),"mm"),
//                        " - : - ",
//                        c.getString(mess),
//                        R.drawable.status_start));
//            }
//            else{
//                long kol = differenceUnixSeconds(c.getLong(time_e),c.getLong(time_s));
//
//                String _yyyy,_MM,_dd,_HH,_mm;
//
//                if (getTimeFormat(kol,"yyyy").equals("0000")) _yyyy = "";
//                else if(Integer.valueOf(getTimeFormat(kol,"yyyy"))<10) _yyyy = getTimeFormat(kol,"yyyy") + " г. ";
//                else _yyyy = getTimeFormat(kol,"yyyy") + " л ";
//
//                if (getTimeFormat(kol,"MM").equals("00")) _MM = "";
//                else _MM = getTimeFormat(kol,"MM") + " мес ";
//
//                if (getTimeFormat(kol,"dd").equals("00")) _dd = "";
//                else _dd = getTimeFormat(kol,"dd") + " д ";
//
//                if (getTimeFormat(kol,"HH").equals("00")) _HH = "";
//                else _HH = getTimeFormat(kol,"HH") + " ч ";
//
//                _mm = getTimeFormat(kol,"mm")+" мин";
//
//                eventArrayList.add(new LVEvent(_tableName,
//                        _idIconLv, _idFonLvOff,
//                        c.getString(idColIndex),
//                        c.getLong(time_s),c.getLong(time_e),
//                        _actionStop,
//                        _yyyy+_MM+_dd+_HH+_mm,
//                        getTimeFormat(c.getLong(time_s),"HH")+"-"+getTimeFormat(c.getLong(time_s),"mm"),
//                        getTimeFormat(c.getLong(time_e),"HH")+"-"+getTimeFormat(c.getLong(time_e),"mm"),
//                        c.getString(mess),
//                        R.drawable.status_stop));
//            }
//            //---------------------------------------------------------------
//
//            c.moveToLast();
//            Log.e(LOG_TAG, _tableName +" status = " + c.getString(status_ColIndex));
//
//            if (c.getString(status_ColIndex).equals("start")) {
//                _button.setImageResource(_idImageActiv);
//            } else {
//                _button.setImageResource(_idImagePassiv);
//            }
//        }
//        c.close();
//
//    }
}
