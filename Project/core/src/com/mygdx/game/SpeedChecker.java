package com.mygdx.game;

import com.badlogic.gdx.utils.ArrayMap;

import java.util.Vector;

/**
 * Used for checking the speed between different points in code for bad performance
 */
public class SpeedChecker {

    private static SpeedChecker speedChecker;
    private ArrayMap<String, Vector<Long>> times = new ArrayMap();
    private boolean print = true;

    public static SpeedChecker gI() {
        if (speedChecker == null) {
            speedChecker = new SpeedChecker();
        }
        return speedChecker;
    }

    public void startCheck(String name) {
        if (!times.containsKey(name)) {
            times.put(name, new Vector<Long>());
        }
        times.get(name).add(System.currentTimeMillis());
    }

    public long endCheck(String name) {
        if (!times.containsKey(name)) {
            return 0;
        }
        Vector<Long> v = times.get(name);
        long old = v.lastElement();
        v.remove(times.get(name).lastElement());
        v.add(System.currentTimeMillis()-old);
        return v.lastElement();
    }

}
