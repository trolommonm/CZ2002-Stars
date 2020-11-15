package controller;

import errormessage.ErrorMessage;
import filemanager.ILoginInfoFileManager;
import filemanager.IStorageManager;
import filemanager.LoginInfoFileManager;
import model.Course;
import model.LoginInfo;
import model.Student;
import view.AdminUi;

import java.util.ArrayList;

public class AdminController {
    private AdminUi adminUi;
    private IStorageManager storageManager;
    private ILoginInfoFileManager loginInfoFileManager;

    public AdminController(IStorageManager storageManager, ILoginInfoFileManager loginInfoFileManager) {
        adminUi = new AdminUi();
        this.storageManager = storageManager;
        this.loginInfoFileManager = loginInfoFileManager;
    }

    public void run() {
        int choice;
        do {
            choice = adminUi.getMenuInputChoice();

            switch (choice) {
            case 1:
                editStudentAccessPeriod();
                break;
            case 2:
                addStudent();
                break;
            case 3:
                addCourse();
                break;
            case 4:
                editCourse();
                break;
            case 5:
                checkAvailableSlot();
                break;
            case 6:
                printStudentsByIndexNumber();
                break;
            case 7:
                printStudentsByCourses();
                break;
            case 8:
                adminUi.printGoodBye();
                break;
            default:
                adminUi.printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        } while (choice != 8);
    }

    private void addCourse() {
        Course course = adminUi.getCourseToAdd();
        storageManager.addCourse(course);
        adminUi.printCourses(storageManager.getAllCourses(), "Added " + course.toString() + "!");
    }

    private void editCourse() {
        String courseCode = adminUi.getCourseToEdit(storageManager.getAllCourses());
        Course course = storageManager.getCourse(courseCode);
        int choice = adminUi.getEditCourseChoice(course);

        switch (choice) {
        case 1:
            storageManager.setNewCourseCode(adminUi.getNewCourseCode(course), course.getCourseCode());
            break;
        case 2:
            storageManager.setNewCourseName(adminUi.getNewCourseName(course), course.getCourseCode());
            break;
        case 3:
            storageManager.setNewSchool(adminUi.getNewSchool(course), course.getCourseCode());
            break;
        case 4:
            storageManager.addIndexNumber(adminUi.getIndexNumber(true, course), course.getCourseCode());
            break;
        case 5:
            int index = adminUi.getIndexOfIndexNumberToEdit(course);
            int newMaxVacancy = adminUi.getNewMaxVacancy(course, index);
            try {
                storageManager.setNewMaxVacancy(course.getCourseCode(), index, newMaxVacancy);
            } catch (Exception e) {
                adminUi.printMessageWithDivider(e.getMessage());
            }
            break;
        }
    }

    private void checkAvailableSlot() {
        adminUi.checkVacancyOfIndexNumber(storageManager.getAllCourses());
    }

    private void editStudentAccessPeriod() {
        ArrayList<Student> students = storageManager.getAllStudents();
        int index = adminUi.getStudentChoiceEditAccessTime(students);
        Student student = students.get(index);
        storageManager.setNewAccessTime(student.getUserId(),
                adminUi.getNewAccessTime(student));
        adminUi.printMessageWithDivider("Updated access period for " + student.getName() + "!");
    }

    private void addStudent() {
        LoginInfo loginInfoForStudent;
        while (true) {
            loginInfoForStudent = adminUi.getLoginInfo();
            if (checkUserIdExists(loginInfoForStudent.getUserId())) {
                adminUi.printErrorMessage(ErrorMessage.USER_ID_EXISTS);
                continue;
            }
            break;
        }
        Student student = adminUi.getNewStudent(loginInfoForStudent.getUserId());
        storageManager.addStudent(student);
        loginInfoFileManager.addLoginInfoForNewStudent(loginInfoForStudent);
        adminUi.printStudents(storageManager.getAllStudents(), "Added " + student.getName() + "!");
    }

    private boolean checkUserIdExists(String userId) {
        LoginInfoFileManager loginInfoFileManager = new LoginInfoFileManager();
        for (LoginInfo loginInfo: loginInfoFileManager.retrieveStudentLoginInfoList()) {
            if (loginInfo.getUserId().equals(userId)) {
                return true;
            }
        }

        return false;
    }

    private void printStudentsByIndexNumber() {
        adminUi.printStudentsByIndexNumber(storageManager.getAllCourses());
    }

    private void printStudentsByCourses() {
        adminUi.printStudentsByCourse(storageManager.getAllCourses());
    }
}
