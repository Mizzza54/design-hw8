package org.example;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author Michael Gerasimov
 */
public class EventsStatisticsImpl implements EventsStatistics {
    private static final Double MINUTES_IN_HOUR = 60.0;

    private final Clock clock;
    private final Queue<Event> queueEvents;
    private final Map<String, Integer> rpmByName;

    public EventsStatisticsImpl(Clock clock) {
        this.clock = clock;
        queueEvents = new ArrayDeque<>();
        rpmByName = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        rpmByName.merge(name, 1, Integer::sum);
        queueEvents.add(new Event(clock.instant(), name));
    }

    @Override
    public double getEventStatisticByName(String name) {
        clearQueue();

        return rpmByName.get(name) / MINUTES_IN_HOUR;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        clearQueue();

        return rpmByName.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / MINUTES_IN_HOUR));
    }

    @Override
    public void printStatistic() {
        getAllEventStatistic().forEach((k, v) -> System.out.printf("Event: %s, RPM: %s%n", k, v));
    }

    private void clearQueue() {
        Instant hourAgo = clock.instant().minus(1, ChronoUnit.HOURS);

        while (!queueEvents.isEmpty() && queueEvents.peek().getTime().isBefore(hourAgo)) {
            Event popEvent = queueEvents.poll();
            rpmByName.computeIfPresent(popEvent.getName(), (x, oldCount) -> oldCount - 1);
        }
    }
}
