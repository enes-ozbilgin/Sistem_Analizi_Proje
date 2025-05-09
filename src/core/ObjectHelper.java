package core;
import java.sql.*;

public class ObjectHelper {
		//This class is to create database connections in other classes.
        private static final String URL = "jdbc:mysql://localhost:3306/mydb";
        private static final String USER = "root";
        private static final String PASSWORD = "03Emin30!";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
}

