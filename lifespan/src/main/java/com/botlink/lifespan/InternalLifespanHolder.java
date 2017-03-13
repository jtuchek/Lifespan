package com.botlink.lifespan;


public class InternalLifespanHolder<T> implements LifespanHolder<T> {

    private final long token;
    private final T value;
    private final LifespanTermination termination;

    public InternalLifespanHolder(long token, T value, LifespanTermination termination) {
        this.token = token;
        this.value = value;
        this.termination = termination;
    }

    @Override
    public long token() {
        return token;
    }

    @Override
    public T value() {
        return value;
    }

    public LifespanTermination termination() {
        return termination;
    }
}
