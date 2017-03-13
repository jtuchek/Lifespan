package com.botlink.lifespan;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public final class Lifespan implements ILifespan {

    private final Application application;
    private final String key;

    private final AtomicLong tokenGenerator = new AtomicLong(0);
    private final Map<Long, InternalLifespanHolder> mapOfHolding = new HashMap<>();

    public Lifespan(Application application, String key) {
        this.application = application;
        this.key = key;
    }

    @Override
    public <T> LifespanHolder<T> manage(Bundle bundle,
                                        LifespanCreation<T> creation,
                                        LifespanTermination termination) {
        if(bundle == null) {
            final long token = tokenGenerator.getAndIncrement();
            if(mapOfHolding.containsKey(token)) {
                throw new IllegalStateException("key already exist:" + token);
            }
            InternalLifespanHolder<T> holder = new InternalLifespanHolder<>(
                    token, creation.provide(), termination);
            mapOfHolding.put(token, holder);
            return holder;
        } else {
            final long token = bundle.getLong(key, -1);
            if(token == -1) {
                throw new IllegalStateException("key not found");
            }
            //noinspection unchecked
            return (LifespanHolder<T>) mapOfHolding.get(token);
        }
    }

    @Override
    public void write(Bundle bundle, LifespanHolder holder) {
        bundle.putLong(key, holder.token());
    }

    public void onCreate() {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private final ActivityLifecycleCallbacks lifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

        @Override
        public void onActivityStarted(Activity activity) {}

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}

        @Override
        public void onActivityStopped(Activity activity) {
            if(activity instanceof LifespanToken) {
                LifespanToken token = (LifespanToken) activity;
                if(activity.isFinishing() && mapOfHolding.containsKey(token.token())) {
                    mapOfHolding.get(token.token()).termination().isFinishing();
                    mapOfHolding.remove(token.token());
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

        @Override
        public void onActivityDestroyed(Activity activity) {
            if(activity instanceof LifespanToken) {
                LifespanToken token = (LifespanToken) activity;
                if(activity.isFinishing() && mapOfHolding.containsKey(token.token())) {
                    mapOfHolding.get(token.token()).termination().isFinishing();
                    mapOfHolding.remove(token.token());
                }
            }
        }
    };


}
