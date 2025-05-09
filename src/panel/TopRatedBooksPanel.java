package panel;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import core.FeedbackDAO;
import core.BookDAO;
import core.Book;

public class TopRatedBooksPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TopRatedBooksPanel(CardLayout cardLayout, JPanel cardPanel, FeedbackDAO feedbackDAO, BookDAO bookDAO) {
        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Top 10 Rated Books", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        add(title, BorderLayout.NORTH);

        JTextArea topBooksArea = new JTextArea();
        topBooksArea.setFont(new Font("Serif", Font.PLAIN, 18));
        topBooksArea.setEditable(false);
        topBooksArea.setBackground(new Color(255, 250, 240));
        topBooksArea.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1, true));
        JScrollPane scrollPane = new JScrollPane(topBooksArea);
        add(scrollPane, BorderLayout.CENTER);

        ArrayList<Book> topBooks = (ArrayList<Book>) feedbackDAO.getTopRatedBooks(bookDAO);
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (Book bas : topBooks) {
            sb.append(rank++).append(". ")
              .append(bas.getName())
              .append(" - Avg. Score: ").append(String.format("%.2f", bas.getAvgScore()))
              .append("\n");
        }
        if (topBooks.isEmpty()) {
            sb.append("No reviews yet.");
        }
        topBooksArea.setText(sb.toString());

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Serif", Font.BOLD, 16));
        backButton.setBackground(new Color(255, 221, 153));
        backButton.setForeground(new Color(102, 51, 0));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "readerMainPanel"));

        add(backButton, BorderLayout.SOUTH);
    }
}

