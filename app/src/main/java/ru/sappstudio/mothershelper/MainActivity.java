package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {

    final String LOG_TAG = "MainActivity";

    ImageButton btnSleap, btnFood, btnGame, btnWalk;

    DBHelper dbHelper;

    SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
    String year_yyyy = sdf_yyyy.format(new Date());

    SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
    String month_MM = sdf_MM.format(new Date());

    SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
    String day_dd = sdf_dd.format(new Date());

    SimpleDateFormat sdf_HHmm = new SimpleDateFormat("HHmm");
    String time_HHmm = sdf_HHmm.format(new Date());
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnSleap:

                Log.e(LOG_TAG, "--- Rows in sleap: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("sleap", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int year_yyyy_ColIndex = c.getColumnIndex("year_yyyy");
                    int month_MM_ColIndex = c.getColumnIndex("month_MM");
                    int day_dd_ColIndex = c.getColumnIndex("day_dd");
                    int time_HHmm_ColIndex = c.getColumnIndex("time_HHmm");
                    int status_ColIndex = c.getColumnIndex("status");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", year_yyyy = " + c.getString(year_yyyy_ColIndex) +
                                        ", month_MM = " + c.getString(month_MM_ColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else {
                    Log.d(LOG_TAG, "DB = null");
                    btnSleap.setImageResource(R.drawable.krovat2);
                    Log.e(LOG_TAG, "--- Insert in sleap: ---");
                    updateDate();
                    Log.e(LOG_TAG, "год= " + year_yyyy + " месяц ="+month_MM+" число= "+day_dd + " HHmm= "+time_HHmm);

                    cv.put("year", year_yyyy);
                    cv.put("month_MM", month_MM);
                    cv.put("time_dd", day_dd);
                    cv.put("time_HHmm", time_HHmm);
                    cv.put("status", "start");

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("Sleap", null, cv);
                    Log.e(LOG_TAG, "row inserted, ID = " + rowID);
                }

                c.close();
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

    public void updateDate()    //обновляем дату(и время)
    {
        year_yyyy = sdf_yyyy.format(new Date());
        month_MM = sdf_MM.format(new Date());
        day_dd = sdf_dd.format(new Date());
        time_HHmm = sdf_HHmm.format(new Date());
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
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
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ru.sappstudio.mothershelper/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

//
//    @Override
//    public void onClick(View v) {
//
//        // создаем объект для данных
//        ContentValues cv = new ContentValues();
//
//        // получаем данные из полей ввода
////        String name = etName.getText().toString();
////        String email = etEmail.getText().toString();
//
//        // подключаемся к БД
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//
//        switch (v.getId()) {
//            case R.id.btnAdd:
//                Log.d(LOG_TAG, "--- Insert in mytable: ---");
//                // подготовим данные для вставки в виде пар: наименование столбца - значение
//
//                cv.put("name", name);
//                cv.put("email", email);
//                // вставляем запись и получаем ее ID
//                long rowID = db.insert("mytable", null, cv);
//                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
//                break;
//            case R.id.btnRead:
//                Log.d(LOG_TAG, "--- Rows in mytable: ---");
//                // делаем запрос всех данных из таблицы mytable, получаем Cursor
//                Cursor c = db.query("mytable", null, null, null, null, null, null);
//
//                // ставим позицию курсора на первую строку выборки
//                // если в выборке нет строк, вернется false
//                if (c.moveToFirst()) {
//
//                    // определяем номера столбцов по имени в выборке
//                    int idColIndex = c.getColumnIndex("id");
//                    int nameColIndex = c.getColumnIndex("name");
//                    int emailColIndex = c.getColumnIndex("email");
//
//                    do {
//                        // получаем значения по номерам столбцов и пишем все в лог
//                        Log.d(LOG_TAG,
//                                "ID = " + c.getInt(idColIndex) +
//                                        ", name = " + c.getString(nameColIndex) +
//                                        ", email = " + c.getString(emailColIndex));
//                        // переход на следующую строку
//                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
//                    } while (c.moveToNext());
//                } else
//                    Log.d(LOG_TAG, "0 rows");
//                c.close();
//                break;
//            case R.id.btnClear:
//                Log.d(LOG_TAG, "--- Clear mytable: ---");
//                // удаляем все записи
//                int clearCount = db.delete("mytable", null, null);
//                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
//                break;
//        }
//        // закрываем подключение к БД
//        dbHelper.close();
//    }
}
