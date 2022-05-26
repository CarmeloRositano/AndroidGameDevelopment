package com.mygdx.game;

/**
 * The Point of making this class is because com.sun.tools.javac.util.Pair wouldn't work for the life of me.
 * @param <T>
 * @param <U>
 */
public class Pair<T,U> {
    public final T fst;
    public final U snd;

    public Pair(T key, U value) {
        this.fst = key;
        this.snd = value;
    }

    public T getKey() {
        return this.fst;
    }

    public U getValue() {
        return this.snd;
    }
}