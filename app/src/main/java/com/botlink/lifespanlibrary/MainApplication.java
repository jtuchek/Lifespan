package com.botlink.lifespanlibrary;


import android.app.Application;

import com.botlink.lifespan.ILifespan;
import com.botlink.lifespan.Lifespan;

public class MainApplication extends Application {

    private Lifespan lifespanTracker = new Lifespan(this, "super-unique-non-colliding-key");

    @Override
    public void onCreate() {
        super.onCreate();
        lifespanTracker.onCreate();
    }

    public ILifespan lifespanTracker() {
        return lifespanTracker;
    }

}
