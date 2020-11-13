package filemanager;

import model.LoginInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginInfoFileManager {
    private File studentLoginInfoFile;
    private File adminLoginInfoFile;

    public LoginInfoFileManager() {
        adminLoginInfoFile = new File("data/AdminLoginInfo.txt");
        studentLoginInfoFile = new File("data/StudentLoginInfo.txt");
    }

    public void addLoginInfoForNewStudent(LoginInfo loginInfo) throws IOException {
        FileWriter fw = new FileWriter(studentLoginInfoFile,true);
        fw.write(loginInfo.getUserId() + "|" + loginInfo.getPassword());
        fw.close();
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
