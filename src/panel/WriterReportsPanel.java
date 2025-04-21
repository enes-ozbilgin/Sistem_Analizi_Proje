package panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import core.Book;
import core.Feedback;
import core.FeedbackDAO;
import core.WriterDAO;

public class WriterReportsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private WriterDAO writerDAO;
    private FeedbackDAO feedbackDAO;
    private int writerId;

    public WriterReportsPanel(CardLayout cardLayout, JPanel cardPanel, WriterDAO writerDAO, 
                             FeedbackDAO feedbackDAO, int writerId) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.writerDAO = writerDAO;
        this.feedbackDAO = feedbackDAO;
        this.writerId = writerId;

        setBackground(new Color(245, 240, 230));
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel title = new JLabel("Book Ratings Report", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 26));
        title.setForeground(new Color(102, 51, 0));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Table to display books and ratings
        DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Book Name", "Average Rating", "Number of Reviews", "Release Date"}, 0) {
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

        // Populate table with book data
        populateTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back to Writer Panel");
        backButton.setFont(new Font("Serif", Font.BOLD, 18));
        backButton.setBackground(new Color(255, 221, 153));
        backButton.setForeground(new Color(102, 51, 0));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(102, 51, 0), 2, true));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "writerMainPanel"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 240, 230));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void populateTable(DefaultTableModel tableModel) {
        try {
            // Get author's books
            List<Book> books = writerDAO.getBooksByAuthor(writerId);
            if (books == null || books.isEmpty()) {
                JLabel noDataLabel = new JLabel("No books found for this author.", SwingConstants.CENTER);
                noDataLabel.setFont(new Font("Serif", Font.PLAIN, 18));
                noDataLabel.setForeground(new Color(102, 51, 0));
                add(noDataLabel, BorderLayout.CENTER);
                remove(getComponent(1)); // Remove scrollPane
                System.out.println("populateTable: No books found for writerId=" + writerId);
                return;
            }

            List<BookReport> reports = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Book book : books) {
                List<Feedback> feedbackList = feedbackDAO.getByBookId(book.getId());
                double averageRating = 0.0;
                int reviewCount = feedbackList.size();
                if (!feedbackList.isEmpty()) {
                    double totalScore = 0.0;
                    for (Feedback feedback : feedbackList) {
                        totalScore += feedback.getScore();
                    }
                    averageRating = totalScore / reviewCount;
                }
                String releaseDate = book.getReleaseDate() != null ? 
                                    dateFormat.format(book.getReleaseDate()) : "N/A";
                reports.add(new BookReport(book.getName(), averageRating, reviewCount, releaseDate));
            }

            // Sort reports by average rating (desc), review count (desc), name (asc)
            reports.sort(Comparator.comparingDouble(BookReport::getAverageRating).reversed()
                          .thenComparingInt(BookReport::getReviewCount).reversed()
                          .thenComparing(BookReport::getBookName));

            // Populate table
            for (BookReport report : reports) {
                tableModel.addRow(new Object[]{
                    report.getBookName(),
                    String.format("%.2f", report.getAverageRating()),
                    report.getReviewCount(),
                    report.getReleaseDate()
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

    // Helper class to store report data
    private static class BookReport {
        private String bookName;
        private double averageRating;
        private int reviewCount;
        private String releaseDate;

        public BookReport(String bookName, double averageRating, int reviewCount, String releaseDate) {
            this.bookName = bookName;
            this.averageRating = averageRating;
            this.reviewCount = reviewCount;
            this.releaseDate = releaseDate;
        }

        public String getBookName() { return bookName; }
        public double getAverageRating() { return averageRating; }
        public int getReviewCount() { return reviewCount; }
        public String getReleaseDate() { return releaseDate; }
    }
}