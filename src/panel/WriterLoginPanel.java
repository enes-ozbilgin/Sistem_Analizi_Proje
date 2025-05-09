package panel;

import core.FeedbackDAO;
import core.Writer;
import core.WriterDAO;
import javax.swing.*;
import java.awt.*;

public class WriterLoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public WriterLoginPanel(CardLayout cardLayout, JPanel cardPanel, WriterDAO writerDAO) {
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Writer Login", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 230));
        formPanel.setLayout(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JLabel usernameLabel = new JLabel("Username:");
        styleLabel(usernameLabel);
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);
        passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Writer writer = writerDAO.read(username, password);
            if (writer != null) {
                JOptionPane.showMessageDialog(this, "Login successful for " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardPanel.add(new WriterMainPanel(cardLayout, cardPanel, writerDAO,new FeedbackDAO(writerDAO.getConnection()), writer.getId()), "writerMainPanel");
                cardLayout.show(cardPanel, "writerMainPanel");
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        styleButton(backButton);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
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

    public String getEnteredUsername() {
        return usernameField.getText();
    }
}