package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 23.03.2017.
 */
public class NewDBRecord extends Activity {

    String yyyy_s, MM_s, dd_s, yyyy_e, MM_e, dd_e, mess="", tebleN="",status = "stop";
    int HH_s,mm_s,HH_e,mm_e;
    long time_s=0,time_e=0;
    String[] data = {"Сон", "Еда", "Колики", "Прогулка"};
    String LOG_TAG = "NewDBRecord";
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

    Button bnAnDBrDate_e,bnAnDBrDate_s,btAnDBrTime_s,btAnDBrTime_e,bAnDBrAdd;
    private int  mHour, mMinute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_db_record);

        Spinner sAnDBrEvent = (Spinner)findViewById(R.id.sAnDBrEvent);
        bnAnDBrDate_s = (Button)findViewById(R.id.bnAnDBrDate_s);
        btAnDBrTime_s = (Button)findViewById(R.id.btAnDBrTime_s);
        bnAnDBrDate_e = (Button)findViewById(R.id.bnAnDBrDate_e);
        btAnDBrTime_e = (Button)findViewById(R.id.btAnDBrTime_e);
        bAnDBrAdd = (Button)findViewById(R.id.bAnDBrAdd);
        final EditText etAnDBrMess = (EditText)findViewById(R.id.etAnDBrMess);

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) findViewById(R.id.sAnDBrEvent);
        spinner.setAdapter(adapter);

       // int mHour=0,mMinute=0;
        btAnDBrTime_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewDBRecord.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                HH_s = hourOfDay;
                                mm_s = minute;
                                btAnDBrTime_s.setText(hourOfDay + ":" + minute);
                                Log.e(LOG_TAG,"hourOfDay + minute ="+hourOfDay + ":" + minute);
                                //txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        btAnDBrTime_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewDBRecord.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                HH_e = hourOfDay;
                                mm_e = minute;
                                btAnDBrTime_e.setText(hourOfDay + ":" + minute);
                                Log.e(LOG_TAG,"hourOfDay + minute ="+hourOfDay + ":" + minute);
                                //txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        Intent intent = getIntent();
        final String _tableName = intent.getStringExtra("_tableName");
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
                        tebleN = "Sleap";
                        break;
                    case 1:
                        tebleN = "Food";
                        break;
                    case 2:
                        tebleN = "Koliki";
                        break;
                    case 3:
                        tebleN = "Walk";
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
                time_s = (long)Integer.valueOf(yyyy_s)*31536000 + Integer.valueOf(MM_s)*2628002 +Integer.valueOf(dd_s)*86400 +HH_s*3600 +mm_s;
                time_e = (long)Integer.valueOf(yyyy_e)*31536000 + Integer.valueOf(MM_e)*2628002 +Integer.valueOf(dd_e)*86400 +HH_e*3600 +mm_e;
                if(time_s!=0&&time_e!=0&&time_e>=time_s&&!tebleN.equals("")){
                    Toast.makeText(getBaseContext(), "Add!" , Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getBaseContext(), "Error!" , Toast.LENGTH_SHORT).show();

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
}
