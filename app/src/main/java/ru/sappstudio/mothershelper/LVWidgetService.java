package ru.sappstudio.mothershelper;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by user on 26.03.17.
 */


public class LVWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new LVWidgetAdapter(getApplicationContext(), intent);
    }
}




