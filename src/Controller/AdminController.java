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
            choice = adminUi.getMenuInputChoice();

            switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                addCourse();
                break;
            case 4:
                editCourse();
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
            storageManager.addIndexNumber(adminUi.getIndexNumber(true), courseCode);
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
        default:
            adminUi.print(ErrorMessage.ERROR_INPUT_CHOICE);
        }
    }

}
