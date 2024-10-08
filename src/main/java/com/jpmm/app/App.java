/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.jpmm.app;

import java.util.Scanner;

import com.jpmm.app.utils.MyScannerWrapperUtil;
import com.jpmm.app.utils.MyUtil;
import com.jpmm.app.utils.csvUtils;

import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

// TODO: work out the oddities when it comes to importing and exporting data from the database

public class App {

    static DatabaseDao Dao;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<AccountModel> returnedAccounts;
        AccountModel correctAccount;
        String userInput = "";
        Dao = new DatabaseDao();
        boolean pasteHasBeenDone = false;

        System.out.println("Enter the name of The account Or enter a command: ");

        if (scanner.hasNextLine()) {
            userInput = scanner.nextLine();
        }

        if (userInput == "") {
            System.exit(-1);
        }

        // handles commands
        if (userInput.startsWith(".")) {
            handleCommands(userInput, Dao);
        } else {
            returnedAccounts = Dao.getLikeAccounts(userInput);

            if (returnedAccounts.size() <= 0) {
                System.out.println("No accounts found!");
            } else {
                correctAccount = getCorrectAccount(returnedAccounts);
                copyAccountPasswordToClipboard(correctAccount);
                pasteHasBeenDone = true;
                printAccount(correctAccount);
            }
        }

        // clear the system clipboard if the password has been pasted to it
        if (pasteHasBeenDone) {

            // Nothing else works, just make it wait like 2 seconds before closing :(
            try {
                Thread.sleep(3500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            clearClipboard();
            System.out.println("Password has been cleared from clipboard");
        }

        MyScannerWrapperUtil.close();
        scanner.close();
        Dao.closeConnection();
    }

    /**
     * Sets clipboard to the empty string
     */
    private static void clearClipboard() {
        // set empty string
        StringSelection data = new StringSelection("");

        // get the system clipboard
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        // copy the password to the clipboard
        cb.setContents(data, data);
    }

    /**
     * Sets clipboard to the passed in accountModel password
     */
    private static void copyAccountPasswordToClipboard(AccountModel accountModel) {

        // make the password a transferable item
        StringSelection data = new StringSelection(accountModel.getPassword());

        // get the system clipboard
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        // copy the password to the clipboard
        cb.setContents(data, data);
    }

    public static AccountModel getCorrectAccount(ArrayList<AccountModel> accounts) {

        Scanner scanner = new Scanner(System.in);
        int size = accounts.size();
        int userInput = 1;
        System.out.println("Which Account:");

        String output;

        for (int i = 0; i < size; i++) {
            output = String.format("%d: %s (%s)", i + 1, accounts.get(i).getAccount(),
                    accounts.get(i).getUsername());
            System.out.println(output);
        }

        // subtract 1 to take be in sync with the menu
        if (scanner.hasNextInt()) {
            userInput = scanner.nextInt();
            userInput--;
        }

        while (userInput < 0 || userInput > (size - 1)) {
            System.out.println("Invalid input, try again");
            if (scanner.hasNextInt()) {
                userInput = scanner.nextInt();
                userInput--;
            }
        }

        MyUtil.clearConsole();
        scanner.close();
        return accounts.get(userInput);
    }

    public static void printAccount(AccountModel accountModel) {

        String output;
        // get string of '*' with the length of the actual password
        String passwordPrintOut = obscurePassword(accountModel.getPassword().length());
        output = String.format("Account: %s \nUsername: %s \nPassword: %s \n", accountModel.getAccount(),
                accountModel.getUsername(), passwordPrintOut);
        System.out.print(output);

    }

    private static String obscurePassword(int passLen) {
        String retval = "";
        for (int i = 0; i < passLen; i++) {
            retval += "*";
        }
        return retval;
    }

    public static void handleCommands(String userInput, DatabaseDao Dao) {

        // remove the '.' from the input to check the input
        // makes upper case to look nice in code IDK
        userInput = userInput.substring(1).toUpperCase();

        switch (userInput) {
            case "A":
                addAccount();
                break;
            case "D":
                deleteAccount();
            case "I":
                csvUtils.importCSV(Dao);
                break;
            case "E":
                csvUtils.exportCSV(Dao);
                break;
            default:
                break;
        }
    }

    public static void addAccount() {

        String accountName = "";
        String userName = "";
        int passLen = -1;

        System.out.println("Enter the name of the account you want to add: ");
        accountName = MyScannerWrapperUtil.getStringInput();

        System.out.println("Enter the username/email for the account: ");
        userName = MyScannerWrapperUtil.getStringInput();

        System.out.println("Enter the length of password to be generated: ");
        passLen = MyScannerWrapperUtil.getPositiveIntInput();

        Dao.addAccount(accountName, userName, passLen);
    }

    public static void deleteAccount() {
        ArrayList<AccountModel> returnedAccounts;
        AccountModel correctAccount;
        String accountName = "";
        String correctAccountNameToRemove = "";
        System.out.println("Enter the name of the account you want to delete: ");
        accountName = MyScannerWrapperUtil.getStringInput();

        returnedAccounts = Dao.getLikeAccounts(accountName);

        if (returnedAccounts.size() < 0) {

            System.err.println("Can not find account in database");
            System.exit(-1);
        }

        correctAccount = getCorrectAccount(returnedAccounts);

        correctAccountNameToRemove = correctAccount.getAccount();

        Dao.deleteAccount(correctAccountNameToRemove);
    }
}
