package com.botlink.lifespan;


public interface LifespanHolder<T> {

    long token();

    T value();

}
