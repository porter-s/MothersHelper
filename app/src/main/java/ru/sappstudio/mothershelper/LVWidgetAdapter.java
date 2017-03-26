package ru.sappstudio.mothershelper;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 26.03.17.
 */
public class LVWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    String LOG_TAG = "LVWidgetAdapter";
    ArrayList<LVEvent> data;
    Context context;
    SimpleDateFormat sdf;
    int widgetID;

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
//-----------------------------------------------------------------------------//

    LVWidgetAdapter(Context ctx, Intent intent) {
        context = ctx;
        sdf = new SimpleDateFormat("HH:mm:ss");
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<LVEvent>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.item_list_widget);

        rView.setTextViewText(R.id.tvWTimeEvent, data.get(position).tvTimeEvent);
        rView.setTextViewText(R.id.tvWEvent, data.get(position).tvEvent);
        rView.setImageViewResource(R.id.ivWFon, data.get(position).ivLVFon);
        rView.setImageViewResource(R.id.ivWEvent, data.get(position).ivEvent);
        rView.setImageViewResource(R.id.ivWStatusEvent, data.get(position).ivStatusEvent);

        if(data.get(position).tableName.equals("Koliki")||data.get(position).tableName.equals("Food"))
        {
            rView.setTextViewText(R.id.tvWTime_s, "");
            rView.setTextViewText(R.id.tvWTime_e, data.get(position).tvStopEvent);
            rView.setTextViewText(R.id.tvWS, "");
            rView.setTextViewText(R.id.tvWDo, "");
        }else{
            rView.setTextViewText(R.id.tvWTime_s, data.get(position).tvStarEvent);
            rView.setTextViewText(R.id.tvWTime_e, data.get(position).tvStopEvent);
            rView.setTextViewText(R.id.tvWS, "С");
            rView.setTextViewText(R.id.tvWDo, "до");
        }

        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        data.clear();
//        data.add(sdf.format(new Date(System.currentTimeMillis())));
//        data.add(String.valueOf(hashCode()));
        //data.add(String.valueOf(widgetID));
        initData("Sleap", "Спим", "Спали", R.drawable.krovat, R.drawable.krovat_w, R.drawable.krovat, R.drawable.fon_lv_sleap_on, R.drawable.fon_lv_sleap_off);
        initData("Food", "Ели", "Ели", R.drawable.but_w, R.drawable.but_w, R.drawable.but, R.drawable.fon_lv_but_on, R.drawable.fon_lv_but_off);
        initData("Koliki", "Колики", "Колики", R.drawable.koliki_w, R.drawable.koliki_w, R.drawable.koliki, R.drawable.fon_lv_koliki_on, R.drawable.fon_lv_koliki_off);
        initData("Walk", "Гуляем", "Гуляли", R.drawable.kolaska, R.drawable.kolaska_w, R.drawable.kolaska, R.drawable.fon_lv_kolaska_on, R.drawable.fon_lv_kolaska_off);

    }


    @Override
    public void onDestroy() {

    }

    //------------------------------------------------------------------------//

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

    public void initData(String _tableName, String _actionStart, String _actionStop, int _idImageActiv,int _idImagePassiv, int _idIconLv, int _idFonLvOn, int _idFonLvOff)    //обновляем данные
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(context);
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
                    data.add(new LVEvent(_tableName,
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
                    data.add(new LVEvent(_tableName,
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
                data.add(new LVEvent(_tableName,
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

                data.add(new LVEvent(_tableName,
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
            Log.e(LOG_TAG, _tableName + " status = " + c.getString(status_ColIndex));

//            if (c.getString(status_ColIndex).equals("start")) {
//                _button.setImageResource(_idImageActiv);
//            } else {
//                _button.setImageResource(_idImagePassiv);
//            }
        }
        c.close();

    }
}
