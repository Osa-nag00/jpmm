package jpmm;

import java.util.Scanner;

public class MyScannerWrapper {

    private static Scanner scanner = new Scanner(System.in);
    private static String userInputString = "";
    private static int userInputInt = -1;

    public static String getStringInput() {

        if (scanner.hasNextLine()) {
            userInputString = scanner.nextLine();

        }

        return userInputString;
    }

    public static int getIntInput() {

        boolean firstTime = true;

        // get user empty while its blank
        do {

            if (!firstTime) {
                System.out.println("EMPTY INPUT, TRY AGAIN");
            }

            // if scanner has a next line get the input
            if (scanner.hasNextInt()) {
                userInputInt = scanner.nextInt();
                firstTime = false;
            }
            // check while it is still the default
        } while (userInputInt == -1);

        return userInputInt;
    }

    public static void close() {
        scanner.close();
    }

}
