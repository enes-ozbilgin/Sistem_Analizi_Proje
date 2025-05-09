package core;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private Connection connection;


    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    // Add new book
    public boolean addBook(Book book) {
        String query = "INSERT INTO books (author_id, name, release_date, description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, book.getAuthorId());
            ps.setString(2, book.getName());
            ps.setDate(3, new java.sql.Date(book.getReleaseDate().getTime())); // LocalDate -> java.sql.Date
            ps.setString(4, book.getDescription());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update book by id
    public boolean updateBook(Book book) {
        String query = "UPDATE books SET author_id = ?, name = ?, release_date = ?, description = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, book.getAuthorId());
            ps.setString(2, book.getName());
            ps.setDate(3, (Date)(book.getReleaseDate()));
            ps.setString(4, book.getDescription());
            ps.setInt(5, book.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete book by id
    public boolean deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a book by name
    public Book getBookByName(String name) {
        String query = "SELECT * FROM books WHERE name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getDate("release_date"),
                        rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    // Get a book by name
    public Book getBookById(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("name"),
                        rs.getDate("release_date"),
                        rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getInt("author_id"),
                    rs.getString("name"),
                    rs.getDate("release_date"),
                    rs.getString("description")
                );
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList;
    }
}
