import controller.AdminController;
import controller.LoginController;
import controller.StudentController;
import filemanager.ILoginInfoFileManager;
import filemanager.ILoginable;
import filemanager.IStorageManager;
import filemanager.LoginInfoFileManager;
import filemanager.LoginManager;
import filemanager.StorageManager;
import model.LoginInfo;

public class Main {

    public static void main(String[] args) {
        IStorageManager storageManager = new StorageManager();
        ILoginInfoFileManager loginInfoFileManager = new LoginInfoFileManager();
        ILoginable loginManager = new LoginManager(loginInfoFileManager, storageManager);

        LoginController lc = new LoginController(storageManager, loginManager);
        LoginInfo providedLoginInfo = lc.run();
        switch (providedLoginInfo.getAccountType()) {
        case ADMIN:
            AdminController ac = new AdminController(storageManager, loginInfoFileManager);
            ac.run();
            break;
        case STUDENT:
            StudentController sc = new StudentController(providedLoginInfo.getUserId(), storageManager, loginManager);
            sc.run();
            break;
        default:
            assert false : "Invalid accountType returned from LoginController!";
        }
    }

}
