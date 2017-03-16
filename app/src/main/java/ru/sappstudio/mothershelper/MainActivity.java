package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity implements View.OnClickListener {

    final String LOG_TAG = "MainActivity";

    ImageButton btnSleap, btnFood, btnGame, btnWalk;

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

    ArrayList<LVEvent> eventArrayList = new ArrayList<LVEvent>();
    LVEventAdapter lvEventAdapter;

    private Timer timer = new Timer();;
    private TimerListUpdate timerListUpdate = new TimerListUpdate();
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

        timer.scheduleAtFixedRate(timerListUpdate,0,10000);
        //long unixTime = System.currentTimeMillis() / 1000L;

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
            int time_s = c.getColumnIndex("time_s");
            int time_e = c.getColumnIndex("time_e");
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
                Log.e(LOG_TAG,"Расчет = "+String.valueOf(unixSeconds - c.getLong(time_s)));

                if(unixSeconds - c.getLong(time_s)>300)
                {
                    btnSleap.setImageResource(R.drawable.krovat2_w);
                    cv.put("time_e", unixSeconds);
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
                                        ", time_s = " + c.getLong(time_s) +
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

                cv.put("time_s", unixSeconds);
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

            cv.put("time_s", unixSeconds);
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
        unixSeconds = System.currentTimeMillis() / 1000L; // секунды
        date = new Date(unixSeconds*1000L); // *1000 получаем миллисекунды

        year_yyyy = Integer.valueOf(sdf_yyyy.format(date));
        month_MM = Integer.valueOf(sdf_MM.format(date));
        day_dd = Integer.valueOf(sdf_dd.format(date));
        time_HH = Integer.valueOf(sdf_HH.format(date));
        time_mm = Integer.valueOf(sdf_mm.format(date));
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
            int time_s = c.getColumnIndex("time_s");
            int time_e = c.getColumnIndex("time_e");
            int status_ColIndex = c.getColumnIndex("status");
            updateDate();
            do {
                //if((month_MM ==c.getInt(month_MM_s_ColIndex))&&(day_dd==c.getInt(day_dd_s_ColIndex)))
                    if (c.getString(status_ColIndex).equals("start")){

                        long kol = differenceUnixSeconds(unixSeconds,c.getLong(time_s));

                        String _yyyy,_MM,_dd,_HH,_mm;

                        if (getTimeFormat(kol,"yyyy").equals("0000")) _yyyy = "";
                        else if(Integer.valueOf(getTimeFormat(kol,"yyyy"))<10) _yyyy = getTimeFormat(kol,"yyyy") + " г. ";
                            else _yyyy = getTimeFormat(kol,"yyyy") + " л. ";

                        if (getTimeFormat(kol,"MM").equals("00")) _MM = "";
                            else _MM = getTimeFormat(kol,"MM") + " м. ";

                        if (getTimeFormat(kol,"dd").equals("00")) _dd = "";
                            else _dd = getTimeFormat(kol,"dd") + " д. ";

                        if (getTimeFormat(kol,"HH").equals("00")) _HH = "";
                            else _HH = getTimeFormat(kol,"HH") + " ч. ";

                        _mm = getTimeFormat(kol,"mm")+" м.";
                        eventArrayList.add(new LVEvent(R.drawable.krovat2_b,
                                "Cпит",
                               _yyyy+_MM+_dd+_HH+_mm,
                                getTimeFormat(c.getLong(time_s),"HH")+"-"+getTimeFormat(c.getLong(time_s),"mm"),
                                " - : - ",
                                R.drawable.status_start));
                    }
                    else{
                        long kol = differenceUnixSeconds(c.getLong(time_e),c.getLong(time_s));

                        String _yyyy,_MM,_dd,_HH,_mm;

                        if (getTimeFormat(kol,"yyyy").equals("0000")) _yyyy = "";
                        else if(Integer.valueOf(getTimeFormat(kol,"yyyy"))<10) _yyyy = getTimeFormat(kol,"yyyy") + " г. ";
                        else _yyyy = getTimeFormat(kol,"yyyy") + " л. ";

                        if (getTimeFormat(kol,"MM").equals("00")) _MM = "";
                        else _MM = getTimeFormat(kol,"MM") + " м. ";

                        if (getTimeFormat(kol,"dd").equals("00")) _dd = "";
                        else _dd = getTimeFormat(kol,"dd") + " д. ";

                        if (getTimeFormat(kol,"HH").equals("00")) _HH = "";
                        else _HH = getTimeFormat(kol,"HH") + " ч. ";

                        _mm = getTimeFormat(kol,"mm")+" м.";

                        eventArrayList.add(new LVEvent(R.drawable.krovat2_b,
                                "Спала",
                                _yyyy+_MM+_dd+_HH+_mm,
                                getTimeFormat(c.getLong(time_s),"HH")+"-"+getTimeFormat(c.getLong(time_s),"mm"),
                                getTimeFormat(c.getLong(time_e),"HH")+"-"+getTimeFormat(c.getLong(time_e),"mm"),
                                R.drawable.status_stop));
                    }
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

    } //fdzg

    String getTimeFormat(long _unixSeconds, String _format){

        Date _date = new Date(_unixSeconds*1000L);
        if(_format.equals("yyyy"))
            if(_unixSeconds<=31536000) return "0000";
            else return sdf_yyyy.format(_date);

        if(_format.equals("MM"))
            if(_unixSeconds<=2628002) return "00";
            else return sdf_MM.format(_date);

        if(_format.equals("dd"))
            if(_unixSeconds<=86400) return "00";
            else return sdf_dd.format(_date);

        if(_format.equals("HH"))
            if(_unixSeconds<=3600) return "00";
            else if(_unixSeconds<86400) return String.valueOf(_unixSeconds/3600);
                        else return sdf_HH.format(_date);

        if(_format.equals("mm"))
            return sdf_mm.format(_date);

        return null;
    }

    long differenceUnixSeconds(long _a,long _b)
    {
        return _a-_b;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    class TimerListUpdate extends TimerTask{

        @Override
        public void run() {
            Log.e(LOG_TAG,"TimerRun");

            runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   initData();
                   lvEventAdapter.notifyDataSetChanged();
               }
            });
        }
    }
}
