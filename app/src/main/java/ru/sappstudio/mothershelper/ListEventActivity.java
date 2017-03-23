package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by USER on 20.03.2017.
 */
public class ListEventActivity extends Activity {

    String yyyy, MM, dd;
    String LOG_TAG = "ListEventActivity";
    long unixSeconds = System.currentTimeMillis() / 1000L; // секунды
    Date date = new Date(unixSeconds * 1000L); // *1000 получаем миллисекунды

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
    CheckBox cbSleap, cbFood, cbKoliki, cbWalk;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        yyyy = getTimeFormat(unixSeconds, "yyyy");
        MM = getTimeFormat(unixSeconds, "MM");
        dd = getTimeFormat(unixSeconds, "dd");

        ListView lvAle = (ListView) findViewById(R.id.lvAle);
        btnALeDatePicker = (Button) findViewById(R.id.btnALeDatePicker);
        cbSleap = (CheckBox) findViewById(R.id.cbSleap);
        cbFood = (CheckBox) findViewById(R.id.cbFood);
        cbKoliki = (CheckBox) findViewById(R.id.cbKoliki);
        cbWalk = (CheckBox) findViewById(R.id.cbWalk);
        Button btnALeBack = (Button) findViewById(R.id.btnALeBack);
        btnALeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        if (intent.getStringExtra("_tableName").equals("Sleap")) cbSleap.setChecked(true);
        if (intent.getStringExtra("_tableName").equals("Food")) cbFood.setChecked(true);
        if (intent.getStringExtra("_tableName").equals("Koliki")) cbKoliki.setChecked(true);
        if (intent.getStringExtra("_tableName").equals("Walk")) cbWalk.setChecked(true);

        dbHelper = new DBHelper(this);
        lvListEventAdapter = new LVListEventAdapter(this, eventArrayList);
        updateLV();

        lvAle.setAdapter(lvListEventAdapter);


