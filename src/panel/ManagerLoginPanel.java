package panel;

import javax.swing.*;
import java.awt.*;

public class ManagerLoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String MANAGER_PASSWORD = "03emin30";//buradan şifreyi değiştirebilirsin.
    private JPasswordField passwordField;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ManagerLoginPanel(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel title = new JLabel("Manager Login", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 230));
        formPanel.setLayout(new GridLayout(2, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Serif", Font.BOLD, 18));
        passwordLabel.setForeground(new Color(102, 51, 0));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Serif", Font.PLAIN, 18));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> validatePassword());
        formPanel.add(new JLabel()); // Empty cell
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

    private void validatePassword() {
        String enteredPassword = new String(passwordField.getPassword()).trim();
        if (enteredPassword.equals(MANAGER_PASSWORD)) {
            passwordField.setText("");
            cardLayout.show(cardPanel, "managerPanel");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(new Color(255, 221, 153));
        button.setForeground(new Color(102, 51, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
    }
}