package model;

public class LoginInfo {
    private AccountType accountType;
    private String userId;
    private String password;

    public LoginInfo(AccountType accountType, String userId, String password) {
        this.accountType = accountType;
        this.userId = userId;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LoginInfo) {
            LoginInfo loginInfoObj = (LoginInfo) obj;

            return getUserId().equals(loginInfoObj.getUserId()) &&
                    getPassword().equals(loginInfoObj.getPassword()) &&
                    getAccountType().equals(loginInfoObj.getAccountType());
        }

        return false;
    }
}
