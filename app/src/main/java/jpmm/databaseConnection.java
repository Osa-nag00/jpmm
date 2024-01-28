package jpmm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {

    public Connection connection;
    private String sqlLiteDatabasePath;

    databaseConnection() {
        connection = null;

        // gets the absolute path of the sqlite *.db file
        sqlLiteDatabasePath = (App.class.getClassLoader().getResource("db/passwords.db")).toString();

        // Need to add this prefix for the connection to occur
        // when dealing with sqlite database
        sqlLiteDatabasePath = "jdbc:sqlite:" + sqlLiteDatabasePath;

        try {
            connection = DriverManager.getConnection(sqlLiteDatabasePath.toString());
        } catch (SQLException e) {

            System.err.println("COULD NOT MAKE CONNECTION TO SQLITE DATABASE");
            e.printStackTrace();
        }
    }

    /*
     * Closes the class database connection
     */
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("COULD NOT CLOSE CONNECTION");
        }
    }
}