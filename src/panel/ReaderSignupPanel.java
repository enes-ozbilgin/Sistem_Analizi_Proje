package panel;

import javax.swing.*;
import core.Reader;
import core.ReaderDAO;
import java.awt.*;
import java.util.ArrayList;

public class ReaderSignupPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public ReaderSignupPanel(CardLayout cardLayout, JPanel cardPanel, ReaderDAO readerDAO) {
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Reader Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 230));
        formPanel.setLayout(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JLabel usernameLabel = new JLabel("Username:");
        styleLabel(usernameLabel);
        JTextField usernameField = new JTextField();

        JLabel mailLabel = new JLabel("Mail:");
        styleLabel(mailLabel);
        JTextField mailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);
        JPasswordField passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(mailLabel);
        formPanel.add(mailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Sign Up Button
        JButton signUpButton = new JButton("Sign Up");
        styleButton(signUpButton);
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String mail = mailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            ArrayList<Reader> readers = (ArrayList<Reader>)readerDAO.getAll();

            if (username.isEmpty() || mail.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Reader reader : readers) {
                if (reader.getUsername().equals(username) || reader.getMail().equals(mail)) {
                    JOptionPane.showMessageDialog(this, "Reader with same email or username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    usernameField.setText("");
                    mailField.setText("");
                    passwordField.setText("");
                    return;
                }
            }

            // Create a new Reader object
            Reader reader = new Reader(username, mail, password);

            // Add reader to the database using the ReaderDAO
            boolean isAdded = readerDAO.addReader(reader);
            if (isAdded) {
                JOptionPane.showMessageDialog(this, "Sign up successful for " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear the form
                usernameField.setText("");
                mailField.setText("");
                passwordField.setText("");
                // Navigate to mainMenu
                cardLayout.show(cardPanel, "mainMenu");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding reader. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel());
        formPanel.add(signUpButton);

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
}