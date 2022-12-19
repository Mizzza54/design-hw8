package org.example;

import java.time.Instant;

/**
 * @author Michael Gerasimov
 */
public class Event {
    private final Instant time;
    private final String name;

    public Event(Instant time, String name) {
        this.time = time;
        this.name = name;
    }

    public Instant getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
