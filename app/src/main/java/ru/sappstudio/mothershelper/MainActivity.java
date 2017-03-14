package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {

    final String LOG_TAG = "MainActivity";

    ImageButton btnSleap, btnFood, btnGame, btnWalk;

    DBHelper dbHelper;

    SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
    int year_yyyy = Integer.valueOf(sdf_yyyy.format(new Date()));

    SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
    int month_MM = Integer.valueOf(sdf_MM.format(new Date()));

    SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
    int day_dd = Integer.valueOf(sdf_dd.format(new Date()));

    SimpleDateFormat sdf_HH = new SimpleDateFormat("HH");
    int time_HH = Integer.valueOf(sdf_HH.format(new Date()));

    SimpleDateFormat sdf_mm = new SimpleDateFormat("mm");
    int time_mm = Integer.valueOf(sdf_mm.format(new Date()));

    ArrayList<LVEvent> eventArrayList = new ArrayList<LVEvent>();
    LVEventAdapter lvEventAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   // private GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSleap = (ImageButton) findViewById(R.id.btnSleap);
        btnSleap.setOnClickListener(this);

        btnFood = (ImageButton) findViewById(R.id.btnFood);
        btnFood.setOnClickListener(this);

        btnGame = (ImageButton) findViewById(R.id.btnGame);
        btnGame.setOnClickListener(this);

        btnWalk = (ImageButton) findViewById(R.id.btnWalk);
        btnWalk.setOnClickListener(this);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);


        // создаем адаптер
        lvEventAdapter = new LVEventAdapter(this, eventArrayList);
        initData();

        // настраиваем список
        ListView listOfEvents = (ListView) findViewById(R.id.listOfEvents);
        listOfEvents.setAdapter(lvEventAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSleap:
                clickSleap();
                initData();
                lvEventAdapter.notifyDataSetChanged();
            break;

            case R.id.btnFood:
                btnFood.setImageResource(R.drawable.but);
            break;

            case R.id.btnGame:
                btnGame.setImageResource(R.drawable.activ);
            break;

            case R.id.btnWalk:
                btnWalk.setImageResource(R.drawable.kalaska);
            break;
        }
    }

    public void clickSleap()    //обновляем дату(и время)
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
            int year_yyyy_s_ColIndex = c.getColumnIndex("year_yyyy_s");
            int month_MM_s_ColIndex = c.getColumnIndex("month_MM_s");
            int day_dd_s_ColIndex = c.getColumnIndex("day_dd_s");
            int time_HH_s_ColIndex = c.getColumnIndex("time_HH_s");
            int time_mm_s_ColIndex = c.getColumnIndex("time_mm_s");
            int status_ColIndex = c.getColumnIndex("status");

            do {
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
            } while (c.moveToNext());
            c.moveToLast();
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);
            if(c.getString(status_ColIndex).equals("start"))
            {
                Log.e(LOG_TAG,"Расчет = "+String.valueOf(year_yyyy*100000000 + month_MM*1000000 + day_dd*10000 + time_HH*100 + time_mm -
                        c.getInt(year_yyyy_s_ColIndex)*100000000 -
                        c.getInt(month_MM_s_ColIndex)*1000000 -
                        c.getInt(day_dd_s_ColIndex)*10000 -
                        c.getInt(time_HH_s_ColIndex)*100 -
                        c.getInt(time_mm_s_ColIndex)));

                if((year_yyyy*100000000+month_MM*1000000+day_dd*10000+time_HH*100+time_mm-
                        c.getInt(year_yyyy_s_ColIndex)*100000000-
                        c.getInt(month_MM_s_ColIndex)*1000000-
                        c.getInt(day_dd_s_ColIndex)*10000-
                        c.getInt(time_HH_s_ColIndex)*100-
                        c.getInt(time_mm_s_ColIndex))>5)
                {
                    btnSleap.setImageResource(R.drawable.krovat2_w);
                    cv.put("year_yyyy_e", year_yyyy);
                    cv.put("month_MM_e", month_MM);
                    cv.put("day_dd_e", day_dd);
                    cv.put("time_HH_e", time_HH);
                    cv.put("time_mm_e", time_mm);
                    cv.put("status", "stop");

                    // вставляем запись и получаем ее ID
                    // обновляем по id
                    Log.e(LOG_TAG, "--- Update mytable: ---");

                    int updCount = db.update("Sleap", cv, "id = ?",
                            new String[] {c.getString(idColIndex)});
                    Log.e(LOG_TAG, "updated rows count = " + updCount);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Проснулась!)", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не прошло и 5 минут.\n  Отмена действия!)", Toast.LENGTH_LONG);
                    toast.show();
                    // удаляем последнюю запись
                    int clearCount = db.delete("Sleap", "id = "+String.valueOf(c.getInt(idColIndex)),null);
                    Log.e(LOG_TAG, "deleted rows count = " + clearCount + " id = "+String.valueOf(c.getInt(idColIndex)));
                    btnSleap.setImageResource(R.drawable.krovat2_w);

                    c.moveToFirst();
                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.e(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", year_yyyy_s = " + c.getInt(year_yyyy_s_ColIndex) +
                                        ", month_MM_s = " + c.getInt(month_MM_s_ColIndex)+
                                        ", day_dd_s = " + c.getInt(day_dd_s_ColIndex)+
                                        ", time_HH_s = " + c.getInt(time_HH_s_ColIndex)+
                                        ", time_mm_s = " + c.getInt(time_mm_s_ColIndex)+
                                        ", status = " + c.getString(status_ColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                }
            }else
            {
                Log.e(LOG_TAG, "DB = null");
                btnSleap.setImageResource(R.drawable.krovat2);
                Log.e(LOG_TAG, "--- Insert in Sleap: ---");
                updateDate();
//                Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                cv.put("year_yyyy_s", year_yyyy);
                cv.put("month_MM_s", month_MM);
                cv.put("day_dd_s", day_dd);
                cv.put("time_HH_s", time_HH);
                cv.put("time_mm_s", time_mm);
                cv.put("status", "start");

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Sleap", null, cv);
                Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Уснула...", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Log.e(LOG_TAG, "DB = null");
            btnSleap.setImageResource(R.drawable.krovat2);
            Log.e(LOG_TAG, "--- Insert in Sleap: ---");
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

            cv.put("year_yyyy_s", year_yyyy);
            cv.put("month_MM_s", month_MM);
            cv.put("day_dd_s", day_dd);
            cv.put("time_HH_s", time_HH);
            cv.put("time_mm_s", time_mm);
            cv.put("status", "start");

            // вставляем запись и получаем ее ID
            long rowID = db.insert("Sleap", null, cv);
            Log.e(LOG_TAG, "row inserted, ID = " + rowID);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Уснула...", Toast.LENGTH_LONG);
            toast.show();
        }


        c.close();
    }

    public void updateDate()    //обновляем дату(и время)
    {
        year_yyyy = Integer.valueOf(sdf_yyyy.format(new Date()));
        month_MM = Integer.valueOf(sdf_MM.format(new Date()));
        day_dd = Integer.valueOf(sdf_dd.format(new Date()));
        time_HH = Integer.valueOf(sdf_HH.format(new Date()));
        time_mm = Integer.valueOf(sdf_mm.format(new Date()));
    }

    public void initData()    //обновляем данные
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query("Sleap", null, null, null, null, null, null);
        eventArrayList.clear();
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int year_yyyy_s_ColIndex = c.getColumnIndex("year_yyyy_s");
            int month_MM_s_ColIndex = c.getColumnIndex("month_MM_s");
            int day_dd_s_ColIndex = c.getColumnIndex("day_dd_s");
            int time_HH_s_ColIndex = c.getColumnIndex("time_HH_s");
            int time_mm_s_ColIndex = c.getColumnIndex("time_mm_s");
            int year_yyyy_e_ColIndex = c.getColumnIndex("year_yyyy_e");
            int month_MM_e_ColIndex = c.getColumnIndex("month_MM_e");
            int day_dd_e_ColIndex = c.getColumnIndex("day_dd_e");
            int time_HH_e_ColIndex = c.getColumnIndex("time_HH_e");
            int time_mm_e_ColIndex = c.getColumnIndex("time_mm_e");
            int status_ColIndex = c.getColumnIndex("status");
            updateDate();
            do {
                if((month_MM ==c.getInt(month_MM_s_ColIndex))&&(day_dd==c.getInt(day_dd_s_ColIndex)))
                    if (c.getString(status_ColIndex).equals("start"))
                    eventArrayList.add(new LVEvent(R.drawable.krovat2_b,
                            "Cпит",
                            String.valueOf(time_HH - Integer.valueOf(c.getString(time_HH_s_ColIndex)))+":"+
                            String.valueOf(time_mm - Integer.valueOf(c.getString(time_mm_s_ColIndex))),
                            c.getString(time_HH_s_ColIndex)+":"+
                            c.getString(time_mm_s_ColIndex),
                            " - : - ",
                            R.drawable.status_start));
                    else eventArrayList.add(new LVEvent(R.drawable.krovat2_b,
                            "Спала",
                            String.valueOf(Integer.valueOf(c.getString(time_HH_e_ColIndex)) -
                                    Integer.valueOf(c.getString(time_HH_s_ColIndex)))+":"+
                                    String.valueOf(Integer.valueOf(c.getString(time_mm_e_ColIndex)) -
                                    Integer.valueOf(c.getString(time_mm_s_ColIndex))),
                            c.getString(time_HH_s_ColIndex)+":"+
                                    c.getString(time_mm_s_ColIndex),
                            c.getString(time_HH_e_ColIndex)+":"+
                                    c.getString(time_mm_e_ColIndex),
                            R.drawable.status_stop));
            } while (c.moveToNext());

            c.moveToLast();
            Log.e(LOG_TAG, "Sleap status = " + c.getString(status_ColIndex));

            if (c.getString(status_ColIndex).equals("start")) {
                btnSleap.setImageResource(R.drawable.krovat2);
            } else {
                btnSleap.setImageResource(R.drawable.krovat2_w);
            }
        }
        c.close();

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://ru.sappstudio.mothershelper/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://ru.sappstudio.mothershelper/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

