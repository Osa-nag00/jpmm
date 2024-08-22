package jpmm;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.io.File;

public class DatabaseDao {

    private Connection connection;
    private String sqlLiteDatabasePath;

    DatabaseDao() {

        connection = null;

        // gets the absolute path of the sqlite *.db file
        // sqlLiteDatabasePath =
        // sqlLiteDatabasePath =
        // App.class.getClassLoader().getResource("db/passwords.db").toString();

        // This makes it so the db folder needs to be at the root of the dir
        // the context of this is within the /app dir
        // sqlLiteDatabasePath = "src/main/java/jpmm/passwords.db";
        sqlLiteDatabasePath = System.getProperty("user.dir") + File.separator + "passwords.db";

        // Use this to double check and make sure the file exist
        File tempFileObject = new File(sqlLiteDatabasePath);

        if (!tempFileObject.exists()) {
            System.err.println("COULD NOT FIND DATABASE FILE");
            // TODO: create new database here??
        }

        // Need to add this prefix for the connection to occur
        // when dealing with sqlite database
        sqlLiteDatabasePath = "jdbc:sqlite:" + sqlLiteDatabasePath;

        try {
            connection = DriverManager.getConnection(sqlLiteDatabasePath.toString());
        } catch (SQLException e) {

            System.err.println("COULD NOT MAKE CONNECTION TO SQLITE DATABASE");
            e.printStackTrace();
        }

        // MyUtil.clearConsole();
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
        String dateLastModified;

        ArrayList<AccountModel> accounts = new ArrayList<>();
        String queryString = "SELECT * FROM passwords";

        try {
            connection = DriverManager.getConnection(sqlLiteDatabasePath.toString());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                account = rs.getString(1);
                username = rs.getString(2);
                password = rs.getString(3);
                dateLastModified = rs.getString(4);

                accounts.add(new AccountModel(account, username, password, Long.parseLong(dateLastModified)));
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

        String account;
        String username;
        String password;
        String dateLastModified;
        ArrayList<AccountModel> accounts = new ArrayList<>();
        String queryString = String.format("SELECT * FROM passwords WHERE Account LIKE ?");

        try {
            PreparedStatement pstmt = connection.prepareStatement(queryString);
            pstmt.setString(1, accountParam);

            ResultSet rs = pstmt.executeQuery(queryString);

            while (rs.next()) {
                account = rs.getString(1);
                username = rs.getString(2);
                password = rs.getString(3);
                dateLastModified = rs.getString(4);

                accounts.add(new AccountModel(account, username, password, Long.parseLong(dateLastModified)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public void addAccount(String account, String userName, int passwordLen) {

        String generatedPassword = passwordUtil.generatePassword(passwordLen);
        String queryString = "INSERT INTO passwords (account, username, password) VALUES (?, ?, ?, ?)";

        try {

            Long currentTimeSince1970ms = System.currentTimeMillis();

            PreparedStatement pstmt = connection.prepareStatement(queryString);
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

        // TODO: this will delete all instances of "account"
        // should not be an issue since the accounts names should be unique, maybe
        // fix later

        String queryString = "DELETE FROM passwords WHERE Account = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(queryString);
            pstmt.setString(1, account);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}