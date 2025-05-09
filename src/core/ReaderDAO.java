package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {
    private Connection connection;

    public ReaderDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addReader(Reader reader) {
        String query = "INSERT INTO readers (username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, reader.getUsername());
            ps.setString(2, reader.getMail());
            ps.setString(3, reader.getPassword());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Reader read(String username, String password) {
    	String query = "SELECT * FROM readers WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Reader(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public Reader read(String username) {
    	String query = "SELECT * FROM readers WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Reader(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteReader(int id) {
        String query = "DELETE FROM readers WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Reader> getAll() {
        List<Reader> readers = new ArrayList<>();
        String query = "SELECT * FROM readers";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                readers.add(new Reader(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;  // Return list of all readers
    }

	public Connection getConnection() {
		return connection;
	}
}

