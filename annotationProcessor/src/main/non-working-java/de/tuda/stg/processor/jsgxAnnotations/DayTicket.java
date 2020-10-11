package de.tuda.stg.processor.jsgxAnnotations;

@FactoryTicket(type = Ticket.class, id = "DayTicket")
public class DayTicket implements Ticket{

    int price = 5;

    @Override
    public int getPrice() {
        return this.price;
    }
}
