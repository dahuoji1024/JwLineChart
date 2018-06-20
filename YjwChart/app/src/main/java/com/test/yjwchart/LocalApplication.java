package com.test.yjwchart;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by 10732 on 2018/5/17.
 */

public class LocalApplication extends Application {

    public static Context context;
    public static Typeface pf_normal;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    public static Context getContext() {
        return context;
    }


}
