package ru.sappstudio.mothershelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by user on 26.03.17.
 */
public class UpdateWidgetService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // create some random data

//        DisplayMetrics metrics = this.getResources().getDisplayMetrics();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        ComponentName thisWidget = new ComponentName(getApplicationContext(), Widget.class);
        int[] allCurrentWidget = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetID : allCurrentWidget) {
            Widget.updateWidget(this, appWidgetManager, widgetID);
        }
        Log.e("Servis","servis start");

//        thisWidget = new ComponentName(getApplicationContext(), Widget.class);
//        allCurrentWidget = appWidgetManager.getAppWidgetIds(thisWidget);
//
//        for (int widgetID : allCurrentWidget) {
//            Widget.updateWidget(this, appWidgetManager, widgetID);
//        }

//        appWidgetManager.notifyAppWidgetViewDataChanged(allCurrentWidget, R.id.lvWEvents);

        startUpdateAlarm(this);
        stopSelf();


        return super.onStartCommand(intent, flags, startId);
    }

    public static void startUpdateAlarm(Context cc) {

        int intervalInMillis = 1000;

        Intent intent = new Intent(cc, UpdateWidgetService.class);
        PendingIntent pintent = PendingIntent.getService(cc, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND,1000*10);

        AlarmManager alarmManager = (AlarmManager) cc.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), intervalInMillis, pintent);
    }
}
