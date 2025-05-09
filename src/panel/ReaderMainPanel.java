package panel;

import javax.swing.*;
import core.Book;
import core.BookDAO;
import core.EventDAO;
import core.Feedback;
import core.FeedbackDAO;
import core.Reader;
import core.ReaderDAO;
import core.Writer;
import core.WriterDAO;
import java.awt.*;
import java.time.LocalDateTime;

public class ReaderMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultsArea;
    private int selectedScore = 0;
    private JButton[] starButtons = new JButton[5];
    private ImageIcon starPressedIcon;
    private ImageIcon starNotPressedIcon;

    public ReaderMainPanel(CardLayout cardLayout, JPanel cardPanel, ReaderDAO readerDAO, 
                          BookDAO bookDAO, WriterDAO writerDAO, FeedbackDAO feedbackDAO, ReaderLoginPanel readerLogin) {
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(20, 20));

        // Title Label
        JLabel title = new JLabel("Reader Panel", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Top Search Bar Panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(new Color(245, 240, 230));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        searchField = new JTextField();
        searchField.setFont(new Font("Serif", Font.PLAIN, 18));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));

        searchButton = new JButton("Search Book");
        searchButton.setFont(new Font("Serif", Font.BOLD, 18));
        searchButton.setBackground(new Color(255, 221, 153));
        searchButton.setForeground(new Color(102, 51, 0));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        
        searchButton.addActionListener(e -> {
            String searchedName = getSearchField().getText();
            Book searchedBook = bookDAO.getBookByName(searchedName);
            if (searchedBook != null) {
                StringBuilder resultText = new StringBuilder();
                resultText.append("Book Name: ").append(searchedBook.getName()).append("\n");
                
                Writer writer = writerDAO.read(searchedBook.getAuthorId());
                resultText.append("Author: ").append(writer.getName()).append("\n");
                
                resultText.append("Release Date: ").append(searchedBook.getReleaseDate()).append("\n");
                resultText.append("Description: ").append(searchedBook.getDescription()).append("\n");

                getResultsArea().setText(resultText.toString());
            } else {
                getResultsArea().setText("No book found with that name.");
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        add(searchPanel, BorderLayout.NORTH);

        // Create the text area
        resultsArea = new JTextArea();
        resultsArea.setFont(new Font("Serif", Font.PLAIN, 16));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setEditable(false);
        resultsArea.setBackground(new Color(255, 250, 240));
        resultsArea.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1, true));

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1, true));

        // Panel to hold results and comment area together
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(245, 240, 230));
        centerPanel.setLayout(new BorderLayout(10, 10));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Comment Label and Text Area
        JLabel commentLabel = new JLabel("Your Comment:");
        commentLabel.setFont(new Font("Serif", Font.BOLD, 18));
        commentLabel.setForeground(new Color(102, 51, 0));

        JTextArea commentArea = new JTextArea(3, 20);
        commentArea.setFont(new Font("Serif", Font.PLAIN, 16));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1, true));
        
        // Load icons
        starPressedIcon = new ImageIcon(ReaderMainPanel.class.getResource("starbutton.jpg"));
        Image pressedImage = starPressedIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon pressedScaledIcon = new ImageIcon(pressedImage);
        starNotPressedIcon = new ImageIcon(ReaderMainPanel.class.getResource("starbuttonnotpressed.png"));
        Image notPressedImage = starNotPressedIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon notPressedScaledIcon = new ImageIcon(notPressedImage);

        // Panel for stars
        JPanel starPanel = new JPanel();
        starPanel.setBackground(new Color(245, 240, 230));
        starPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        JLabel scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(102, 51, 0));
        starPanel.add(scoreLabel);

        for (int i = 0; i < 5; i++) {
            final int starIndex = i;
            starButtons[i] = new JButton(starNotPressedIcon);
            starButtons[i].setContentAreaFilled(false);
            starButtons[i].setBorderPainted(false);
            starButtons[i].setFocusPainted(false);
            starButtons[i].setPreferredSize(new Dimension(35, 35));
            starButtons[i].addActionListener(e -> {
                selectedScore = starIndex + 1;
                updateStarIcons(pressedScaledIcon, notPressedScaledIcon);
            });
            starPanel.add(starButtons[i]);
        }

        // Submit Button
        JButton submitCommentButton = new JButton("Submit Comment");
        submitCommentButton.setFont(new Font("Serif", Font.BOLD, 16));
        submitCommentButton.setBackground(new Color(255, 221, 153));
        submitCommentButton.setForeground(new Color(102, 51, 0));
        submitCommentButton.setFocusPainted(false);
        submitCommentButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));

        submitCommentButton.addActionListener(e -> {
            String comment = commentArea.getText();
            String searchedName = getSearchField().getText();
            Book searchedBook = bookDAO.getBookByName(searchedName);
            String readerUsername = readerLogin.getEnteredUsername();
            Reader reader = readerDAO.read(readerUsername);
            LocalDateTime now = LocalDateTime.now();
            int score = selectedScore; 
            if (searchedBook != null && reader != null) {
                if (!comment.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Comment submitted:\n" + comment, "Thank You!", JOptionPane.INFORMATION_MESSAGE);
                    commentArea.setText("");
                    Feedback feedback = new Feedback(reader.getId(), searchedBook.getId(), score, now, comment);
                    boolean isAdded = feedbackDAO.create(feedback);
                    if (isAdded) {
                        JOptionPane.showMessageDialog(null, "Review added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error adding review. Please try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a comment before submitting.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                System.out.println("Submit comment failed: Book=" + (searchedBook != null ? searchedBook.getName() : "null") + 
                                   ", Reader=" + (reader != null ? reader.getId() : "null") + 
                                   ", Username=" + readerUsername);
                JOptionPane.showMessageDialog(this, "Error: Invalid book or reader!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel for comment and button
        JPanel commentPanel = new JPanel();
        commentPanel.setBackground(new Color(245, 240, 230));
        commentPanel.setLayout(new BorderLayout(5, 5));
        commentPanel.add(commentLabel, BorderLayout.NORTH);
        commentPanel.add(new JScrollPane(commentArea), BorderLayout.CENTER);
        commentPanel.add(starPanel, BorderLayout.NORTH);
        commentPanel.add(submitCommentButton, BorderLayout.SOUTH);
        
        centerPanel.add(commentPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Create individual buttons
        JButton eventsButton = new JButton("View Available Events");
        eventsButton.setFont(new Font("Serif", Font.BOLD, 16));
        eventsButton.setBackground(new Color(255, 221, 153));
        eventsButton.setForeground(new Color(102, 51, 0));
        eventsButton.setFocusPainted(false);
        eventsButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        eventsButton.addActionListener(e -> {
            String readerUsername = readerLogin.getEnteredUsername();
            Reader reader = readerDAO.read(readerUsername);
            if (reader != null && readerUsername != null && !readerUsername.isEmpty()) {
                System.out.println("Navigating to AddEventPanel: Username=" + readerUsername + ", Reader ID=" + reader.getId());
                cardPanel.add(new AddEventPanel(cardLayout, cardPanel, new EventDAO(readerDAO.getConnection()), reader.getId()), "addEventPanel");
                cardLayout.show(cardPanel, "addEventPanel");
            } else {
                System.out.println("Failed to navigate to AddEventPanel: Username=" + readerUsername + 
                                   ", Reader=" + (reader != null ? reader.getId() : "null"));
                JOptionPane.showMessageDialog(this, "Error: Invalid reader or username not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton topRatedButton = new JButton("View Top-Rated Books");
        topRatedButton.setFont(new Font("Serif", Font.BOLD, 16));
        topRatedButton.setBackground(new Color(255, 221, 153));
        topRatedButton.setForeground(new Color(102, 51, 0));
        topRatedButton.setFocusPainted(false);
        topRatedButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        
        topRatedButton.addActionListener(e -> {
            TopRatedBooksPanel topBooksPanel = new TopRatedBooksPanel(cardLayout, cardPanel, feedbackDAO, bookDAO);
            cardPanel.add(topBooksPanel, "topRated");
            cardLayout.show(cardPanel, "topRated");
        });

        JButton logOffButton = new JButton("Log Off");
        logOffButton.setFont(new Font("Serif", Font.BOLD, 16));
        logOffButton.setBackground(new Color(255, 221, 153));
        logOffButton.setForeground(new Color(102, 51, 0));
        logOffButton.setFocusPainted(false);
        logOffButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        logOffButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "mainMenu");
        });

        // Create a panel for the buttons and add them
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 240, 230));
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 0));
        buttonPanel.add(eventsButton);
        buttonPanel.add(topRatedButton);
        buttonPanel.add(logOffButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextArea getResultsArea() {
        return resultsArea;
    }
    
    private void updateStarIcons(ImageIcon pressedScaledIcon, ImageIcon notPressedScaledIcon) {
        for (int i = 0; i < 5; i++) {
            if (i < selectedScore) {
                starButtons[i].setIcon(pressedScaledIcon);
            } else {
                starButtons[i].setIcon(notPressedScaledIcon);
            }
        }
    }
}
