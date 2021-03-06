package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 23.03.2017.
 */
public class NewDBRecordActivity extends Activity {

    String yyyy_s, MM_s, dd_s, yyyy_e, MM_e, dd_e, mess="", tableN="",status = "stop";
    String HH_s,mm_s,HH_e,mm_e;
    long time_s=0,time_e=0;
    String[] data = {"Сон", "Еда", "Колики", "Прогулка"};
    String LOG_TAG = "NewDBRecordActivity";
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

    int DIALOG_DATE_S = 1, DIALOG_DATE_E = 2;


    int myYear = 0;
    int myMonth = 0;
    int myDay = 0;

    Button bnAnDBrDate_e,bnAnDBrDate_s,btAnDBrTime_s,btAnDBrTime_e,bAnDBrAdd,bAnDBrBack;
    private int  mHour, mMinute;
//-----------------------------------------------------------------------------------------//
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_db_record);

        Spinner sAnDBrEvent = (Spinner)findViewById(R.id.sAnDBrEvent);
        bnAnDBrDate_s = (Button)findViewById(R.id.bnAnDBrDate_s);
        btAnDBrTime_s = (Button)findViewById(R.id.btAnDBrTime_s);
        bnAnDBrDate_e = (Button)findViewById(R.id.bnAnDBrDate_e);
        btAnDBrTime_e = (Button)findViewById(R.id.btAnDBrTime_e);
        bAnDBrAdd = (Button)findViewById(R.id.bAnDBrAdd);
        bAnDBrBack = (Button)findViewById(R.id.bAnDBrBack);
        final EditText etAnDBrMess = (EditText)findViewById(R.id.etAnDBrMess);
        Intent intent = getIntent();
        final String _tableName = intent.getStringExtra("_tableName");

        yyyy_s=getTimeFormat(unixSeconds,"yyyy");
        MM_s=getTimeFormat(unixSeconds,"MM");
        dd_s=getTimeFormat(unixSeconds,"dd");
        HH_s=getTimeFormat(unixSeconds,"HH");
        mm_s=getTimeFormat(unixSeconds,"mm");
        yyyy_e=getTimeFormat(unixSeconds,"yyyy");
        MM_e=getTimeFormat(unixSeconds,"MM");
        dd_e=getTimeFormat(unixSeconds,"dd");
        HH_e=getTimeFormat(unixSeconds,"HH");
        mm_e=getTimeFormat(unixSeconds,"mm");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) findViewById(R.id.sAnDBrEvent);
        spinner.setAdapter(adapter);

       // int mHour=0,mMinute=0;
        btAnDBrTime_s.setText(HH_s+":"+mm_s);
        btAnDBrTime_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewDBRecordActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay<10)  HH_s = "0"+String.valueOf(hourOfDay);
                                else HH_s = String.valueOf(hourOfDay);
                                if(minute<10)  mm_s = "0"+String.valueOf(minute);
                                else mm_s = String.valueOf(minute);

                                if(Integer.valueOf(HH_e)<hourOfDay)
                                    if(Integer.valueOf(mm_e)<minute)
                                    {
                                        if(hourOfDay<10)  HH_e = "0"+String.valueOf(hourOfDay);
                                        else HH_e = String.valueOf(hourOfDay);

                                        if(minute+5<10)  mm_e = "0"+String.valueOf(minute+5);
                                        else mm_e = String.valueOf(minute+5);


                                    }
                                if(tableN.equals("Koliki")||tableN.equals("Food"))
                                {
                                    HH_e = HH_s;
                                    mm_e = mm_s;
                                }
                                btAnDBrTime_e.setText(HH_e + ":" + mm_e);
                                btAnDBrTime_s.setText(HH_s + ":" + mm_s);
                                Log.e(LOG_TAG,"hourOfDay + minute ="+hourOfDay + ":" + minute);
                                //txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        btAnDBrTime_e.setText(HH_e+":"+mm_e);
        btAnDBrTime_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewDBRecordActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay<10)  HH_e = "0"+String.valueOf(hourOfDay);
                                else HH_e = String.valueOf(hourOfDay);
                                if(minute<10)  mm_e = "0"+String.valueOf(minute);
                                else mm_e = String.valueOf(minute);

                                btAnDBrTime_e.setText(HH_e + ":" + mm_e);
                                Log.e(LOG_TAG,"hourOfDay + minute ="+hourOfDay + ":" + minute);
                                //txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        switch(_tableName){
            case "Sleap":
                // заголовок
                spinner.setPrompt("Сон");
                // выделяем элемент
                spinner.setSelection(0);
                // устанавливаем обработчик нажатия
                break;
            case "Food":
                // заголовок
                spinner.setPrompt("Еда");
                // выделяем элемент
                spinner.setSelection(1);
                // устанавливаем обработчик нажатия
                break;
            case "Koliki":
                // заголовок
                spinner.setPrompt("Колики");
                // выделяем элемент
                spinner.setSelection(2);
                // устанавливаем обработчик нажатия
                break;
            case "Walk":
                // заголовок
                spinner.setPrompt("Прогулка");
                // выделяем элемент
                spinner.setSelection(3);
                // устанавливаем обработчик нажатия
                break;

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                switch(position){
                    case 0:
                        tableN = "Sleap";
                        break;
                    case 1:
                        tableN = "Food";
                        break;
                    case 2:
                        tableN = "Koliki";
                        break;
                    case 3:
                        tableN = "Walk";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        bnAnDBrDate_s.setText(getTimeFormat(unixSeconds, "dd") + "." + getTimeFormat(unixSeconds, "MM") + "." + getTimeFormat(unixSeconds, "yyyy"));
        bnAnDBrDate_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_S);
            }
        });

        bnAnDBrDate_e.setText(getTimeFormat(unixSeconds, "dd") + "." + getTimeFormat(unixSeconds, "MM") + "." + getTimeFormat(unixSeconds, "yyyy"));
        bnAnDBrDate_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_E);

            }
        });

        bAnDBrAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mess = String.valueOf(etAnDBrMess.getText());
                //time_s = new SimpleDateFormat(dd_s+"/"+MM_s+"/"+yyyy_s+"");
                Date dTemp = null;
                try {
                    dTemp  =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dd_s+"/"+MM_s+"/"+yyyy_s+" "+HH_s+":"+mm_s+":00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e(LOG_TAG,"dTemp="+String.valueOf(dTemp.getTime()/1000));
                time_s = dTemp.getTime()/1000 ;

                try {
                    dTemp  =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dd_e+"/"+MM_e+"/"+yyyy_e+" "+HH_e+":"+mm_e+":00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e(LOG_TAG,"dTemp="+String.valueOf(dTemp.getTime()/1000));
                time_e = dTemp.getTime()/1000 ;

                Log.e(LOG_TAG,"yyyy_s="+yyyy_s+" MM_s="+MM_s+" dd_s="+dd_s);
                Log.e(LOG_TAG,"time_s="+time_s+" time_e="+time_e+" unixs="+unixSeconds);
                if(time_s!=0&&time_e!=0&&time_e>=time_s&&!tableN.equals("")){


                    DBHelper dbHelper = new DBHelper(NewDBRecordActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor c = db.query(tableN, null, null, null, null, null, null);

                    int idColIndex = c.getColumnIndex("id");
                    ContentValues cv = new ContentValues();
                    if(tableN.equals("Koliki")||tableN.equals("Food"))
                    {
                        cv.put("time", time_s);
                        cv.put("mess", mess);
                        cv.put("status", "stop");
                    }else
                    {
                        cv.put("time_s", time_s);
                        cv.put("time_e", time_e);
                        cv.put("mess", mess);
                        cv.put("status", "stop");
                    }

                    // вставляем запись и получаем ее ID
                    // обновляем по id
                    Log.e(LOG_TAG, "--- Update mytable: ---");

                    long rowID = db.insert(tableN, null, cv);

                    db.close();

                    Toast.makeText(getBaseContext(), "Запись добавлена! " + tableN + " kol= "+rowID, Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getBaseContext(), "Error!" , Toast.LENGTH_SHORT).show();

            }
        });
        bAnDBrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE_S) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack_s, year_yyyy, month_MM - 1, day_dd);
            return tpd;
        }
        if (id == DIALOG_DATE_E) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack_e, year_yyyy, month_MM - 1, day_dd);
            return tpd;
        }
        return super.onCreateDialog(id);
    }



    DatePickerDialog.OnDateSetListener myCallBack_s = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;

            yyyy_s = String.valueOf(myYear);

            if (myMonth < 10) MM_s = "0" + String.valueOf(myMonth);
            else MM_s = String.valueOf(myMonth);

            if (myDay < 10) dd_s = "0" + String.valueOf(myDay);
            else dd_s = String.valueOf(myDay);

            yyyy_e = yyyy_s;
            MM_e=MM_s;
            dd_e=dd_s;

            bnAnDBrDate_e.setText(dd_e + "." + MM_e + "." + yyyy_e);

            bnAnDBrDate_s.setText(dd_s + "." + MM_s + "." + yyyy_s);
            Log.e(LOG_TAG, "Today is " + myDay + "/" + myMonth + "/" + myYear);
        }
    };

    DatePickerDialog.OnDateSetListener myCallBack_e = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;

            yyyy_e = String.valueOf(myYear);

            if (myMonth < 10) MM_e = "0" + String.valueOf(myMonth);
            else MM_e = String.valueOf(myMonth);

            if (myDay < 10) dd_e = "0" + String.valueOf(myDay);
            else dd_e = String.valueOf(myDay);

            bnAnDBrDate_e.setText(dd_e + "." + MM_e + "." + yyyy_e);
            Log.e(LOG_TAG, "Today is " + myDay + "/" + myMonth + "/" + myYear);
        }
    };

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
}
