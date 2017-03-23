package ru.sappstudio.mothershelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 04.03.17.
 */
public class LVEvent {

    String tableName;
    int ivEvent;    //id image res
    int ivLVFon;    //id image res
    String id;
    long unixTimeSecond_s;
    long unixTimeSecond_e;
    String tvEvent; //name event
    String tvTimeEvent; //time event
    String tvStarEvent;
    String tvLVMess;
    String tvStopEvent;
    int ivStatusEvent;
    String sortIdTimeS;


    LVEvent(String _tableName, int _ivEvent,  int _ivLVFon, String _id, long _unixTimeSecond_s, long _unixTimeSecond_e, String _tvEvent, String _tvTimeEvent, String _tvStarEvent, String _tvStopEvent,String _tvLVMess, int _tvStatusEvent) {

            tableName = _tableName;
            ivEvent =_ivEvent;
            ivLVFon = _ivLVFon;
            id=_id;
            unixTimeSecond_s = _unixTimeSecond_s;
            unixTimeSecond_e = _unixTimeSecond_e;
            tvEvent = _tvEvent;
            tvTimeEvent = _tvTimeEvent;
            tvStarEvent = _tvStarEvent;
            tvStopEvent = _tvStopEvent;
            ivStatusEvent = _tvStatusEvent;
            tvLVMess = _tvLVMess;
            sortIdTimeS = getTimeSFormatHHmm();
    }
    public String getTimeSFormatHHmm(){
        return String.valueOf(unixTimeSecond_s -(Integer.valueOf(getTimeFormat(unixTimeSecond_s,"yyyy"))*31536000 + Integer.valueOf(getTimeFormat(unixTimeSecond_s,"MM"))*2628002 + Integer.valueOf(getTimeFormat(unixTimeSecond_s,"dd"))*86400));
       // return 0;
    }

    public String getTimeFormat(long _unixSeconds,String _format){
        Date date = new Date(_unixSeconds*1000L); // *1000 получаем миллисекунды

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
