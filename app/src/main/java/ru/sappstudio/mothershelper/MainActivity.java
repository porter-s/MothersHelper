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


public class MainActivity extends Activity implements View.OnClickListener {

    final String LOG_TAG = "MainActivity";

    ImageButton btnSleap, btnFood, btnKoliki, btnWalk;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSleap = (ImageButton) findViewById(R.id.btnSleap);
        btnSleap.setOnClickListener(this);

        btnFood = (ImageButton) findViewById(R.id.btnFood);
        btnFood.setOnClickListener(this);

        btnKoliki = (ImageButton) findViewById(R.id.btnKoliki);
        btnKoliki.setOnClickListener(this);

        btnWalk = (ImageButton) findViewById(R.id.btnWalk);
        btnWalk.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("Sleap", null, null, null, null, null, null);
        int mess = c.getColumnIndex("mess");
        if(mess<0){
            dbHelper.onUpgrade(db,1,1);
            Log.e(LOG_TAG,"Error DB - new DB");
        }else Log.e(LOG_TAG,"DB done");
        db.close();
        // создаем объект для создания и управления версиями БД

        //SQLiteDatabase db = dbHelper.getWritableDatabase();

       // int k = db.getVersion();
        //dbHelper.onUpgrade(db,1,1);
        //db.close();
        //Cursor c = db.query("Sleap", null, null, null, null, null, null);
//        if (!c.moveToFirst())
//            dbHelper.onUpgrade(db,1,1);
//        db.close();
        // создаем адаптер
        lvEventAdapter = new LVEventAdapter(this, eventArrayList);
        updateLV();
        //initDataSleap();
        //initDataWalk();

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
                updateLV();
            break;

            case R.id.btnFood:
                clickFood();
                updateLV();
            break;

            case R.id.btnKoliki:
                clickKoliki();
                updateLV();
            break;

            case R.id.btnWalk:
                clickWalk();
                updateLV();
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
                    btnSleap.setImageResource(R.drawable.krovat_w);
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
                    btnSleap.setImageResource(R.drawable.krovat_w);

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
                btnSleap.setImageResource(R.drawable.krovat);
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
            btnSleap.setImageResource(R.drawable.krovat);
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

    public void clickWalk()
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
                    btnWalk.setImageResource(R.drawable.kolaska_w);
                    cv.put("time_e", unixSeconds);
                    cv.put("status", "stop");

                    // вставляем запись и получаем ее ID
                    // обновляем по id
                    Log.e(LOG_TAG, "--- Update Walk: ---");

                    int updCount = db.update("Walk", cv, "id = ?",
                            new String[] {c.getString(idColIndex)});
                    Log.e(LOG_TAG, "updated rows count = " + updCount);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Погуляли!)", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не прошло и 5 минут.\n  Отмена действия!)", Toast.LENGTH_LONG);
                    toast.show();
                    // удаляем последнюю запись
                    int clearCount = db.delete("Walk", "id = "+String.valueOf(c.getInt(idColIndex)),null);
                    Log.e(LOG_TAG, "deleted rows count = " + clearCount + " id = "+String.valueOf(c.getInt(idColIndex)));
                    btnWalk.setImageResource(R.drawable.kolaska_w);

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
                btnWalk.setImageResource(R.drawable.kolaska);
                Log.e(LOG_TAG, "--- Insert in Walk: ---");
                updateDate();
//                Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                cv.put("time_s", unixSeconds);
                cv.put("status", "start");

                // вставляем запись и получаем ее ID
                long rowID = db.insert("Walk", null, cv);
                Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Гуляем...", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Log.e(LOG_TAG, "DB = null");
            btnWalk.setImageResource(R.drawable.kolaska);
            Log.e(LOG_TAG, "--- Insert in Walk: ---");
            updateDate();
//            Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

            cv.put("time_s", unixSeconds);
            cv.put("status", "start");

            // вставляем запись и получаем ее ID
            long rowID = db.insert("Walk", null, cv);
            Log.e(LOG_TAG, "row inserted, ID = " + rowID);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Гуляем...", Toast.LENGTH_LONG);
            toast.show();
        }


        c.close();
    }

    public void clickFood()
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
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Кушаем!", Toast.LENGTH_LONG);
                    toast.show();
