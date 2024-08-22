package jpmm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

                retval.add(new AccountModel(name, username, password));
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
