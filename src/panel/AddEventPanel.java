package panel;

import core.EventDAO;
import core.Events;
import core.TicketDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AddEventPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private EventDAO eventDAO;
    private int readerId;

    public AddEventPanel(CardLayout cardLayout, JPanel cardPanel, EventDAO eventDAO, int readerId) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.eventDAO = eventDAO;
        this.readerId = readerId;

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("Available Events", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Events List
        JPanel eventsPanel = new JPanel();
        eventsPanel.setBackground(new Color(245, 240, 230));
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Populate events
        List<Events> events = eventDAO.getNonExpiredEvents();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (events.isEmpty()) {
            JLabel noEventsLabel = new JLabel("No upcoming events available.");
            noEventsLabel.setFont(new Font("Serif", Font.PLAIN, 18));
            noEventsLabel.setForeground(new Color(102, 51, 0));
            eventsPanel.add(noEventsLabel);
        } else {
            for (Events event : events) {
                // Fetch event ID
                int eventId = getEventIdByName(event.getName());
                if (eventId <= 0) {
                    System.err.println("Could not find ID for event: " + event.getName());
                    continue;
                }

                JPanel eventPanel = new JPanel(new BorderLayout(5, 5));
                eventPanel.setBackground(new Color(255, 250, 240));
                eventPanel.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1));
                eventPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                JLabel eventLabel = new JLabel("<html><b>" + event.getName() + "</b><br>" +
                    "Date: " + dateFormat.format(event.getDatetime()) + "<br>" +
                    "Capacity: " + event.getCapacity() + "<br>" +
                    "Address: " + event.getAddress() + "</html>");
                eventLabel.setFont(new Font("Serif", Font.PLAIN, 16));
                eventLabel.setForeground(new Color(102, 51, 0));
                eventLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JButton selectButton = new JButton("Select");
                selectButton.setFont(new Font("Serif", Font.BOLD, 14));
                selectButton.setBackground(new Color(255, 221, 153));
                selectButton.setForeground(new Color(102, 51, 0));
                selectButton.setFocusPainted(false);
                selectButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2));
                selectButton.addActionListener(e -> {
                    cardPanel.add(new PickEventPanel(cardLayout, cardPanel, eventDAO, new TicketDAO(eventDAO.getConnection()), event, readerId, eventId), "pickEventPanel");
                    cardLayout.show(cardPanel, "pickEventPanel");
                });

                eventPanel.add(eventLabel, BorderLayout.CENTER);
                eventPanel.add(selectButton, BorderLayout.EAST);
                eventsPanel.add(eventPanel);
                eventsPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Back Button
        JButton backButton = new JButton("Back to Reader Panel");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
        backButton.setBackground(new Color(255, 221, 153));
        backButton.setForeground(new Color(102, 51, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "readerMainPanel"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private int getEventIdByName(String name) {
        String query = "SELECT id FROM events WHERE name = ?";
        try (PreparedStatement ps = eventDAO.getConnection().prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in getEventIdByName: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
}