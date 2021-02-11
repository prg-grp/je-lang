package de.tuda.prg.eventProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EventGenerator {

    private static int lowerBound = 1;
    private static int upperBound = 100;

    public static List<IntEvent> getIntEvents(int noOfEvents) {
        List<IntEvent> eventList = new ArrayList<>();
        for (int i = 1 ; i <= noOfEvents; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
            eventList.add(new IntEvent(randomNum));
        }
        return eventList;
    }
}
