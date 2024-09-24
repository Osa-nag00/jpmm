package com.jpmm.app.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.jpmm.app.AccountModel;
import com.jpmm.app.DatabaseDao;

import java.util.HashMap;

public class csvUtils {

    public static void importCSV(DatabaseDao Dao) {

        File passwordCsv = findPasswordCsv();

        ArrayList<AccountModel> accountsFromFile = parsePasswordCsv(passwordCsv);

        ArrayList<AccountModel> accountsFromDatabase = Dao.getAllAccounts();

        importPasswordsToDb(Dao, accountsFromDatabase, accountsFromFile);
    }

    // TODO: slight issue (work on this at some point, for now both accounts are in)
    /**
     * for accounts like {"google", "google.com"} don't update properly because of
     * the slight difference in names
     */

    private static void importPasswordsToDb(DatabaseDao Dao, ArrayList<AccountModel> accountsFromDatabase,
            ArrayList<AccountModel> accountsFromFile) {

        HashMap<Integer, AccountModel> accountsFromDbMap = new HashMap<>();
        HashMap<String, Integer> accountNamesFromDbMap = new HashMap<>();

        /**
         * QUICK NOTE: normalize both account names to lowercase to make sure match
         * is solely on the spelling
         */

        // put accounts from db into map for quick lookup
        for (AccountModel acc : accountsFromDatabase) {
            // this stores names to search if there are dupes
            accountNamesFromDbMap.put(acc.getAccount().toLowerCase(), acc.getId());
            // this stores the actual accounts to receive later if a dupe is found
            accountsFromDbMap.put(acc.getId(), acc);
        }

        // loop through all password from csv
        for (AccountModel acc : accountsFromFile) {

            String tempAccountFromFileName = acc.getAccount().toLowerCase();

            // if there is a duplicate account name
            if (accountNamesFromDbMap.containsKey(tempAccountFromFileName)) {
                int id = accountNamesFromDbMap.get(tempAccountFromFileName);
                // if account from the import is newer than the on stored in the database,
                // replace in the database
                if (accountsFromDbMap.get(id).getDateLastModified() <= acc.getDateLastModified()) {
                    Dao.updateAccount(acc, id);
                }
            } else {
                // if not already in the the db just add it without needing to update the
                // password
                Dao.addAccount(acc.getAccount(), acc.getUsername(), acc.getPassword());
            }
        }

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

                // the accounts from the excel sheet won't have proper ids yet
                // just set it to -1 for now
                retval.add(new AccountModel(-1, name, username, password, currentTimeSince1970ms));
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
