package Controller;

import ErrorMessage.ErrorMessage;
import FileManager.LoginInfoFileManager;
import FileManager.StorageManager;
import Model.Course;
import Model.IndexNumber;
import Model.LoginInfo;
import Model.Student;
import View.StudentUi;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StudentController {

    private StudentUi studentUi;
    private StorageManager storageManager;

    public StudentController() {
        studentUi = new StudentUi();
        storageManager = new StorageManager();
    }

    public void run() {
        int choice;
        do {
            choice = studentUi.getMenuInputChoice();
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    changeIndex();
                    break;
                case 6:
                    break;
                case 7:
                    break;
                default:
                    studentUi.printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        } while (choice != 7);
    }

    private void addCourse() {

    }

    private void dropCourse() {

    }

    private void printRegisteredCourse() {

    }

    private void printVacancies() {

    }

    private void changeIndex() {
        String courseCode = studentUi.getCourseCode("Enter course code: ");
        int currentIndex = studentUi.getIndex("Enter current index: ");
        int swappingIndex = studentUi.getIndex("Enter index to change to: ");
        Course course = storageManager.getCourse(courseCode);
        ArrayList<IndexNumber> courseIndexNumbers = course.getIndexNumbers();
        int swappingIndexVacancy;
        for (IndexNumber searchCurrent : courseIndexNumbers) {
            if (searchCurrent.getIndexNumber() == currentIndex) {
                for (IndexNumber searchSwap : courseIndexNumbers) {
                    if (searchSwap.getIndexNumber() == swappingIndex) {
                        swappingIndexVacancy = searchSwap.getAvailableVacancy();
                        if (swappingIndexVacancy > 0) {
                            // drop current index
                            // add swapping index
                            System.out.println("Successful: Dropped index " + currentIndex + " for " + swappingIndex);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void swapIndex() {
        String courseCode = studentUi.getCourseCode("Enter course code: ");
        int currentIndex = studentUi.getIndex("Enter current index: ");
        int swappingIndex = studentUi.getIndex("Enter index to change to: ");
        String username = studentUi.getLoginInfo("Enter username of student swapping with you: ");
        String password = studentUi.getLoginInfo("Enter username of student swapping with you: ");
        boolean verified = false;
        LoginInfo toSwapInfo = new LoginInfo(username, password);
        LoginInfoFileManager verify = new LoginInfoFileManager();
        try {
            ArrayList<LoginInfo> loginInfoList = verify.retrieveStudentLoginInfoList();
            for (LoginInfo loginInfo: loginInfoList) {
                if (loginInfo.equals(toSwapInfo)) {
                    verified = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (verified) {
            Course course = storageManager.getCourse(courseCode);
            ArrayList<IndexNumber> courseIndexNumbers = course.getIndexNumbers();
            for (IndexNumber searchSwap : courseIndexNumbers) {
                if (searchSwap.getIndexNumber() == swappingIndex) {
                    ArrayList<Student> studentList = storageManager.getStudentsInCourse(course);
                    for (Student s : studentList) {
                        if (s.getUserId().equals(username)) {
                            // Add & drop index x2
                            System.out.println("Successful: Dropped index " + currentIndex + " for " + swappingIndex);
                        }
                    }
                }
            }
        }
        else {
            System.out.println("Error");
        }
    }

}
