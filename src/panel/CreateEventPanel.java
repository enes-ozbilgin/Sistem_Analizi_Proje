package panel;

import javax.swing.*;
import core.EventDAO;
import core.Events;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CreateEventPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField nameField;
    private JTextField dateField;
    private JTextField capacityField;
    private JTextArea addressArea;
    private EventDAO eventDAO;
    private int authorId;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public CreateEventPanel(CardLayout cardLayout, JPanel cardPanel, EventDAO eventDAO, int authorId) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.eventDAO = eventDAO;
        this.authorId = authorId;

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Create Event", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 230));
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Name
        JLabel nameLabel = new JLabel("Event Name:");
        styleLabel(nameLabel);
        nameField = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        // Date
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD HH:MM:SS):");
        styleLabel(dateLabel);
        dateField = new JTextField();
        formPanel.add(dateLabel);
        formPanel.add(dateField);

        // Capacity
        JLabel capacityLabel = new JLabel("Capacity:");
        styleLabel(capacityLabel);
        capacityField = new JTextField();
        formPanel.add(capacityLabel);
        formPanel.add(capacityField);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        styleLabel(addressLabel);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        formPanel.add(addressLabel);
        formPanel.add(addressScroll);

        // Submit Button
        JButton submitButton = new JButton("Create Event");
        styleButton(submitButton);
        submitButton.addActionListener(e -> createEvent());

        formPanel.add(new JLabel());
        formPanel.add(submitButton);

        add(formPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Writer Panel");
        styleButton(backButton);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "writerMainPanel")); // Changed to writerMainPanel

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createEvent() {
        String name = nameField.getText().trim();
        String dateStr = dateField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        String address = addressArea.getText().trim();

        // Validation
        if (name.isEmpty() || dateStr.isEmpty() || capacityStr.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate authorId
        if (authorId <= 0 || !eventDAO.authorExists(authorId)) {
            JOptionPane.showMessageDialog(this, "Invalid Author ID! Author does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        Timestamp date;
        try {
            date = new Timestamp(dateFormat.parse(dateStr).getTime());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate capacity
        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, "Capacity must be a positive number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Events event = new Events(authorId, name, date, capacity, address);
        boolean success = eventDAO.addEvent(event);
		if (success) {
		    JOptionPane.showMessageDialog(this, "Event created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		    nameField.setText("");
		    dateField.setText("");
		    capacityField.setText("");
		    addressArea.setText("");
		    cardLayout.show(cardPanel, "writerMainPanel");
		} else {
		    JOptionPane.showMessageDialog(this, "Failed to create event! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setForeground(new Color(102, 51, 0));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(new Color(255, 221, 153));
        button.setForeground(new Color(102, 51, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
    }
}
