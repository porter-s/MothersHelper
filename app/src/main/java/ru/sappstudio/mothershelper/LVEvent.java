package ru.sappstudio.mothershelper;

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
    }

}
