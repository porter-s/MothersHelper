package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 24.03.2017.
 */
public class ChDBActivity extends Activity {

    String yyyy_s, MM_s, dd_s, yyyy_e, MM_e, dd_e, mess="", tableN="",status = "stop";
    String HH_s,mm_s,HH_e,mm_e,id_t;
    long time_s=0,time_e=0, _unixTimeSecond_s, _unixTimeSecond_e;
    String[] data = {"Сон", "Еда", "Колики", "Прогулка"};
    String LOG_TAG = "ChDBActivity";
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

    Button bAchDBDate_e,bAchDBDate_s,bAchDBTime_s,bAchDBTime_e,bAchDBCh,bAchDBBack,bAchDBDelete;
    private int  mHour, mMinute;
//-----------------------------------------------------------------------------------------//

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_db);
        //Spinner sAnDBrEvent = (Spinner)findViewById(R.id.sAnDBrEvent);
        TextView tvAchDBTableName = (TextView)findViewById(R.id.tvAchDBTableName);
        bAchDBDate_s = (Button)findViewById(R.id.bAchDBDate_s);
        bAchDBTime_s = (Button)findViewById(R.id.bAchDBTime_s);
        bAchDBDate_e = (Button)findViewById(R.id.bAchDBDate_e);
        bAchDBTime_e = (Button)findViewById(R.id.bAchDBTime_e);
        bAchDBCh = (Button)findViewById(R.id.bAchDBCh);
        bAchDBBack = (Button)findViewById(R.id.bAchDBBack);
        bAchDBDelete =(Button)findViewById(R.id.bAchDBDelete);
        final EditText etAchDBMess = (EditText)findViewById(R.id.etAchDBMess);
        Intent intent = getIntent();
        final String _tableName = intent.getStringExtra("_tableName");


        id_t = intent.getStringExtra("_id");
        mess = intent.getStringExtra("_tvLVMess");
        _unixTimeSecond_s = intent.getLongExtra("_unixTimeSecond_s",0);
        _unixTimeSecond_e = intent.getLongExtra("_unixTimeSecond_e",0);
        yyyy_s=getTimeFormat(_unixTimeSecond_s,"yyyy");
        MM_s=getTimeFormat(_unixTimeSecond_s,"MM");
        dd_s=getTimeFormat(_unixTimeSecond_s,"dd");
        HH_s=getTimeFormat(_unixTimeSecond_s,"HH");
        mm_s=getTimeFormat(_unixTimeSecond_s,"mm");
        yyyy_e=getTimeFormat(_unixTimeSecond_e,"yyyy");
        MM_e=getTimeFormat(_unixTimeSecond_e,"MM");
        dd_e=getTimeFormat(_unixTimeSecond_e,"dd");
        HH_e=getTimeFormat(_unixTimeSecond_e,"HH");
        mm_e=getTimeFormat(_unixTimeSecond_e,"mm");

        etAchDBMess.setText(mess);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        final Spinner spinner = (Spinner) findViewById(R.id.sAnDBrEvent);
//        spinner.setAdapter(adapter);

        // int mHour=0,mMinute=0;
        bAchDBTime_s.setText(HH_s+":"+mm_s);
        bAchDBTime_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChDBActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay<10)  HH_s = "0"+String.valueOf(hourOfDay);
                                else HH_s = String.valueOf(hourOfDay);
                                if(minute<10)  mm_s = "0"+String.valueOf(minute);
                                else mm_s = String.valueOf(minute);

                                bAchDBTime_s.setText(HH_s + ":" + mm_s);
                                Log.e(LOG_TAG,"hourOfDay + minute ="+hourOfDay + ":" + minute);
                                //txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        bAchDBTime_e.setText(HH_e+":"+mm_e);
        bAchDBTime_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChDBActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay<10)  HH_e = "0"+String.valueOf(hourOfDay);
                                else HH_e = String.valueOf(hourOfDay);
                                if(minute<10)  mm_e = "0"+String.valueOf(minute);
                                else mm_e = String.valueOf(minute);

                                bAchDBTime_e.setText(HH_e + ":" + mm_e);
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
                tvAchDBTableName.setText("Сон");

                break;
            case "Food":
                // заголовок
                tvAchDBTableName.setText("Еда");
                break;
            case "Koliki":
                // заголовок
                tvAchDBTableName.setText("Колики");

                break;
            case "Walk":
                // заголовок
                tvAchDBTableName.setText("Прогулка");
                break;

        }


        tableN = _tableName;

        bAchDBDate_s.setText(getTimeFormat(unixSeconds, "dd") + "." + getTimeFormat(unixSeconds, "MM") + "." + getTimeFormat(unixSeconds, "yyyy"));
        bAchDBDate_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_S);
            }
        });

        bAchDBDate_e.setText(getTimeFormat(unixSeconds, "dd") + "." + getTimeFormat(unixSeconds, "MM") + "." + getTimeFormat(unixSeconds, "yyyy"));
        bAchDBDate_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_E);

            }
        });

        bAchDBCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mess = String.valueOf(etAchDBMess.getText());
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


                    DBHelper dbHelper = new DBHelper(ChDBActivity.this);
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
                    int updCount = db.update(tableN, cv, "id = ?",
                            new String[] {id_t});

                    db.close();

                    Toast.makeText(getBaseContext(), "Запись изменена! ", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else Toast.makeText(getBaseContext(), "Error!" , Toast.LENGTH_SHORT).show();

            }
        });
        bAchDBBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bAchDBDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ChDBActivity.this);

                // set title
                alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Удалить запись?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                DBHelper dbHelper = new DBHelper(ChDBActivity.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                //Cursor c = db.query(tableN, null, null, null, null, null, null);

                                //int idColIndex = c.getColumnIndex("id");
                                Log.e(LOG_TAG,"id_t = "+id_t);
                                int clearCount = db.delete(tableN, "id = "+id_t,null);
                                db.close();
                                Toast.makeText(getBaseContext(), "Запись удалена! ", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing

                                dialog.cancel();


                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                //onBackPressed();
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

            bAchDBDate_s.setText(dd_s + "." + MM_s + "." + yyyy_s);
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

            bAchDBDate_e.setText(dd_e + "." + MM_e + "." + yyyy_e);
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
