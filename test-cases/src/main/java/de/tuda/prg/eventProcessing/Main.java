package de.tuda.prg.eventProcessing;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<IntEvent> events = EventGenerator.getIntEvents(100);
        List<EncIntEvent> encEvents = Encryptor.encrypt(events);
        List<EncIntEvent> result = encEvents.stream().filter(FilterNode.filter).collect(Collectors.toList());
    }
}
