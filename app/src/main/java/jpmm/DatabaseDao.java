package jpmm;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseDao {

    private AccountModel accountModel;
    private Connection connection;
    private String sqlLiteDatabasePath;

    DatabaseDao() {
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

    public ArrayList<AccountModel> getAllAccounts() {

        String account;
        String username;
        String password;
        ArrayList<AccountModel> accounts = new ArrayList<>();
        String queryString = "SELECT * FROM passwords";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                account = rs.getString("Account");
                username = rs.getString("Username");
                password = rs.getString("Password");

                accounts.add(new AccountModel(account, username, password));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public AccountModel getAccountFromAccountName() {

        return new AccountModel();
    }

}