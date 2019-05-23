package com.saveandstudio.mario.cdd;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

public class LeakDebug extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {//1
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
