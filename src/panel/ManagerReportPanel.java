package panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import core.Book;
import core.BookDAO;
import core.FeedbackDAO;
import core.WriterDAO;
import core.Writer;
import java.util.List;

public class ManagerReportPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private FeedbackDAO feedbackDAO;
    private WriterDAO writerDAO;
    private BookDAO bookDAO;

    public ManagerReportPanel(CardLayout cardLayout, JPanel cardPanel, FeedbackDAO feedbackDAO, 
                             WriterDAO writerDAO, BookDAO bookDAO) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.feedbackDAO = feedbackDAO;
        this.writerDAO = writerDAO;
        this.bookDAO = bookDAO;

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel title = new JLabel("Top 10 Books by Average Score", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Table to display top 10 books
        DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Book Name", "Author", "Average Score", "Description"}, 0) {
            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setBackground(new Color(255, 250, 240));
        table.setGridColor(new Color(102, 51, 0));
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        table.getTableHeader().setForeground(new Color(102, 51, 0));
        table.getTableHeader().setBackground(new Color(255, 221, 153));

        // Populate table with data
        populateTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Manager Panel");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
        backButton.setBackground(new Color(255, 221, 153));
        backButton.setForeground(new Color(102, 51, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "managerPanel"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void populateTable(DefaultTableModel tableModel) {
        try {
            // Get top 10 books
            List<Book> topBooks = feedbackDAO.getTopRatedBooks(bookDAO);
            if (topBooks.isEmpty()) {
                JLabel noDataLabel = new JLabel("No books with ratings found.", SwingConstants.CENTER);
                noDataLabel.setFont(new Font("Serif", Font.PLAIN, 18));
                noDataLabel.setForeground(new Color(102, 51, 0));
                add(noDataLabel, BorderLayout.CENTER);
                remove(getComponent(1)); // Remove scrollPane
                System.out.println("populateTable: No rated books found.");
                return;
            }

            // Populate table
            for (Book book : topBooks) {
                Writer author = writerDAO.read(book.getAuthorId());
                String authorName = (author != null) ? author.getName() : "Unknown Author";
                tableModel.addRow(new Object[]{
                    book.getName(),
                    authorName,
                    String.format("%.2f", book.getAvgScore()),
                    book.getDescription()
                });
            }
        } catch (Exception e) {
            System.err.println("Error in populateTable: " + e.getMessage());
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading book ratings: " + e.getMessage(), SwingConstants.CENTER);
            errorLabel.setFont(new Font("Serif", Font.PLAIN, 18));
            errorLabel.setForeground(new Color(102, 51, 0));
            add(errorLabel, BorderLayout.CENTER);
            remove(getComponent(1)); // Remove scrollPane
        }
    }
}