package panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import core.EventDAO;
import core.FeedbackDAO;
import core.WriterDAO;

public class WriterMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public WriterMainPanel(CardLayout cardLayout, JPanel cardPanel, WriterDAO writerDAO, FeedbackDAO feedbackDAO, int id) {
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Writer Panel", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Center Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 240, 230));
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 20)); // 3 rows, 1 column, 20px vertical gap
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150)); // padding around buttons

        JButton reportsButton = new JButton("Reports");
        styleButton(reportsButton);
        reportsButton.addActionListener(e -> {
            cardPanel.add(new WriterReportsPanel(cardLayout, cardPanel, writerDAO, feedbackDAO, id), "showWriterReports");
            cardLayout.show(cardPanel, "showWriterReports");
        });

        JButton createEventButton = new JButton("Create Event");
        styleButton(createEventButton);
        createEventButton.addActionListener(e -> {
            cardPanel.add(new CreateEventPanel(cardLayout, cardPanel, new EventDAO(writerDAO.getConnection()), id), "createEventPanel");
            cardLayout.show(cardPanel, "createEventPanel");
        });

        buttonPanel.add(reportsButton);
        buttonPanel.add(createEventButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        styleButton(backButton);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Helper method to style buttons consistently
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setBackground(new Color(255, 221, 153));
        button.setForeground(new Color(102, 51, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
    }
}