import Controller.AdminController;
import Controller.LoginController;
import Controller.StudentController;
import Enum.AccountType;

public class Main {

    public static void main(String[] args) {
        LoginController lc = new LoginController();
        AccountType accountType = lc.run();
        switch (accountType) {
        case ADMIN:
            AdminController ac = new AdminController();
            ac.run();
            break;
        case STUDENT:
            StudentController sc = new StudentController();
            sc.run();
            break;
        default:
            throw new RuntimeException("Invalid accountType returned from LoginController!");
        }

    }

}
