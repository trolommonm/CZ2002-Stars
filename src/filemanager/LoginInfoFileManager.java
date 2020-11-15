package filemanager;

import model.AccountType;
import model.LoginInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginInfoFileManager implements ILoginInfoFileManager {
    private File studentLoginInfoFile;
    private File adminLoginInfoFile;

    public LoginInfoFileManager() {
        adminLoginInfoFile = new File("data/AdminLoginInfo.txt");
        studentLoginInfoFile = new File("data/StudentLoginInfo.txt");
    }

    @Override
    public void addLoginInfoForNewStudent(LoginInfo loginInfo) {
        try {
            FileWriter fw = new FileWriter(studentLoginInfoFile, true);
            fw.write(loginInfo.getUserId() + "|" + loginInfo.getPassword());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<LoginInfo> retrieveStudentLoginInfoList() {
        return retrieveLoginInfoList(studentLoginInfoFile, AccountType.STUDENT);
    }

    @Override
    public ArrayList<LoginInfo> retrieveAdminLoginInfoList() {
        return retrieveLoginInfoList(adminLoginInfoFile, AccountType.ADMIN);
    }

    private ArrayList<LoginInfo> retrieveLoginInfoList(File file, AccountType accountType) {
        ArrayList<LoginInfo> adminLoginInfoList = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNext()) {
                String[] lineSplit = sc.nextLine().split("\\|");
                adminLoginInfoList.add(new LoginInfo(accountType, lineSplit[0], lineSplit[1]));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return adminLoginInfoList;
    }

}
