package org.example;

import java.util.Map;

/**
 * @author Michael Gerasimov
 */
public interface EventsStatistics {
    void incEvent(String name);

    double getEventStatisticByName(String name);

    Map<String, Double> getAllEventStatistic();

    void printStatistic();
}