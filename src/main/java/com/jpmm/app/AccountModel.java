package com.jpmm.app;

public class AccountModel {

    /**
     * Model to represent Account from database
     */
    private String Account;
    private String Username;
    private String Password;

    /**
     * Saved in milliseconds since 1970
     */
    private Long DateLastModified;

    AccountModel() {
        this.Account = "";
        this.Username = "";
        this.Password = "";
        this.DateLastModified = 0L;
    }

    AccountModel(String Account, String Username, String Password, Long DateLastModified) {
        this.Account = Account;
        this.Username = Username;
        this.Password = Password;
        this.DateLastModified = DateLastModified;

    }

    public String getAccount() {
        return Account;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public Long getDateLastModified() {
        return DateLastModified;
    }

    @Override
    public String toString() {
        return this.Account + " : " + this.Username + " : " + this.Password + "\n";
    }
}
