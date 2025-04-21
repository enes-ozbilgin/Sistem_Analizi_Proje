package panel;

import javax.swing.*;

import core.BookDAO;
import core.FeedbackDAO;
import core.Reader;
import core.ReaderDAO;
import core.WriterDAO;

import java.awt.*;

public class ReaderLoginPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;
    private String enteredUsername; // Store authenticated username
    private ReaderDAO readerDAO;

    public ReaderLoginPanel(CardLayout cardLayout, JPanel cardPanel, ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Reader Login", SwingConstants.CENTER);
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
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("Login attempt: Empty username or password");
                JOptionPane.showMessageDialog(this, "Please enter both fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } 
            
            Reader reader = readerDAO.read(username, password);
            if (reader != null) {
                enteredUsername = username; // Store authenticated username
                System.out.println("Login successful: Username=" + username + ", Reader ID=" + reader.getId());
                JOptionPane.showMessageDialog(this, "Login successful for " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardPanel.add(new ReaderMainPanel(cardLayout, cardPanel, readerDAO, new BookDAO(readerDAO.getConnection()),
                                                 new WriterDAO(readerDAO.getConnection()), new FeedbackDAO(readerDAO.getConnection()), this), "readerMainPanel");
                cardLayout.show(cardPanel, "readerMainPanel");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                System.out.println("Login failed: Username=" + username);
                JOptionPane.showMessageDialog(this, "Wrong username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        formPanel.add(new JLabel()); // empty cell for spacing
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            enteredUsername = null; // Reset on logout
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(cardPanel, "mainMenu");
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Helper method to style labels
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setForeground(new Color(102, 51, 0));
    }

    // Helper method to style buttons
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(new Color(255, 221, 153));
        button.setForeground(new Color(102, 51, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
    }
    
    public String getEnteredUsername() {
        return enteredUsername; // Return stored username, not field text
    }
}
