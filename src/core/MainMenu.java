package core;
import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainMenu(CardLayout cardLayout, JPanel cardPanel) {
        // Set warm background color
        setBackground(new Color(245, 240, 230));  // soft beige
        setLayout(new BorderLayout(20, 20));

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome to Yellow Petal Publishing", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(102, 51, 0)); // rich brown
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20)); // 3 rows, 1 column, gaps
        buttonPanel.setBackground(new Color(245, 240, 230)); // match background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80)); // padding around buttons

        // Create styled buttons
        JButton readerButton = createStyledButton("Reader");
        readerButton.addActionListener(e -> cardLayout.show(cardPanel, "readerPanel"));

        JButton managerButton = createStyledButton("Manager");
        managerButton.addActionListener(e -> cardLayout.show(cardPanel, "managerLoginPanel"));

        JButton writerButton = createStyledButton("Writer");
        writerButton.addActionListener(e -> cardLayout.show(cardPanel, "writerPanel"));

        // Add buttons to the panel
        buttonPanel.add(readerButton);
        buttonPanel.add(managerButton);
        buttonPanel.add(writerButton);

        // Center the button panel
        add(buttonPanel, BorderLayout.CENTER);
    }

    // Helper method to style buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setBackground(new Color(255, 221, 153)); // warm light orange
        button.setForeground(new Color(102, 51, 0)); // rich brown
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }
}