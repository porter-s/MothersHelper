package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by USER on 20.03.2017.
 */
public class ListEventActivity extends Activity{

    String LOG_TAG = "ListEventActivity";
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

    int DIALOG_DATE = 1;


    int myYear = 0;
    int myMonth = 0;
    int myDay = 0;

    Button btnALeDatePicker;
    ArrayList<LVEvent> eventArrayList = new ArrayList<LVEvent>();
    LVListEventAdapter lvListEventAdapter;
    DBHelper dbHelper;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        ListView lvAle = (ListView)findViewById(R.id.lvAle);
        btnALeDatePicker = (Button) findViewById(R.id.btnALeDatePicker);

        dbHelper = new DBHelper(this);
        lvListEventAdapter = new LVListEventAdapter(this, eventArrayList);
        updateLV();

        lvAle.setAdapter(lvListEventAdapter);


        btnALeDatePicker.setText(getTimeFormat(unixSeconds,"dd")+"."+getTimeFormat(unixSeconds,"MM")+"."+getTimeFormat(unixSeconds,"yyyy"));
        btnALeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
    }


    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, year_yyyy, month_MM-1, day_dd);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
//            myYear = year;
//            myMonth = monthOfYear+1;
//            myDay = dayOfMonth;
            //long _unixTime = year*31536000 + (monthOfYear+1)*2628002 + dayOfMonth*86400;
          //  btnALeDatePicker.setText(getTimeFormat(_unixTime,"dd")+"."+getTimeFormat(_unixTime,"MM")+"."+getTimeFormat(_unixTime,"yyyy"));
            Log.e(LOG_TAG, "Today is " + myDay + "/" + myMonth + "/" + myYear);
        }
    };

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

    public void initData(String _tableName, String _actionStart, String _actionStop, ImageButton _button, int _idImageActiv, int _idImagePassiv, int _idIconLv, int _idFonLvOn, int _idFonLvOff)    //обновляем данные
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(db);

        //загружаем Sleap//

        // делаем запрос всех данных из таблицы sleap, получаем Cursor
        Cursor c = db.query(_tableName, null, null, null, null, null, null);


        if(_tableName.equals("Koliki")||_tableName.equals("Food")) {
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int time = c.getColumnIndex("time");
                int mess = c.getColumnIndex("mess");
                // int status_ColIndex = c.getColumnIndex("status");

                do {
                    long kol = c.getLong(time);

                    String _yyyy, _MM, _dd, _HH, _mm;

                    if (getTimeFormat(kol, "yyyy").equals("0000")) _yyyy = "";
                    else _yyyy = getTimeFormat(kol, "yyyy");


                    if (getTimeFormat(kol, "MM").equals("00")) _MM = "";
                    else _MM = getTimeFormat(kol, "MM");

                    if (getTimeFormat(kol, "dd").equals("00")) _dd = "";
                    else _dd = getTimeFormat(kol, "dd");

                    if (getTimeFormat(kol, "HH").equals("00")) _HH = "";
                    else _HH = getTimeFormat(kol, "HH");

                    _mm = getTimeFormat(kol, "mm") + " мин";
                    eventArrayList.add(new LVEvent(_tableName,
                            _idIconLv, _idFonLvOn,
                            c.getString(idColIndex),
                            c.getLong(time), c.getLong(time),
                            _actionStart,
                            getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"),
                            "",
                            _dd + "." + _MM + "." + _yyyy,
                            c.getString(mess),
                            R.drawable.status_stop));
                } while (c.moveToNext());
            }
        }
        c.close();
    }
    void updateLV()
    {
        eventArrayList.clear();

        //initData("Sleap", "Спим", "Спали", null, R.drawable.krovat, R.drawable.krovat_w, R.drawable.krovat, R.drawable.fon_lv_sleap_on, R.drawable.fon_lv_sleap_off);
        //initData("Food", "Ели", "Ели", null, R.drawable.but_w, R.drawable.but_w, R.drawable.but, R.drawable.fon_lv_but_on, R.drawable.fon_lv_but_off);
        initData("Koliki", "Колики", "Колики", null, R.drawable.koliki_w, R.drawable.koliki_w, R.drawable.koliki, R.drawable.fon_lv_koliki_on, R.drawable.fon_lv_koliki_off);
       // initData("Walk", "Гуляем", "Гуляли", null, R.drawable.kolaska, R.drawable.kolaska_w, R.drawable.kolaska, R.drawable.fon_lv_kolaska_on, R.drawable.fon_lv_kolaska_off);

        lvListEventAdapter.notifyDataSetChanged();

    }
}
