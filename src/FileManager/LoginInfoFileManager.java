package FileManager;

import Model.LoginInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginInfoFileManager {
    private File studentLoginInfoFile;
    private File adminLoginInfoFile;

    public LoginInfoFileManager() {
        adminLoginInfoFile = new File("data/AdminLoginInfo.txt");
        studentLoginInfoFile = new File("data/StudentLoginInfo.txt");
    }

    public ArrayList<LoginInfo> retrieveStudentLoginInfoList() throws FileNotFoundException {
        return retrieveLoginInfoList(studentLoginInfoFile);
    }

    public ArrayList<LoginInfo> retrieveAdminLoginInfoList() throws FileNotFoundException {
        return retrieveLoginInfoList(adminLoginInfoFile);
    }

    private ArrayList<LoginInfo> retrieveLoginInfoList(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<LoginInfo> adminLoginInfoList = new ArrayList<>();

        while (sc.hasNext()) {
            String[] lineSplit = sc.nextLine().split("\\|");
            adminLoginInfoList.add(new LoginInfo(lineSplit[0], lineSplit[1]));
        }

        return adminLoginInfoList;
    }

}
