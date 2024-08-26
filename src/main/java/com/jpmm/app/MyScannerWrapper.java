package com.jpmm.app;

import java.util.Scanner;

public class MyScannerWrapper {

    private static Scanner scanner = new Scanner(System.in);
    private static String userInputString = "";
    // private static int userInputInt = -1;

    public static String getStringInput() {

        if (scanner.hasNextLine()) {
            userInputString = scanner.nextLine();

        }
        return userInputString;
    }

    public static int getPositiveIntInput() {
        int retval = 0;

        do {
            if (scanner.hasNextInt()) {
                retval = scanner.nextInt();
            }

        } while ((retval % 1 != 0) && (retval < 0)); // makes sure the integer, is an actual integer

        return retval;
    }

    public static void close() {
        scanner.close();
    }

}
