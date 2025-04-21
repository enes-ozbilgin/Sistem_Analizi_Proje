package core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TicketDAO {
    private Connection connection;

    public TicketDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Ticket ticket) throws SQLException {
        String query = "INSERT INTO tickets (reader_id, event_id, registration_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, ticket.getReaderId());
            ps.setInt(2, ticket.getEventId());
            ps.setTimestamp(3, ticket.getRegistrationDate());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean isReaderRegistered(int readerId, int eventId) {
        String query = "SELECT 1 FROM tickets WHERE reader_id = ? AND event_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, readerId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("SQL error in isReaderRegistered: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eventHasCapacity(int eventId) {
        String query = "SELECT e.capacity, COUNT(t.id) AS ticket_count " +
                      "FROM events e LEFT JOIN tickets t ON e.id = t.event_id " +
                      "WHERE e.id = ? GROUP BY e.id, e.capacity";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int capacity = rs.getInt("capacity");
                    int ticketCount = rs.getInt("ticket_count");
                    System.out.println("Event ID: " + eventId + ", Capacity: " + capacity + ", Tickets: " + ticketCount);
                    if (rs.wasNull() || capacity <= 0) {
                        System.err.println("Invalid capacity for event ID " + eventId + ": " + capacity);
                        return false;
                    }
                    return ticketCount < capacity;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in eventHasCapacity: " + e.getMessage());
            e.printStackTrace();
        }
        System.err.println("Event ID " + eventId + " not found.");
        return false;
    }
}