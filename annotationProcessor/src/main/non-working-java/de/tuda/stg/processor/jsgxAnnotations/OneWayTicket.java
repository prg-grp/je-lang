package de.tuda.stg.processor.jsgxAnnotations;

@FactoryTicket(type = Ticket.class, id = "OneWayTicket")
public class OneWayTicket implements Ticket {
    int price = 1;

    @Override
    public int getPrice() {
        return this.price;
    }
}
