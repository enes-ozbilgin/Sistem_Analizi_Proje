package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WriterDAO {
    private Connection connection;

    public WriterDAO(Connection connection) {
        this.connection = connection;
    }

    // 1. Create: Add a new writer
    public boolean create(Writer writer) {
        String query = "INSERT INTO authors (username, email, password, name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, writer.getUsername());
            stmt.setString(2, writer.getEmail());
            stmt.setString(3, writer.getPassword());
            stmt.setString(4, writer.getName()); // Fixed: Use getName() instead of getPassword()
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating writer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 2. Read: Get a writer by username
    public Writer read(String username) {
        String query = "SELECT * FROM authors WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Writer(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error reading writer by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public Writer read(int id) {
        String query = "SELECT * FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Writer(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error reading writer by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 3. Update: Modify an existing writer's data
    public boolean update(Writer writer) {
        String query = "UPDATE authors SET username = ?, email = ?, name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, writer.getUsername());
            stmt.setString(2, writer.getEmail());
            stmt.setString(3, writer.getName());
            stmt.setInt(4, writer.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating writer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 4. Delete: Remove a writer from the database
    public boolean delete(int id) {
        String query = "DELETE FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting writer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 5. Get all writers
    public List<Writer> getAll() {
        List<Writer> writers = new ArrayList<>();
        String query = "SELECT * FROM authors";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                writers.add(new Writer(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all writers: " + e.getMessage());
            e.printStackTrace();
        }
        return writers;
    }

    // 6. Read: Get writer by username and password
    public Writer read(String username, String password) {
        String query = "SELECT * FROM authors WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Found writer: id=" + rs.getInt("id") + ", username=" + rs.getString("username"));
                    return new Writer(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name")
                    );
                } else {
                    System.out.println("No writer found for username=" + username);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in read(username, password): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    // Updated: Get books by author with average rating
    public List<Book> getBooksByAuthor(int authorId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id, b.author_id, b.name, b.release_date, b.description, " +
                      "COALESCE(AVG(f.score), 0) AS average_rating, COUNT(f.score) AS review_count " +
                      "FROM books b LEFT JOIN feedback f ON b.id = f.book_id " +
                      "WHERE b.author_id = ? " +
                      "GROUP BY b.id, b.author_id, b.name, b.release_date, b.description " +
                      "ORDER BY average_rating DESC";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getDate("release_date"),
                        rs.getString("description")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching books for authorId=" + authorId + ": " + e.getMessage());
            e.printStackTrace();
            return books; // Return empty list instead of null
        }
        System.out.println("getBooksByAuthor: authorId=" + authorId + ", books=" + books.size());
        return books;
    }
}
