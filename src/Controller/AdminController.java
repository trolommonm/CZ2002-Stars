package Controller;

import ErrorMessage.ErrorMessage;
import FileManager.StorageManager;
import Model.Course;
import View.AdminUi;

public class AdminController {
    private AdminUi adminUi;
    private StorageManager storageManager;

    public AdminController() {
        adminUi = new AdminUi();
        storageManager = new StorageManager();
    }

    public void getUserChoice() {
        int choice;
        do {
            adminUi.printMenu();
            choice = adminUi.getUserChoice();

            switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                addCourse();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                adminUi.printMessageWithDivider(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        } while (choice != 8);
    }

    public void addCourse() {
        Course course = adminUi.getCourseToAdd();
        storageManager.addCourse(course);

        adminUi.printCourses(storageManager.getAllCourses(), "Added " + course.toString() + "!");
    }

}
