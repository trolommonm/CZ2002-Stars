import controller.AdminController;
import controller.LoginController;
import controller.StudentController;
import model.AccountType;

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
            StudentController sc = new StudentController(lc.getUserId());
            sc.run();
            break;
        default:
            assert false : "Invalid accountType returned from LoginController!";
        }

    }

}
