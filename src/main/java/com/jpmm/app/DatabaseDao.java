package com.jpmm.app;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseDao {

    private Connection connection;
    private String sqlLiteDatabasePath;

    DatabaseDao() {

        connection = null;
        File tempDbFile = null;
        // Copy the database file to a temporary location
        try {
            tempDbFile = File.createTempFile("passwords", ".db");
            tempDbFile.deleteOnExit(); // Ensure it's deleted when the program exits

        } catch (IOException e) {
            System.err.println("Could not create temp sqlite database");
        }

        // load in database from resource folder
        try (InputStream dbStream = getClass().getClassLoader().getResourceAsStream("db/passwords.db")) {
            FileOutputStream outputStream = new FileOutputStream(tempDbFile);

            // if not found
            if (dbStream == null) {
                outputStream.close();
                throw new IOException("Database file not found");
            }

            // Copy the InputStream to the temporary file
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = dbStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        // Need to add this prefix for the connection to occur
        // when dealing with sqlite database
        sqlLiteDatabasePath = "jdbc:sqlite:" + tempDbFile.getAbsolutePath();

        try {
            connection = DriverManager.getConnection(sqlLiteDatabasePath);
        } catch (SQLException e) {
            System.err.println("COULD NOT MAKE CONNECTION TO SQLITE DATABASE");
            e.printStackTrace();
            System.exit(-1); // exit could not connect to db
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

        int id;
        String account;
        String username;
        String password;
        String dateLastModified;

        ArrayList<AccountModel> accounts = new ArrayList<>();
        String queryString = "SELECT * FROM passwords";

        try {
            connection = DriverManager.getConnection(sqlLiteDatabasePath.toString());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);

            while (rs.next()) {

                id = rs.getInt("id");
                account = rs.getString(1);
                username = rs.getString(2);
                password = rs.getString(3);
                dateLastModified = rs.getString(4);

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
                account = rs.getString(1);
                username = rs.getString(2);
                password = rs.getString(3);
                dateLastModified = rs.getString(4);

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

        String queryString = "UPDATE passwords SET Password = ?, "
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