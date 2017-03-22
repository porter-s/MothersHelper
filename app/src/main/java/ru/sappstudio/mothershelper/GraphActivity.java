package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USER on 22.03.2017.
 */
public class GraphActivity extends Activity {

    String LOG_TAG = "GraphActivity";
    String yyyy,MM,dd;
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
        setContentView(R.layout.activity_graph);

        long time_s_b;

        GraphView graph = (GraphView) findViewById(R.id.graph);
        String _yyyy, _MM, _dd, _HH, _mm;

        Intent intent = getIntent();
        time_s_b = intent.getLongExtra("_unixTimeSecond_s", 0);
        String _tableName = intent.getStringExtra("_tableName");

        TextView tvGraphName = (TextView) findViewById(R.id.tvGraphName);
        if (_tableName.equals("Koliki"))
            tvGraphName.setText("График Коликов (кол/день)");
        if (_tableName.equals("Food"))
            tvGraphName.setText("График Питания (кол/день)");
        if (_tableName.equals("Sleap"))
            tvGraphName.setText("График Сна (кол/день)");
        if (_tableName.equals("Walk"))
            tvGraphName.setText("График Прогулок (кол/день)");


        if (getTimeFormat(time_s_b, "yyyy").equals("0000")) yyyy = "";
        else yyyy = getTimeFormat(time_s_b, "yyyy");


        if (getTimeFormat(time_s_b, "MM").equals("00")) MM = "";
        else MM = getTimeFormat(time_s_b, "MM");

        if (getTimeFormat(time_s_b, "dd").equals("00")) dd = "";
        else dd = getTimeFormat(time_s_b, "dd");

        Button btnAGBack = (Button) findViewById(R.id.btnAGBack);
        btnAGBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        DBHelper dbHelper = new DBHelper(this);

        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DataPoint[] dataPoint = new DataPoint[31];
        // делаем запрос всех данных из таблицы sleap, получаем Cursor

        Cursor c = db.query(_tableName, null, null, null, null, null, null);
        if (_tableName.equals("Koliki") || _tableName.equals("Food")) {

            if (c.moveToFirst()) {

                int time = c.getColumnIndex("time");

                for (int i = 1; i <= 31; i++) {
                    int kol_z = 0;
                    do {
                        long kol = c.getLong(time);

                        if (getTimeFormat(kol, "yyyy").equals("0000")) _yyyy = "";
                        else _yyyy = getTimeFormat(kol, "yyyy");


                        if (getTimeFormat(kol, "MM").equals("00")) _MM = "";
                        else _MM = getTimeFormat(kol, "MM");

                        if (getTimeFormat(kol, "dd").equals("00")) _dd = "";
                        else _dd = getTimeFormat(kol, "dd");

                        if (_yyyy.equals(yyyy) && _MM.equals(MM) && i == Integer.valueOf(_dd))
                            kol_z++;

                    } while (c.moveToNext());
                    dataPoint[i - 1] = new DataPoint(i, kol_z);
                    c.moveToFirst();
                }

            }
        } else {
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int time_s = c.getColumnIndex("time_s");
                int status_ColIndex = c.getColumnIndex("status");
                int time = c.getColumnIndex("time_s");
                for (int i = 1; i <= 31; i++) {
                    int kol_z = 0;

                    do {

                        long kol = c.getLong(time);

                        if (getTimeFormat(kol, "yyyy").equals("0000")) _yyyy = "";
                        else _yyyy = getTimeFormat(kol, "yyyy");

                        if (getTimeFormat(kol, "MM").equals("00")) _MM = "";
                        else _MM = getTimeFormat(kol, "MM");

                        if (getTimeFormat(kol, "dd").equals("00")) _dd = "";
                        else _dd = getTimeFormat(kol, "dd");

                        if (getTimeFormat(c.getLong(time_s), "yyyy").equals(yyyy) &&
                                getTimeFormat(c.getLong(time_s), "MM").equals(MM) &&
                                i == Integer.valueOf(_dd) &&
                                c.getString(status_ColIndex).equals("stop"))
                            kol_z++;

                    } while (c.moveToNext());
                    dataPoint[i - 1] = new DataPoint(i, kol_z);
                    c.moveToFirst();
                }

            }
        }
    db.close();
            // dataPoint[0] = new DataPoint(0,2);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);

            if (_tableName.equals("Koliki"))
                series.setColor(Color.parseColor("#55AAFF"));
            if (_tableName.equals("Food"))
                series.setColor(Color.parseColor("#8efe59"));
            if (_tableName.equals("Sleap"))
                series.setColor(Color.parseColor("#d753f4"));
            if (_tableName.equals("Walk"))
                series.setColor(Color.parseColor("#fa7548"));

            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(5);
            Log.e(LOG_TAG, "dataPoint = " + dataPoint.length);
            c.close();

            // set manual X bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(10);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(1);
            graph.getViewport().setMaxX(31);

            // enable scaling and scrolling
            graph.getViewport().setScrollable(true); // enables horizontal scrolling
            graph.getViewport().setScrollableY(true); // enables vertical scrolling
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

            graph.addSeries(series);
    }

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
}
//    private void generateDataForGraph() {
//        int size = Amount.size();
//        values = new DataPoint[size];
//        for (int i=0; i<size; i++) {
//            Integer xi = Integer.parseInt(Dates.get(i));
//            Integer yi = Integer.parseInt(Amount.get(i));
//            DataPoint v = new DataPoint(xi, yi);
//            values[i] = v;
//        }
//        series = new LineGraphSeries<DataPoint>(values);
//        graph2.addSeries(series);
//    }