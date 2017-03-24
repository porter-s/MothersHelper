package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 17.03.17.
 */
public class MessActivity extends Activity {

    String LOG_TAG = "MessActivity";
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);

        final Intent intent = getIntent();

        final String _tableName = intent.getStringExtra("_tableName");
        String _tvEvent = intent.getStringExtra("_tvEvent");
        final String _id = intent.getStringExtra("_id");
        String _tvLVMess = intent.getStringExtra("_tvLVMess");
        long _unixTimeSecond_s = intent.getLongExtra("_unixTimeSecond_s",0);
        long _unixTimeSecond_e = intent.getLongExtra("_unixTimeSecond_e",0);

        TextView tvAmEvent = (TextView) findViewById(R.id.tvAmEvent);
        TextView tvAmDate = (TextView) findViewById(R.id.tvAmDate);
        TextView tvAmTime_s = (TextView) findViewById(R.id.tvAmTime_s);
        TextView tvAmTime_e = (TextView) findViewById(R.id.tvAmTime_e);
        Button bAmSave = (Button) findViewById(R.id.bAmSave);
        final EditText tvAmMess = (EditText) findViewById(R.id.tvAmMess);

        TextView tvAmDo = (TextView) findViewById(R.id.tvAmDo);
        TextView tvAmS = (TextView) findViewById(R.id.tvAmS);

        tvAmMess.setText(_tvLVMess);
        tvAmEvent.setText(_tvEvent);

        if (_tableName.equals("Food")||_tableName.equals("Koliki"))
        {
            tvAmDate.setText(getTimeFormat(_unixTimeSecond_s,"dd")+"."+
                    getTimeFormat(_unixTimeSecond_s,"MM")+ "."+
                    getTimeFormat(_unixTimeSecond_s,"yyyy"));
            tvAmTime_s.setText(getTimeFormat(_unixTimeSecond_s,"HH")+":"+getTimeFormat(_unixTimeSecond_s,"mm"));
            tvAmDo.setText("");
            tvAmS.setText("в");
            tvAmTime_e.setText("");
           // tvAmTime_e.setText(getTimeFormat(_unixTimeSecond_e, "HH") + ":" + getTimeFormat(_unixTimeSecond_e, "mm"));

        }else{
            if(!getTimeFormat(_unixTimeSecond_e,"yyyy").equals("0000")) {
                if (!(getTimeFormat(_unixTimeSecond_s, "yyyy").equals(getTimeFormat(_unixTimeSecond_e, "yyyy"))) ||
                        !(getTimeFormat(_unixTimeSecond_s, "MM").equals(getTimeFormat(_unixTimeSecond_e, "MM"))) ||
                        !(getTimeFormat(_unixTimeSecond_s, "dd").equals(getTimeFormat(_unixTimeSecond_e, "dd"))))

                {
                    tvAmDate.setText("с " +
                            getTimeFormat(_unixTimeSecond_s, "dd") + "." +
                            getTimeFormat(_unixTimeSecond_s, "MM") + "." +
                            getTimeFormat(_unixTimeSecond_s, "yyyy") + " до " +
                            getTimeFormat(_unixTimeSecond_e, "dd") + "." +
                            getTimeFormat(_unixTimeSecond_e, "MM") + "." +
                            getTimeFormat(_unixTimeSecond_e, "yyyy"));

                    tvAmTime_s.setText(getTimeFormat(_unixTimeSecond_s, "HH") + ":" + getTimeFormat(_unixTimeSecond_s, "mm"));
                    tvAmTime_e.setText(getTimeFormat(_unixTimeSecond_e, "HH") + ":" + getTimeFormat(_unixTimeSecond_e, "mm"));
                } else {
                    tvAmDate.setText(getTimeFormat(_unixTimeSecond_s,"dd")+"."+
                            getTimeFormat(_unixTimeSecond_s,"MM")+ "."+
                            getTimeFormat(_unixTimeSecond_s,"yyyy"));
                    tvAmTime_s.setText(getTimeFormat(_unixTimeSecond_s,"HH")+":"+getTimeFormat(_unixTimeSecond_s,"mm"));
                    tvAmTime_e.setText(getTimeFormat(_unixTimeSecond_e, "HH") + ":" + getTimeFormat(_unixTimeSecond_e, "mm"));
                }
            }
            else {
                tvAmDate.setText(getTimeFormat(_unixTimeSecond_s,"dd")+"."+
                        getTimeFormat(_unixTimeSecond_s,"MM")+ "."+
                        getTimeFormat(_unixTimeSecond_s,"yyyy"));
                tvAmTime_s.setText(getTimeFormat(_unixTimeSecond_s,"HH")+":"+getTimeFormat(_unixTimeSecond_s,"mm"));
                tvAmTime_e.setText("-:-");
            }
        }

        bAmSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(LOG_TAG, "--- Update mytable: ---");
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
               // Cursor c = db.query("Sleap", null, null, null, null, null, null);
                ContentValues cv = new ContentValues();
                cv.put("mess", String.valueOf(tvAmMess.getText()));

                int updCount = db.update(_tableName, cv, "id = ?",
                        new String[] {_id});
                Log.e(LOG_TAG, "updated rows count = " + updCount);
                Log.e(LOG_TAG, "_tableName = "+_tableName+" id= " + _id + " text= "+tvAmMess.getText());

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Заметка сохранена!", Toast.LENGTH_LONG);
                toast.show();
                db.close();
                onBackPressed();
            }
        });



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
}
