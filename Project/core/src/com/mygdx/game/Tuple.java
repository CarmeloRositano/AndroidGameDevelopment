package com.mygdx.game;

/**
 * The Point of making this class is because com.sun.tools.javac.util.Pair wouldn't work for the life of me.
 * @param <T>
 * @param <U>
 */
public class Tuple<T,U,S> {
    public final T fst;
    public final U snd;
    public final S thd;

    public Tuple(T key, U value1, S value2) {
        this.fst = key;
        this.snd = value1;
        this.thd = value2;
    }

    public T getKey() {
        return this.fst;
    }

    public U getValue1() {
        return this.snd;
    }

    public S getValue2() {
        return this.thd;
    }
}