package jpmm;

import java.io.File;
import java.util.ArrayList;

public class csvUtils {

    public static void exportCSV(DatabaseDao Dao) {
        ArrayList<AccountModel> accounts = Dao.getAllAccounts();

        for (AccountModel account : accounts) {
            System.out.println(account);
        }

    }

    public static void importCSV(DatabaseDao Dao) {

        String csvToImport = System.getProperty("user.dir") + File.separator + "import";

        // TODO: look for csv in import directory

        // TODO: read in all the the accounts from the csv

    }

}
