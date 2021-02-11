package de.tuda.prg.eventProcessing;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Encryptor {

    private static String source = "ServerA";
    private static String destination = "Accumulator";

    public static List<EncIntEvent> encrypt(List<IntEvent> intEvents) {
        List<EncIntEvent> encIntEvents = new ArrayList<>();
        for (IntEvent intEvent : intEvents) {
            encIntEvents.add(encrypt(intEvent));
        }
        return encIntEvents;
    }

    public static EncIntEvent encrypt (IntEvent intEvent) {
        return new EncIntEvent(encrypt(intEvent.getEventVal()), source, destination);
    }

    public static EncInt encrypt (Integer toEncrypt) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(toEncrypt).array();
        return new EncInt(bytes);
    }
}
