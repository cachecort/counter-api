package com.innometrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Counter {

    public static final int NO_VALUE = -1;
    private static final int DEFAULT_VALUE = 1;
    private static Counter instance;

    private Map<String, Integer> counter;

    private Counter() {
        init();
    }

    public static Counter getInstance() {
        if (instance == null) {
            instance = new Counter();
        }

        return instance;
    }

    /**
     * Identifies whether a counter corresponding to the given name exists
     * or not.
     *
     * @param counterName - a name of the counter
     * @return true of counter with such name exists, otherwise false
     */
    public boolean exists(final String counterName) {
        return counter.containsKey(counterName);
    }

    /**
     * Returns a value of a counter corresponding to a given name.
     *
     * @param counterName - a name of the counter
     * @return a value of counter
     */
    public Integer getValue(String counterName) {
        return counter.get(counterName);
    }

    /**
     * Increments by one a counter corresponding to a given name.
     *
     * @param counterName - a name of the counter
     */
    public void increment(String counterName) {
        if (exists(counterName)) {
            Integer incremented = counter.get(counterName) + 1;
            counter.put(counterName, incremented);
        } else {
            counter.put(counterName, DEFAULT_VALUE);
        }
    }

    /**
     * Returns all counters along with their current values.
     *
     * @return the list of counter with values
     */
    public Map<String, Integer> listAll() {
        return counter;
    }

    /**
     * Initializes application specific variables.
     */
    private void init() {
        counter = new ConcurrentHashMap<>();
    }
}
