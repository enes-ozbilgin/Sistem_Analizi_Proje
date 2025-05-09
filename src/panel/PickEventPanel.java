package panel;

import core.EventDAO;
import core.Events;
import core.Ticket;
import core.TicketDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class PickEventPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private EventDAO eventDAO;
    private TicketDAO ticketDAO;
    private Events event;
    private int readerId;
    private int eventId;

    public PickEventPanel(CardLayout cardLayout, JPanel cardPanel, EventDAO eventDAO, TicketDAO ticketDAO, Events event, int readerId, int eventId) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.eventDAO = eventDAO;
        this.ticketDAO = ticketDAO;
        this.event = event;
        this.readerId = readerId;
        this.eventId = eventId;

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("Register for Event", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Event Details
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(255, 250, 240));
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1));

        JLabel nameLabel = new JLabel("Event: " + event.getName());
        nameLabel.setFont(new Font("Serif", Font.BOLD, 18));
        nameLabel.setForeground(new Color(102, 51, 0));

        JLabel dateLabel = new JLabel("Date: " + dateFormat.format(event.getDatetime()));
        dateLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(102, 51, 0));

        JLabel capacityLabel = new JLabel("Capacity: " + event.getCapacity());
        capacityLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        capacityLabel.setForeground(new Color(102, 51, 0));

        JLabel addressLabel = new JLabel("Address: " + event.getAddress());
        addressLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        addressLabel.setForeground(new Color(102, 51, 0));

        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(capacityLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(addressLabel);
        detailsPanel.add(Box.createVerticalStrut(10));

        add(detailsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 240, 230));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Serif", Font.BOLD, 18));
        registerButton.setBackground(new Color(255, 221, 153));
        registerButton.setForeground(new Color(102, 51, 0));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2));
        registerButton.addActionListener(e -> registerForEvent());

        JButton backButton = new JButton("Back to Events");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
        backButton.setBackground(new Color(255, 221, 153));
        backButton.setForeground(new Color(102, 51, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "addEventPanel"));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerForEvent() {
        try {
            if (eventId <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid event ID!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (ticketDAO.isReaderRegistered(readerId, eventId)) {
                JOptionPane.showMessageDialog(this, "You are already registered for this event!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!ticketDAO.eventHasCapacity(eventId)) {
                JOptionPane.showMessageDialog(this, "Event is at full capacity or has invalid capacity (0 or not set)!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ticket ticket = new Ticket(readerId, eventId, new Timestamp(System.currentTimeMillis()));
            if (ticketDAO.create(ticket)) {
                JOptionPane.showMessageDialog(this, "Successfully registered for " + event.getName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "addEventPanel");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register for event!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            String message = e.getMessage().contains("unique_reader_event") ?
                "You are already registered for this event!" :
                "Database error: " + e.getMessage();
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}