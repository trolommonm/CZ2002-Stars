package filemanager;

import model.LoginInfo;

import java.util.ArrayList;

public interface ILoginInfoFileManager {
    void addLoginInfoForNewStudent(LoginInfo loginInfo);

    ArrayList<LoginInfo> retrieveStudentLoginInfoList();

    ArrayList<LoginInfo> retrieveAdminLoginInfoList();
}
