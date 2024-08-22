package jpmm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class csvUtils {

    // TODO: might not actually use this
    public static void exportCSV(DatabaseDao Dao) {
        ArrayList<AccountModel> accounts = Dao.getAllAccounts();

        for (AccountModel account : accounts) {
            System.out.println(account);
        }
    }

    public static void importCSV(DatabaseDao Dao) {

        File passwordCsv = findPasswordCsv();

        // TODO: read in all the the accounts from the csv
        // TODO: get rid of all the url and notes col
        // TODO: parse into list of accounts
        ArrayList<AccountModel> accountsFromFile = parsePasswordCsv(passwordCsv);

        // TODO: add to db without dupes
        ArrayList<AccountModel> accountsFromDatabase = Dao.getAllAccounts();

    }

    private static void importPasswordsToDb(DatabaseDao Dao, ArrayList<AccountModel> accountsFromDatabase,
            ArrayList<AccountModel> accountsFromFile) {

        int idx = 0;
        HashMap<Integer, AccountModel> accountsFromDbMap = new HashMap<>();

        // put accounts from db into map for quick lookup
        for (AccountModel acc : accountsFromDatabase) {
            accountsFromDbMap.put(idx++, acc);
        }

        // TODO: work on later
        // need to compare times and put the most up to date accounts into the database

    }

    private static File findPasswordCsv() {

        // this is inside the app folder
        String fileName = "Microsoft Edge Passwords.csv";
        String csvToImport = System.getProperty("user.dir") + File.separator + fileName;
        File f = new File(csvToImport);

        return new File(f.getAbsolutePath());
    }

    private static ArrayList<AccountModel> parsePasswordCsv(File passwordCsv) {

        ArrayList<AccountModel> retval = new ArrayList<>();
        boolean isFirstLine = true;
        final String firstLineOfFile = "name,url,username,password,note";

        try (Scanner fileReader = new Scanner(passwordCsv)) {

            Long currentTimeSince1970ms = 0L;

            // read each line
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                line = line.replace("\n", ""); // remove new lines for comparison

                // make sure the header of the file is correct
                if (isFirstLine && !line.equals(firstLineOfFile)) {
                    // this file is invalid, not the correct header
                    throw new IOException();
                } else if (isFirstLine && line.equals(firstLineOfFile)) {
                    isFirstLine = false;
                    continue;
                }

                // break line into parts
                String[] splitLine = line.split(",");

                String name = splitLine[0];
                String username = splitLine[2];
                String password = splitLine[3];
                currentTimeSince1970ms = System.currentTimeMillis();

                retval.add(new AccountModel(name, username, password, currentTimeSince1970ms));
            }

        } catch (IOException e) {
            String errMsg = String.format("Could not parse file with path %s",
                    passwordCsv.toPath().toString());
            System.err.println(errMsg);
            e.printStackTrace();
            return retval; // return unfinished list
        }

        return retval;
    }

}
