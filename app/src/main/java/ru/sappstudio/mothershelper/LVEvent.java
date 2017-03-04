package ru.sappstudio.mothershelper;

/**
 * Created by user on 04.03.17.
 */
public class LVEvent {

    int ivEvent;    //id image res
    String tvEvent; //name event
    String tvTimeEvent; //time event
    String tvStarEvent;
    String tvStopEvent;
    int ivStatusEvent;


    LVEvent(int _ivEvent, String _tvEvent, String _tvTimeEvent, String _tvStarEvent, String _tvStopEvent, int _tvStatusEvent) {
            ivEvent = _ivEvent;
            tvEvent = _tvEvent;
            tvTimeEvent = _tvTimeEvent;
            tvStarEvent = _tvStarEvent;
            tvStopEvent = _tvStopEvent;
            ivStatusEvent = _tvStatusEvent;
    }

}