        btnALeDatePicker.setText(getTimeFormat(unixSeconds, "dd") + "." + getTimeFormat(unixSeconds, "MM") + "." + getTimeFormat(unixSeconds, "yyyy"));
        btnALeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });

        cbSleap.setOnCheckedChangeListener(CheckChangecbSleap);
        cbFood.setOnCheckedChangeListener(CheckChangecbFood);
        cbKoliki.setOnCheckedChangeListener(CheckChangecbKoliki);
        cbWalk.setOnCheckedChangeListener(CheckChangecbWalk);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    CompoundButton.OnCheckedChangeListener CheckChangecbSleap = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) cbSleap.setChecked(true);
            updateLV();
        }
    };
    CompoundButton.OnCheckedChangeListener CheckChangecbFood = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) cbFood.setChecked(true);
            updateLV();
        }
    };
    CompoundButton.OnCheckedChangeListener CheckChangecbKoliki = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) cbKoliki.setChecked(true);
            updateLV();
        }
    };
    CompoundButton.OnCheckedChangeListener CheckChangecbWalk = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) cbWalk.setChecked(true);
            updateLV();
        }
    };


    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, year_yyyy, month_MM - 1, day_dd);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;

            yyyy = String.valueOf(myYear);

            if (myMonth < 10) MM = "0" + String.valueOf(myMonth);
            else MM = String.valueOf(myMonth);

            if (myDay < 10) dd = "0" + String.valueOf(myDay);
            else dd = String.valueOf(myDay);

            btnALeDatePicker.setText(dd + "." + MM + "." + yyyy);
            updateLV();
            Log.e(LOG_TAG, "Today is " + myDay + "/" + myMonth + "/" + myYear);
        }
    };

    String getTimeFormat(long _unixSeconds, String _format) {

        Date _date = new Date(_unixSeconds * 1000L);
        if (_format.equals("yyyy"))
            if (_unixSeconds <= 31536000) return "0000";
            else return sdf_yyyy.format(_date);

        if (_format.equals("MM"))
            if (_unixSeconds <= 2628002) return "00";
            else return sdf_MM.format(_date);

        if (_format.equals("dd"))
            if (_unixSeconds <= 86400) return "00";
            else return sdf_dd.format(_date);

        if (_format.equals("HH"))
            if (_unixSeconds <= 3600) return "00";
            else if (_unixSeconds < 86400) return String.valueOf(_unixSeconds / 3600);
            else return sdf_HH.format(_date);

        if (_format.equals("mm"))
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


        if (_tableName.equals("Koliki") || _tableName.equals("Food")) {
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

                    _mm = getTimeFormat(kol, "mm") + "";
                    if (_yyyy.equals(yyyy) && _MM.equals(MM) && _dd.equals(dd))
                        eventArrayList.add(new LVEvent(_tableName,
                                _idIconLv, _idFonLvOn,
                                c.getString(idColIndex),
                                c.getLong(time), c.getLong(time),
                                _actionStart,
                                getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"),
                                getTimeFormat(c.getLong(time), "HH") + "-" + getTimeFormat(c.getLong(time), "mm"), // в время
                                "", //_dd + "." + _MM + "." + _yyyy,
                                c.getString(mess),
                                R.drawable.status_stop));
                } while (c.moveToNext());
            }
        } else {
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int time_s = c.getColumnIndex("time_s");
                int time_e = c.getColumnIndex("time_e");
                int mess = c.getColumnIndex("mess");
                int status_ColIndex = c.getColumnIndex("status");

                do {

                    long kol = differenceUnixSeconds(c.getLong(time_e), c.getLong(time_s));

                    String _yyyy, _MM, _dd, _HH, _mm;

                    if (getTimeFormat(kol, "yyyy").equals("0000")) _yyyy = "";
                    else _yyyy = getTimeFormat(kol, "yyyy") + "л.";

                    if (getTimeFormat(kol, "MM").equals("00")) _MM = "";
                    else _MM = getTimeFormat(kol, "MM") + "м.";

                    if (getTimeFormat(kol, "dd").equals("00")) _dd = "";
                    else _dd = getTimeFormat(kol, "dd") + "д. ";

                    if (getTimeFormat(kol, "HH").equals("00")) _HH = "";
                    else _HH = getTimeFormat(kol, "HH") + ":";

                    _mm = getTimeFormat(kol, "mm");

                    if (getTimeFormat(c.getLong(time_s), "yyyy").equals(yyyy) &&
                            getTimeFormat(c.getLong(time_s), "MM").equals(MM) &&
                            getTimeFormat(c.getLong(time_s), "dd").equals(dd) &&
                            c.getString(status_ColIndex).equals("stop"))
                        eventArrayList.add(new LVEvent(_tableName,
                                _idIconLv, _idFonLvOff,
                                c.getString(idColIndex),
                                c.getLong(time_s), c.getLong(time_e),
                                _actionStop,
                                _yyyy + _MM + _dd + _HH + _mm,
                                getTimeFormat(c.getLong(time_s), "HH") + "-" + getTimeFormat(c.getLong(time_s), "mm"),
                                getTimeFormat(c.getLong(time_e), "HH") + "-" + getTimeFormat(c.getLong(time_e), "mm"),
                                c.getString(mess),
                                R.drawable.status_stop));
                } while (c.moveToNext());
            }
        }
        c.close();
    }

    long differenceUnixSeconds(long _a, long _b) {
        return _a - _b;
    }

    void updateLV() {
        eventArrayList.clear();
        if (cbSleap.isChecked())
            initData("Sleap", "Спим", "Спали", null, R.drawable.krovat, R.drawable.krovat_w, R.drawable.krovat, R.drawable.fon_lv_sleap_on, R.drawable.fon_lv_sleap_off);
        if (cbFood.isChecked())
            initData("Food", "Ели", "Ели", null, R.drawable.but_w, R.drawable.but_w, R.drawable.but, R.drawable.fon_lv_but_on, R.drawable.fon_lv_but_off);
        if (cbKoliki.isChecked())
            initData("Koliki", "Колики", "Колики", null, R.drawable.koliki_w, R.drawable.koliki_w, R.drawable.koliki, R.drawable.fon_lv_koliki_on, R.drawable.fon_lv_koliki_off);
        if (cbWalk.isChecked())
            initData("Walk", "Гуляем", "Гуляли", null, R.drawable.kolaska, R.drawable.kolaska_w, R.drawable.kolaska, R.drawable.fon_lv_kolaska_on, R.drawable.fon_lv_kolaska_off);


        // TreeSet
//        ArrayList<LVEvent> sortedSet = new TreeSet<LVEvent>(new Comparator<LVEvent>() {
//            public int compare(eventArrayList.get().tvStarEvent, ObjectName o2) {
//                return o1.toString().compareTo(o2.toString());
//            }
//        });
        //ArrayList<LVEvent> sortedSet = new ArrayList<LVEvent>();

        // List<LVEvent> list = new ArrayList<LVEvent>();

        Collections.sort(eventArrayList, new Comparator<LVEvent>() {
            public int compare(LVEvent o1, LVEvent o2) {
                return o1.sortIdTimeS.compareTo(o2.sortIdTimeS);
            }
        });
        //  sortedSet.addAll(unsortedSet);
        lvListEventAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        updateLV();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ru.sappstudio.mothershelper/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ru.sappstudio.mothershelper/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
