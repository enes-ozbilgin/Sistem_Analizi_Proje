package core;

import java.sql.Timestamp;

public class Ticket {
    private int ticketId;
    private int readerId;
    private int eventId;
    private Timestamp registrationDate;

    public Ticket(int readerId, int eventId, Timestamp registrationDate) {
        this.readerId = readerId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
    }

    public Ticket(int ticketId, int readerId, int eventId, Timestamp registrationDate) {
        this.ticketId = ticketId;
        this.readerId = readerId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
    }

    public int getTicketId() { return ticketId; }
    public int getReaderId() { return readerId; }
    public int getEventId() { return eventId; }
    public Timestamp getRegistrationDate() { return registrationDate; }
}
