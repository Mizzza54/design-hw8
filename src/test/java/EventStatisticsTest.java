import org.example.EventsStatistics;
import org.example.EventsStatisticsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michael Gerasimov
 */
public class EventStatisticsTest {

    private static final String EVENT_NAME = "event";

    private static final Integer MINUTES_IN_HOUR = 60;
    private static final Double EPS = 1e-6;

    private SettableClock clock;
    private EventsStatistics eventsStatistics;


    @BeforeEach
    public void before() {
        this.clock = new SettableClock(Instant.now());
        this.eventsStatistics = new EventsStatisticsImpl(clock);
    }

    @Test
    public void baseFunctionTest() {
        repeat(MINUTES_IN_HOUR, EVENT_NAME);

        checkEvent(EVENT_NAME, 1.0);
    }

    @Test
    public void deleteOldEventsTest() {
        repeat(MINUTES_IN_HOUR, EVENT_NAME);

        clock.instant = clock.instant().plus(2, ChronoUnit.HOURS);

        checkEvent(EVENT_NAME, 0.0);
    }

    @Test
    public void deleteOldEventsAndAfterAddNewEventsTest() {
        repeat(MINUTES_IN_HOUR, EVENT_NAME);

        clock.instant = clock.instant().plus(2, ChronoUnit.HOURS);

        checkEvent(EVENT_NAME, 1.0);
    }


    @Test
    public void addEventsAtTimeIntervals() {
        for (int i = 0; i < 4; i++) {
            clock.instant = clock.instant().plus(5, ChronoUnit.MINUTES);

            repeat(MINUTES_IN_HOUR, EVENT_NAME);
        }

        checkEvent(EVENT_NAME, 4.0);
    }

    private void checkEvent(String name, Double expectedValue) {
        var value = eventsStatistics.getEventStatisticByName(name);
        assertEquals(expectedValue, value, EPS);

        Map<String, Double> allEventStatistic = eventsStatistics.getAllEventStatistic();
        assertEquals(expectedValue, allEventStatistic.get(name), EPS);
    }

    private void repeat(int count, String name) {
        for (int i = 0; i < count; i++) {
            eventsStatistics.incEvent(name);
        }
    }

    private static class SettableClock extends Clock {
        private Instant instant;

        public SettableClock(Instant instant) {
            this.instant = instant;
        }

        @Override
        public ZoneId getZone() {
            return null;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return null;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
