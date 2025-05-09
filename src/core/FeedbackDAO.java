package core;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    private Connection connection;

    public FeedbackDAO(Connection connection) {
        this.connection = connection;
    }

    // 1. Create: Add a new feedback
    public boolean create(Feedback feedback) {
        String query = "INSERT INTO feedback (reader_id, book_id, score, review_date, comment) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, feedback.getReaderId());
            stmt.setInt(2, feedback.getBookId());
            stmt.setInt(3, feedback.getScore());
            stmt.setTimestamp(4, Timestamp.valueOf(feedback.getReviewDate())); 
            stmt.setString(5, feedback.getComment());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Read: Get feedback by id
    public Feedback read(int id) {
        String query = "SELECT * FROM feedback WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Feedback(
                    rs.getInt("id"),
                    rs.getInt("reader_id"),
                    rs.getInt("book_id"),
                    rs.getInt("score"),
                    rs.getTimestamp("review_date").toLocalDateTime(), 
                    rs.getString("comment")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Update: Update an existing feedback's score and comment
    public boolean update(Feedback feedback) {
        String query = "UPDATE feedback SET score = ?, comment = ?, review_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, feedback.getScore());
            stmt.setString(2, feedback.getComment());
            stmt.setTimestamp(3, Timestamp.valueOf(feedback.getReviewDate())); 
            stmt.setInt(4, feedback.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Delete: Remove a feedback by id
    public boolean delete(int id) {
        String query = "DELETE FROM feedback WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Get all feedback
    public List<Feedback> getAll() {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                feedbackList.add(new Feedback(
                    rs.getInt("id"),
                    rs.getInt("reader_id"),
                    rs.getInt("book_id"),
                    rs.getInt("score"),
                    rs.getTimestamp("review_date").toLocalDateTime(), 
                    rs.getString("comment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    // 6. Get all feedbacks for a specific book
    public List<Feedback> getByBookId(int bookId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                feedbackList.add(new Feedback(
                    rs.getInt("id"),
                    rs.getInt("reader_id"),
                    rs.getInt("book_id"),
                    rs.getInt("score"),
                    rs.getTimestamp("review_date").toLocalDateTime(), 
                    rs.getString("comment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
    
    public List<Book> getTopRatedBooks(BookDAO bookDAO) {
        List<Book> topBooks = new ArrayList<>();
        String sql = "SELECT book_id, AVG(score) as avg_score FROM feedback GROUP BY book_id ORDER BY avg_score DESC LIMIT 10";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                double avgScore = rs.getDouble("avg_score");
                Book book = bookDAO.getBookById(bookId);
                book.setAvgScore(avgScore);
                if (book != null) {
                    topBooks.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topBooks;
    }

}
