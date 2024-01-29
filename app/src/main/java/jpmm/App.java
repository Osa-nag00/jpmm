/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package jpmm;

import java.util.Scanner;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    public static void test() {

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<AccountModel> returnedAccounts;
        AccountModel correctAccount;
        String userInput = "";
        DatabaseDao DD = new DatabaseDao();

        System.out.println("Enter the Name of the account: ");

        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            System.err.println("COULD NOT READ USER INPUT");
            e.printStackTrace();
        }

        if (userInput == "") {
            System.exit(-1);
        }

        returnedAccounts = DD.getLikeAccounts(userInput);
        correctAccount = getCorrectAccount(returnedAccounts);
        printAccount(correctAccount);

        // waits for user to press enter to exit program
        scanner.close();
    }

    public static AccountModel getCorrectAccount(ArrayList<AccountModel> accounts) {

        Scanner scanner = new Scanner(System.in);
        int size = accounts.size();
        int userInput = 1;
        System.out.println("Which Account:");

        String output;

        for (int i = 0; i < size; i++) {
            output = String.format("%d: %s", i + 1, accounts.get(i).getAccount());
            System.out.println(output);
        }

        // subtract 1 to take be in sync with the menu
        if (scanner.hasNextInt()) {
            userInput = scanner.nextInt();
            userInput--;
        }

        while (userInput < 0 || userInput > (size - 1)) {
            System.out.println("INVALID INPUT, TRY OPTION");
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
        output = String.format("Account: %s \nUsername: %s \nPassword: %s \n", accountModel.getAccount(),
                accountModel.getUsername(), accountModel.getPassword());
        System.out.print(output);

    }
}
