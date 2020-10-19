import Controller.AdminController;
import Controller.LoginController;
import Controller.StudentController;

public class Main {

    public static void main(String[] args) {
        LoginController lc = new LoginController();
        lc.login();

        AdminController ac = new AdminController();
        ac.getUserChoice();

        StudentController sc = new StudentController();
        sc.getUserChoice();
    }

}
