package ru.sappstudio.mothershelper;

/**
 * Created by user on 04.03.17.
 */
public class LVEvent {

    int ivEvent;    //id image res
    int ivLVFon;    //id image res
    String tvEvent; //name event
    String tvTimeEvent; //time event
    String tvStarEvent;
    String tvLVMess;
    String tvStopEvent;
    int ivStatusEvent;


    LVEvent(int _ivEvent, int _ivLVFon, String _tvEvent, String _tvTimeEvent, String _tvStarEvent, String _tvStopEvent,String _tvLVMess, int _tvStatusEvent) {
            ivEvent = _ivEvent;
            ivLVFon = _ivLVFon;
            tvEvent = _tvEvent;
            tvTimeEvent = _tvTimeEvent;
            tvStarEvent = _tvStarEvent;
            tvStopEvent = _tvStopEvent;
            ivStatusEvent = _tvStatusEvent;
            tvLVMess = _tvLVMess;
    }

}
