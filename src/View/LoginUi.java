package View;

import ErrorMessage.ErrorMessage;
import Model.LoginInfo;

import Enum.AccountType;

import java.util.Scanner;

public class LoginUi extends Ui {
    private final String STARS_LOGO = "  _________________________ __________  _________\n" +
            " /   _____/\\__    ___/  _  \\\\______   \\/   _____/\n" +
            " \\_____  \\   |    | /  /_\\  \\|       _/\\_____  \\ \n" +
            " /        \\  |    |/    |    \\    |   \\/        \\\n" +
            "/_______  /  |____|\\____|__  /____|_  /_______  /\n" +
            "        \\/                 \\/       \\/        \\/ \n";
    private final String WELCOME_MESSAGE = "Welcome to NTU Stars Planner. Please login.";

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
        Scanner sc = new Scanner(System.in);
        int choice;
        AccountType accountType;

        while (true) {
            print("Enter your choice:");
            choice = sc.nextInt();

            if (choice == 1) {
                accountType = AccountType.STUDENT;
                break;
            } else if (choice == 2) {
                accountType = AccountType.ADMIN;
                break;
            }

            printMessageWithDivider(ErrorMessage.ERROR_INPUT_CHOICE);
        }

        return accountType;
    }

    public LoginInfo getLoginInfo() {
        Scanner sc = new Scanner(System.in);
        String userId;
        String password;

        print("Enter user id:");
        userId = sc.next();

        print("Enter password:");
        password = sc.next();

        return new LoginInfo(userId, password);
    }

}
