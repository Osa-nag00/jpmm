package com.jpmm.app;

import java.sql.DriverManager;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.jpmm.app.utils.passwordUtil;

import java.sql.PreparedStatement;

public class DatabaseDao {

    private Connection connection;
    private String sqlitePathToDatabase;

    /**
     * Default constructor, makes connection to database on instantiation
     */
    DatabaseDao() {

        connection = null;

        // load database in from file
        File db = null;
        boolean isNewDatabase = false;

        // check if db parent folder does not exist, make a new dir
        db = new File(System.getProperty("user.dir") + File.separator + "db");
        if (!db.exists()) {
            db.mkdir();
        }

        // if the db file it self does not exist, then make a new one
        db = new File(System.getProperty("user.dir") + File.separator + "db" + File.separator + "passwords.db");
        if (!db.exists()) {
            try {
                db.createNewFile();
                isNewDatabase = true;
            } catch (IOException e) {
                System.err.println("Could not create to passwords.db sqlite file");
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }

        sqlitePathToDatabase = "jdbc:sqlite:" + db.getAbsolutePath();

        try {
            connection = DriverManager.getConnection(sqlitePathToDatabase);

        } catch (SQLException e) {
            System.err.println("Could not make connection to database");
            System.err.println(e.getMessage());
            System.exit(-1); // exit could not connect to db
        }

        // if this is a fresh table, create the passwords table
        if (isNewDatabase) {
            createTable();
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
            System.err.println("Could not close connection");
        }
    }

    /**
     * Helper function to create new passwords table when database is created
     * for the first time
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS passwords (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "account TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "date_last_modified TEXT NOT NULL" +
                ");";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            // Execute the SQL statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Problem with creating passwords table in sqlite database");
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public ArrayList<AccountModel> getAllAccounts() {

        int id;
        String account;
        String username;
        String password;
        String dateLastModified;

        ArrayList<AccountModel> accounts = new ArrayList<>();
        String queryString = "SELECT * FROM passwords";

        try {
            connection = DriverManager.getConnection(sqlitePathToDatabase.toString());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);

            while (rs.next()) {

                id = rs.getInt("id");
                account = rs.getString("account");
                username = rs.getString("username");
                password = rs.getString("password");
                dateLastModified = rs.getString("date_last_modified");

                accounts.add(new AccountModel(id, account, username, password, Long.parseLong(dateLastModified)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    /*
     * This method will match the prefix of whats passed in
     * 
     */
    public ArrayList<AccountModel> getLikeAccounts(String accountParam) {

        int id;
        String account;
        String username;
        String password;
        String dateLastModified;
        ArrayList<AccountModel> accounts = new ArrayList<>();
        String queryString = "SELECT * FROM passwords WHERE account LIKE ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(queryString)) {
            pstmt.setString(1, '%' + accountParam + '%'); // % makes the ends wildcards

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id");
                account = rs.getString("account");
                username = rs.getString("username");
                password = rs.getString("password");
                dateLastModified = rs.getString("date_last_modified");

                accounts.add(new AccountModel(id, account, username, password, Long.parseLong(dateLastModified)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public void addAccount(String account, String userName, String password) {

        String queryString = "INSERT INTO passwords (account, username, password, date_last_modified) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(queryString);) {

            Long currentTimeSince1970ms = System.currentTimeMillis();

            pstmt.setString(1, account);
            pstmt.setString(2, userName);
            pstmt.setString(3, password);
            pstmt.setString(4, currentTimeSince1970ms.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAccount(String account, String userName, int passwordLen) {

        String generatedPassword = passwordUtil.generatePassword(passwordLen);
        String queryString = "INSERT INTO passwords (account, username, password, date_last_modified) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(queryString);) {

            Long currentTimeSince1970ms = System.currentTimeMillis();

            pstmt.setString(1, account);
            pstmt.setString(2, userName);
            pstmt.setString(3, generatedPassword);
            pstmt.setString(4, currentTimeSince1970ms.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String account) {

        // TODO: this will delete all instances of "account" (09/03/24) -> ???
        // should not be an issue since the accounts names should be unique, maybe
        // fix later

        String queryString = "DELETE FROM passwords WHERE account = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(queryString);
            pstmt.setString(1, account);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccount(AccountModel newAccount, int idToUpdate) {

        String queryString = "UPDATE passwords SET password = ?, "
                + "date_last_modified = ?"
                + "WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(queryString);) {

            Long currentTimeSince1970ms = System.currentTimeMillis();

            pstmt.setString(1, newAccount.getPassword());
            pstmt.setString(2, currentTimeSince1970ms.toString());
            pstmt.setInt(3, idToUpdate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}