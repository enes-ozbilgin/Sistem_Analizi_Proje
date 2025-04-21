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

public class WriterPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WriterPanel(CardLayout cardLayout, JPanel cardPanel) {
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
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20)); // 2 rows, 1 col, 20px vertical gap
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));

        JButton loginButton = new JButton("Log In");
        styleButton(loginButton);
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "writerLoginPanel"));

        buttonPanel.add(loginButton);

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

    // Helper method to style buttons consistently
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setBackground(new Color(255, 221, 153));
        button.setForeground(new Color(102, 51, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
    }
}