package jpmm;

public class AccountModel {

    /**
     * Model to represent Account from database
     */
    private String Account;
    private String Username;
    private String Password;

    AccountModel() {
        this.Account = "";
        this.Username = "";
        this.Password = "";
    }

    AccountModel(String Account, String Username, String Password) {
        this.Account = Account;
        this.Username = Username;
        this.Password = Password;
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
}
