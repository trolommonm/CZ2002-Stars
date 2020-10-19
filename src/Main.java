import Controller.AdminController;
import Controller.LoginController;

public class Main {

    public static void main(String[] args) {
        LoginController lc = new LoginController();
        lc.run();

        AdminController ac = new AdminController();
        ac.run();
    }

}
