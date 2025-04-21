package panel;

import javax.swing.*;

import core.Writer;
import core.WriterDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AddWriterPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JTextField emailField;
    private JTextField nameField;
    private JPasswordField passwordField;

    public AddWriterPanel(CardLayout cardLayout, JPanel cardPanel, WriterDAO writerDAO) {
    	
    	Font textFieldFont = new Font("Georgia", Font.BOLD, 26);
    	
    	setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Add New Writer", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Form Panel for entering writer's information
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 5, 10));
        formPanel.setBackground(new Color(245, 240, 230));

        // Username field
        JLabel usernameText = new JLabel("Username:");
        usernameText.setFont(textFieldFont);
        formPanel.add(usernameText);
        usernameField = new JTextField();
        formPanel.add(usernameField);

        // Email field
        JLabel emailText = new JLabel("Email");
        emailText.setFont(textFieldFont);
        formPanel.add(emailText);
        emailField = new JTextField();
        formPanel.add(emailField);

        // Name field
        JLabel nameText = new JLabel("Name:");
        nameText.setFont(textFieldFont);
        formPanel.add(nameText);
        nameField = new JTextField();
        formPanel.add(nameField);

        // Password field
        JLabel passwordText = new JLabel("Password");
        passwordText.setFont(textFieldFont);
        formPanel.add(passwordText);
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // Submit Button
        JButton submitButton = new JButton("Add Writer");
        submitButton.setFont(new Font("Serif", Font.BOLD, 18));
        submitButton.setBackground(new Color(255, 221, 153));
        submitButton.setForeground(new Color(102, 51, 0));
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));

        // Action Listener to add the writer to the database
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String name = nameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                
                ArrayList<Writer> writers = (ArrayList<Writer>) writerDAO.getAll();

                // Validate the fields
                if (username.isEmpty() || email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }
                
                //Check if the writer with same email or user name exists
                for (Writer writer : writers) {
                    if (writer.getUsername().equals(username) || 
                        writer.getEmail().equals(email)) {
                    	JOptionPane.showMessageDialog(null, "Writer with same email or username already exists.");
                    	usernameField.setText("");
                        emailField.setText("");
                        nameField.setText("");
                        passwordField.setText("");
                    	return;
                    }
                }

                // Create a new Writer object
                Writer writer = new Writer(username, email, password, name);

                // Add writer to the database using the WriterDAO
                boolean isAdded = writerDAO.create(writer);
                if (isAdded) {
                    JOptionPane.showMessageDialog(null, "Writer added successfully.");
                    // Optionally, clear the form
                    usernameField.setText("");
                    emailField.setText("");
                    nameField.setText("");
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error adding writer. Please try again.");
                }
            }
        });

        // Manager Panel Button
        JButton managerPanelButton = new JButton("Manager Panel");
        managerPanelButton.setFont(new Font("Serif", Font.BOLD, 18));
        managerPanelButton.setBackground(new Color(255, 221, 153));
        managerPanelButton.setForeground(new Color(102, 51, 0));
        managerPanelButton.setFocusPainted(false);
        managerPanelButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        managerPanelButton.addActionListener(e -> cardLayout.show(cardPanel, "managerPanel"));

        // Create a panel for the buttons and use FlowLayout for horizontal arrangement
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));  // Centered with horizontal gap of 20px
        buttonPanel.setBackground(new Color(245, 240, 230));

        // Add buttons to the button panel
        buttonPanel.add(submitButton);
        buttonPanel.add(managerPanelButton);

        // Add button panel to the bottom of the main panel
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