//
//    @Override
//    public void onClick(View v) {
//
//        // создаем объект для данных
//        ContentValues cv = new ContentValues();
//
//        // получаем данные из полей ввода
////        String name = etName.getText().toString();
////        String email = etEmail.getText().toString();
//
//        // подключаемся к БД
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//
//        switch (v.getId()) {
//            case R.id.btnAdd:
//                Log.d(LOG_TAG, "--- Insert in mytable: ---");
//                // подготовим данные для вставки в виде пар: наименование столбца - значение
//
//                cv.put("name", name);
//                cv.put("email", email);
//                // вставляем запись и получаем ее ID
//                long rowID = db.insert("mytable", null, cv);
//                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
//                break;
//            case R.id.btnRead:
//                Log.d(LOG_TAG, "--- Rows in mytable: ---");
//                // делаем запрос всех данных из таблицы mytable, получаем Cursor
//                Cursor c = db.query("mytable", null, null, null, null, null, null);
//
//                // ставим позицию курсора на первую строку выборки
//                // если в выборке нет строк, вернется false
//                if (c.moveToFirst()) {
//
//                    // определяем номера столбцов по имени в выборке
//                    int idColIndex = c.getColumnIndex("id");
//                    int nameColIndex = c.getColumnIndex("name");
//                    int emailColIndex = c.getColumnIndex("email");
//
//                    do {
//                        // получаем значения по номерам столбцов и пишем все в лог
//                        Log.d(LOG_TAG,
//                                "ID = " + c.getInt(idColIndex) +
//                                        ", name = " + c.getString(nameColIndex) +
//                                        ", email = " + c.getString(emailColIndex));
//                        // переход на следующую строку
//                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
//                    } while (c.moveToNext());
//                } else
//                    Log.d(LOG_TAG, "0 rows");
//                c.close();
//                break;
//            case R.id.btnClear:
//                Log.d(LOG_TAG, "--- Clear mytable: ---");
//                // удаляем все записи
//                int clearCount = db.delete("mytable", null, null);
//                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
//                break;
//        }
//        // закрываем подключение к БД
//        dbHelper.close();
//    }
}
