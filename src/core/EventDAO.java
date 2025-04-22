package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private Connection connection;

    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addEvent(Events event) {
        String query = "INSERT INTO events (author_id, name, date, capacity, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, event.getAuthor_id());
            ps.setString(2, event.getName());
            ps.setTimestamp(3, event.getDatetime());
            ps.setInt(4, event.getCapacity());
            ps.setString(5, event.getAddress());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean deleteEvent(int eventId) {
        String query = "DELETE FROM events WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Events getEventByName(String name) {
        String query = "SELECT * FROM events WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Events(
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getTimestamp("date"),
                        rs.getInt("capacity"),
                        rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Events getEventById(int eventId) {
        String query = "SELECT * FROM events WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Events(
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getTimestamp("date"),
                        rs.getInt("capacity"),
                        rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Events> getAllEvents() {
        List<Events> eventList = new ArrayList<>();
        String query = "SELECT * FROM events ORDER BY date DESC";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Events event = new Events(
                    rs.getInt("author_id"),
                    rs.getString("name"),
                    rs.getTimestamp("date"),
                    rs.getInt("capacity"),
                    rs.getString("address")
                );
                eventList.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }
    public List<Events> getNonExpiredEvents() {
        List<Events> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE date >= CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Events(
                    rs.getInt("author_id"),
                    rs.getString("name"),
                    rs.getTimestamp("date"),
                    rs.getInt("capacity"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching non-expired events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    public boolean authorExists(int authorId) {
        String query = "SELECT 1 FROM authors WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("SQL error in authorExists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}