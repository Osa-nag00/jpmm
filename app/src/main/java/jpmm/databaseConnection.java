package jpmm;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {

    Connection connection;
    private URL sqlLiteDatabasePath;

    databaseConnection() {
        connection = null;

        // gets the absolute path of the sqlite *.db file
        sqlLiteDatabasePath = App.class.getClassLoader().getResource("db/passwords.db");

        try {
            connection = DriverManager.getConnection(sqlLiteDatabasePath.toString());
        } catch (SQLException e) {
            System.err.println("COULD NOT MAKE CONNECTION TO SQLITE DATABASE");
            e.printStackTrace();
        }

    }

}