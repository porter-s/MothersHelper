package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        btnALeDatePicker = (Button) findViewById(R.id.btnALeDatePicker);

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
}
