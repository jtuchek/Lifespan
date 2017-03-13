package com.botlink.lifespan;


import android.os.Bundle;

public interface ILifespan {

    <T> LifespanHolder<T> manage(Bundle bundle,
                                 LifespanCreation<T> creation,
                                 LifespanTermination termination);

    void write(Bundle bundle, LifespanHolder holder);

}
