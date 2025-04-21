package panel;

import javax.swing.*;
import java.awt.*;

public class ManagerPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public ManagerPanel(CardLayout cardLayout, JPanel cardPanel) {
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Manager Panel", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Center Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 240, 230));
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        JButton reportsButton = new JButton("Reports");
        styleButton(reportsButton);
        reportsButton.addActionListener(e -> cardLayout.show(cardPanel, "managerReportPanel"));

        JButton addBookButton = new JButton("Add Book");
        styleButton(addBookButton);
        addBookButton.addActionListener(e -> cardLayout.show(cardPanel, "addBookPanel"));

        JButton addWriterButton = new JButton("Add Writer");
        styleButton(addWriterButton);
        addWriterButton.addActionListener(e -> cardLayout.show(cardPanel, "addWriterPanel"));

        buttonPanel.add(reportsButton);
        buttonPanel.add(addBookButton);
        buttonPanel.add(addWriterButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
        backButton.setBackground(new Color(255, 221, 153));
        backButton.setForeground(new Color(102, 51, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setBackground(new Color(255, 221, 153));
        button.setForeground(new Color(102, 51, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
    }
}
