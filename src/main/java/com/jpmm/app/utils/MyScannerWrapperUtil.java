package com.jpmm.app.utils;

import java.util.Scanner;

/**
 * Pretty sure I made this class because gradle was giving me issues
 * May not need this anymore but don't feel like refactoring (09/26/24)
 */
public class MyScannerWrapperUtil {

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