//                    btnFood.setImageResource(R.drawable.but_w);
                }else{
//                    btnFood.setImageResource(R.drawable.but);
                    Toast toast = Toast.makeText(getApplicationContext(),
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
                Toast toast = Toast.makeText(getApplicationContext(),
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
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Кушаем!", Toast.LENGTH_LONG);
            toast.show();
//            btnFood.setImageResource(R.drawable.but_w);
        }


        c.close();
    }

    public void clickKoliki()
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
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Колик:(", Toast.LENGTH_LONG);
                    toast.show();
//                    btnKoliki.setImageResource(R.drawable.koliki_w);
                }else{
//                    btnKoliki.setImageResource(R.drawable.koliki);
                    Toast toast = Toast.makeText(getApplicationContext(),
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
                Toast toast = Toast.makeText(getApplicationContext(),
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
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Колик:(", Toast.LENGTH_LONG);
            toast.show();
//            btnKoliki.setImageResource(R.drawable.koliki_w);
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


    public void initData(String _tableName, String _actionStart, String _actionStop, ImageButton _button, int _idImageActiv,int _idImagePassiv, int _idIconLv, int _idFonLvOn, int _idFonLvOff)    //обновляем данные
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        //загружаем Sleap//

        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query(_tableName, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if(_tableName.equals("Koliki")||_tableName.equals("Food"))
        {
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int time = c.getColumnIndex("time");
                int mess = c.getColumnIndex("mess");
                int status_ColIndex = c.getColumnIndex("status");
                updateDate();
                c.moveToLast();

                long kol = c.getLong(time);

                String _yyyy, _MM, _dd, _HH, _mm;

                if (getTimeFormat(kol, "yyyy").equals("0000")) _yyyy = "";
                else  _yyyy = getTimeFormat(kol, "yyyy");


                if (getTimeFormat(kol, "MM").equals("00")) _MM = "";
                else _MM = getTimeFormat(kol, "MM");

                if (getTimeFormat(kol, "dd").equals("00")) _dd = "";
                else _dd = getTimeFormat(kol, "dd");

                if (getTimeFormat(kol, "HH").equals("00")) _HH = "";
                else _HH = getTimeFormat(kol, "HH");

                _mm = getTimeFormat(kol, "mm") + " мин";
                if (differenceUnixSeconds(unixSeconds,c.getLong(time))<300){
                    eventArrayList.add(new LVEvent(_tableName,
                            _idIconLv, _idFonLvOn,
                            c.getString(idColIndex),
                            c.getLong(time), c.getLong(time),
                            _actionStart,
                            getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"),
                            "",
                            _dd+"."+_MM+"."+_yyyy,
                            c.getString(mess),
                            R.drawable.status_start));
                }else{
                    eventArrayList.add(new LVEvent(_tableName,
                            _idIconLv, _idFonLvOn,
                            c.getString(idColIndex),
                            c.getLong(time), c.getLong(time),
                            _actionStart,
                            getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"),
                            "",
                            _dd+"."+_MM+"."+_yyyy,
                            c.getString(mess),
                            R.drawable.status_stop));
                }

            }
        }else
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int time_s = c.getColumnIndex("time_s");
            int time_e = c.getColumnIndex("time_e");
            int mess = c.getColumnIndex("mess");
            int status_ColIndex = c.getColumnIndex("status");
            updateDate();
            // do {

            //  } while (c.moveToNext());
            c.moveToLast();
            //---------------Записываем последнюю запись-------------
            if (c.getString(status_ColIndex).equals("start")){

                long kol = differenceUnixSeconds(unixSeconds,c.getLong(time_s));

                String _yyyy,_MM,_dd,_HH,_mm;

                if (getTimeFormat(kol,"yyyy").equals("0000")) _yyyy = "";
                else if(Integer.valueOf(getTimeFormat(kol,"yyyy"))<10) _yyyy = getTimeFormat(kol,"yyyy") + " г. ";
                else _yyyy = getTimeFormat(kol,"yyyy") + " л ";

                if (getTimeFormat(kol,"MM").equals("00")) _MM = "";
                else _MM = getTimeFormat(kol,"MM") + " мес ";

                if (getTimeFormat(kol,"dd").equals("00")) _dd = "";
                else _dd = getTimeFormat(kol,"dd") + " д ";

                if (getTimeFormat(kol,"HH").equals("00")) _HH = "";
                else _HH = getTimeFormat(kol,"HH") + " ч ";

                _mm = getTimeFormat(kol,"mm")+" мин";
                eventArrayList.add(new LVEvent(_tableName,
                        _idIconLv, _idFonLvOn,
                        c.getString(idColIndex),
                        c.getLong(time_s),c.getLong(time_e),
                        _actionStart,
                        _yyyy+_MM+_dd+_HH+_mm,
                        getTimeFormat(c.getLong(time_s),"HH")+"-"+getTimeFormat(c.getLong(time_s),"mm"),
                        " - : - ",
                        c.getString(mess),
                        R.drawable.status_start));
            }
            else{
                long kol = differenceUnixSeconds(c.getLong(time_e),c.getLong(time_s));

                String _yyyy,_MM,_dd,_HH,_mm;

                if (getTimeFormat(kol,"yyyy").equals("0000")) _yyyy = "";
                else if(Integer.valueOf(getTimeFormat(kol,"yyyy"))<10) _yyyy = getTimeFormat(kol,"yyyy") + " г. ";
                else _yyyy = getTimeFormat(kol,"yyyy") + " л ";

                if (getTimeFormat(kol,"MM").equals("00")) _MM = "";
                else _MM = getTimeFormat(kol,"MM") + " мес ";

                if (getTimeFormat(kol,"dd").equals("00")) _dd = "";
                else _dd = getTimeFormat(kol,"dd") + " д ";

                if (getTimeFormat(kol,"HH").equals("00")) _HH = "";
                else _HH = getTimeFormat(kol,"HH") + " ч ";

                _mm = getTimeFormat(kol,"mm")+" мин";

                eventArrayList.add(new LVEvent(_tableName,
                        _idIconLv, _idFonLvOff,
                        c.getString(idColIndex),
                        c.getLong(time_s),c.getLong(time_e),
                        _actionStop,
                        _yyyy+_MM+_dd+_HH+_mm,
                        getTimeFormat(c.getLong(time_s),"HH")+"-"+getTimeFormat(c.getLong(time_s),"mm"),
                        getTimeFormat(c.getLong(time_e),"HH")+"-"+getTimeFormat(c.getLong(time_e),"mm"),
                        c.getString(mess),
                        R.drawable.status_stop));
            }
            //---------------------------------------------------------------

            c.moveToLast();
            Log.e(LOG_TAG, _tableName +" status = " + c.getString(status_ColIndex));

            if (c.getString(status_ColIndex).equals("start")) {
                _button.setImageResource(_idImageActiv);
            } else {
                _button.setImageResource(_idImagePassiv);
            }
        }
        c.close();

    }

    public String getTimeFormat(long _unixSeconds, String _format){

        Date _date = new Date(_unixSeconds*1000L);
        if(_format.equals("yyyy"))
            if(_unixSeconds<=31556926) return "0000";
            else return sdf_yyyy.format(_date);

        if(_format.equals("MM"))
            if(_unixSeconds<=2629743) return "00";
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
        updateLV();

    }

    void updateLV()
    {
        eventArrayList.clear();

        initData("Sleap", "Спим", "Спали", btnSleap, R.drawable.krovat, R.drawable.krovat_w, R.drawable.krovat, R.drawable.fon_lv_sleap_on, R.drawable.fon_lv_sleap_off);
        initData("Food", "Ели", "Ели", btnFood, R.drawable.but_w, R.drawable.but_w, R.drawable.but, R.drawable.fon_lv_but_on, R.drawable.fon_lv_but_off);
        initData("Koliki", "Колики", "Колики", btnKoliki, R.drawable.koliki_w, R.drawable.koliki_w, R.drawable.koliki, R.drawable.fon_lv_koliki_on, R.drawable.fon_lv_koliki_off);
        initData("Walk", "Гуляем", "Гуляли", btnWalk, R.drawable.kolaska, R.drawable.kolaska_w, R.drawable.kolaska, R.drawable.fon_lv_kolaska_on, R.drawable.fon_lv_kolaska_off);

        lvEventAdapter.notifyDataSetChanged();

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
                       updateLV();
               }
            });
        }
    }

}
