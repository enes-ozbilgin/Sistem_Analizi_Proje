package panel;

import javax.swing.*;

import core.Book;
import core.BookDAO;
import core.Writer;
import core.WriterDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class AddBookPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JTextField emailField;
    private JTextField nameField;
    private JPasswordField passwordField;

    public AddBookPanel(CardLayout cardLayout, JPanel cardPanel, WriterDAO writerDAO, BookDAO bookDAO) {

        Font textFieldFont = new Font("Georgia", Font.BOLD, 26);

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Add New Book", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 240, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Release Date label
        JLabel releaseDateLabel = new JLabel("Release Date:");
        releaseDateLabel.setFont(textFieldFont);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(releaseDateLabel, gbc);
        formPanel.setLayout(new GridLayout(7, 2, 5, 10)); // 7 rows (or however many you need)


        // Date selection panel with combo boxes
        JComboBox<Integer> dayCombo = new JComboBox<>();
        JComboBox<Integer> monthCombo = new JComboBox<>();
        JComboBox<Integer> yearCombo = new JComboBox<>();

        for (int i = 1; i <= 12; i++) monthCombo.addItem(i);
        for (int i = 1900; i <= java.time.LocalDate.now().getYear(); i++) yearCombo.addItem(i);

        ActionListener updateDaysListener = e -> {
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            int selectedMonth = (Integer) monthCombo.getSelectedItem();
            int daysInMonth = java.time.YearMonth.of(selectedYear, selectedMonth).lengthOfMonth();

            int previousSelectedDay = dayCombo.getSelectedItem() != null
                    ? (Integer) dayCombo.getSelectedItem() : 1;

            dayCombo.removeAllItems();
            for (int d = 1; d <= daysInMonth; d++) {
                dayCombo.addItem(d);
            }

            if (previousSelectedDay <= daysInMonth) {
                dayCombo.setSelectedItem(previousSelectedDay);
            } else {
                dayCombo.setSelectedItem(daysInMonth);
            }
        };

        monthCombo.addActionListener(updateDaysListener);
        yearCombo.addActionListener(updateDaysListener);

        yearCombo.setSelectedItem(java.time.LocalDate.now().getYear());
        monthCombo.setSelectedItem(java.time.LocalDate.now().getMonthValue());
        updateDaysListener.actionPerformed(null);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(new Color(245, 240, 230));
        datePanel.add(dayCombo);
        datePanel.add(monthCombo);
        datePanel.add(yearCombo);

        gbc.gridx = 1;
        formPanel.add(datePanel, gbc);
        
     // Author ID
        JLabel authorUsernameLabel = new JLabel("Author Username:");
        authorUsernameLabel.setFont(textFieldFont);
        formPanel.add(authorUsernameLabel);

        JTextField authorUsernameField = new JTextField();
        authorUsernameField.setFont(textFieldFont);
        formPanel.add(authorUsernameField);

        // Book Name
        JLabel bookNameLabel = new JLabel("Book Name:");
        bookNameLabel.setFont(textFieldFont);
        formPanel.add(bookNameLabel);

        JTextField bookNameField = new JTextField();
        bookNameField.setFont(textFieldFont);
        formPanel.add(bookNameField);

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(textFieldFont);
        formPanel.add(descriptionLabel);

        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(textFieldFont);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descriptionScrollPane);


        add(formPanel, BorderLayout.CENTER);
        
     // Submit Button
        JButton submitButton = new JButton("Add Book");
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
                String username = authorUsernameField.getText().trim();
                String name = bookNameField.getText().trim();
                String description = descriptionArea.getText();
                
                int day = (Integer) dayCombo.getSelectedItem();
                int month = (Integer) monthCombo.getSelectedItem();
                int year = (Integer) yearCombo.getSelectedItem();

                // Create a Calendar instance
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1); // Calendar.MONTH is 0-based
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // Convert to Date
                Date selectedDate = calendar.getTime();

                
                ArrayList<Writer> writers = (ArrayList<Writer>) writerDAO.getAll();

                // Validate the fields
                if (username.isEmpty() || description.isEmpty() || name.isEmpty() || dayCombo.getSelectedItem() == null || 
                		monthCombo.getSelectedItem() == null || yearCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }
                
                Writer writer = writerDAO.read(username);
                
                if (writer == null) {
                	JOptionPane.showMessageDialog(null, "No writer with this username exists.");
                	authorUsernameField.setText("");
                    return;
                }

                // Create a new Book object
                Book book = new Book(writer.getId(), name, selectedDate, description);
               

                // Add writer to the database using the WriterDAO
                boolean isAdded = bookDAO.addBook(book);
                if (isAdded) {
                    JOptionPane.showMessageDialog(null, "Book added successfully.");
                    // Optionally, clear the form
                    authorUsernameField.setText("");
                    bookNameField.setText("");
                    descriptionArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error adding book. Please try again.");
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
