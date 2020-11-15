package view;

import errormessage.ErrorMessage;

import model.AccountType;

public class LoginUi extends Ui {
    private final String STARS_LOGO = "  _________________________ __________  _________\n" +
            " /   _____/\\__    ___/  _  \\\\______   \\/   _____/\n" +
            " \\_____  \\   |    | /  /_\\  \\|       _/\\_____  \\ \n" +
            " /        \\  |    |/    |    \\    |   \\/        \\\n" +
            "/_______  /  |____|\\____|__  /____|_  /_______  /\n" +
            "        \\/                 \\/       \\/        \\/ \n";
    private final String WELCOME_MESSAGE = "Welcome to NTU Stars Planner. Please login.";
    private final String[] loginOptions = {
            "1. Student Login",
            "2. Admin Login"
    };

    public void printWelcomeMessage() {
        print(STARS_LOGO);
        print(WELCOME_MESSAGE);
    }

    public void printLoginMessage() {
        printMessageWithDivider(
                "1. Student Login",
                "2. Admin Login"
        );
    }

    public AccountType getAccountType() {
        int choice;
        AccountType accountType;

        printWelcomeMessage();
        while (true) {
            choice = getInputChoice("Enter your choice:", loginOptions);
            if (choice == 1) {
                accountType = AccountType.STUDENT;
                break;
            } else if (choice == 2) {
                accountType = AccountType.ADMIN;
                break;
            } else {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        }

        return accountType;
    }

}
