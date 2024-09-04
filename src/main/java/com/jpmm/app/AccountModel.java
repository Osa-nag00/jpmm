package com.jpmm.app;

public class AccountModel {

    /**
     * Model to represent Account from database
     */
    private int id;
    private String Account;
    private String Username;
    private String Password;

    /**
     * Saved in milliseconds since 1970
     */
    private Long DateLastModified;

    AccountModel() {
        this.id = -1;
        this.Account = "";
        this.Username = "";
        this.Password = "";
        this.DateLastModified = 0L;
    }

    AccountModel(int id, String Account, String Username, String Password, Long DateLastModified) {
        this.id = id;
        this.Account = Account;
        this.Username = Username;
        this.Password = Password;
        this.DateLastModified = DateLastModified;

    }

    public int getId() {
        return id;
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
