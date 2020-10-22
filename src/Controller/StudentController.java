package Controller;

import ErrorMessage.ErrorMessage;
import FileManager.LoginInfoFileManager;
import FileManager.StorageManager;
import Model.Course;
import Model.IndexNumber;
import Model.LoginInfo;
import Model.Student;
import View.StudentUi;
import Exception.CourseRegisteredException;
import Exception.ClashingIndexNumberException;
import Exception.NoVacancyException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StudentController {
    private Student student;
    private StudentUi studentUi;
    private StorageManager storageManager;

    public StudentController(String userId) {
        studentUi = new StudentUi();
        storageManager = new StorageManager();
        student = storageManager.getStudent(userId);
    }

    public void run() {
        studentUi.printWelcomeMessage(student.getName());
        int choice;
        do {
            choice = studentUi.getMenuInputChoice();
            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    dropCourse();
                    break;
                case 3:
                    printRegisteredCourses();
                    break;
                case 4:
                    printVacancies();
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
        ArrayList<Course> courses = storageManager.getAllCourses();
        int index;
        index = studentUi.getIndexOfCourseToRegister(courses);
        Course courseToBeAdded = courses.get(index);
        index = studentUi.getIndexOfIndexNumberToRegister(courseToBeAdded.getIndexNumbers());
        IndexNumber indexNumberToBeAdded = courseToBeAdded.getIndexNumbers().get(index);
        try {
            storageManager.registerForCourse(student.getUserId(), courseToBeAdded.getCourseCode(),
                    indexNumberToBeAdded);
        } catch (CourseRegisteredException | ClashingIndexNumberException | NoVacancyException e) {
            studentUi.printErrorMessage(e.getMessage());
        }
    }

    private void dropCourse() {
        ArrayList<Course> courses = storageManager.getCoursesTakenByStudent(student);
        int index = studentUi.getIndexOfCourseToDrop(courses);
        Course course = courses.get(index);
        IndexNumber indexNumber = student.getRegisteredIndexNumbers().get(course.getCourseCode());
        storageManager.dropCourse(student.getUserId(), course.getCourseCode(), indexNumber);
    }

    private void printRegisteredCourses() {
        String registeredCourses = "\n";
        int index = 1;
        for (String courseCode: student.getCourseCodes()) {
            Course course = storageManager.getCourse(courseCode);
            registeredCourses += (index) + ". " + course.toString();
            registeredCourses += "\n\t" + student.getRegisteredIndexNumbers().get(courseCode).getFullDescription();
            if (index != student.getCourseCodes().size()) {
                registeredCourses += "\n";
            }
            index++;
        }
        studentUi.printMessageWithDivider("Here are your registered courses:", registeredCourses);
    }

    private void printVacancies() {
        studentUi.checkVacancyOfIndexNumber(storageManager.getAllCourses());
    }

    private void changeIndex() {
        String courseCode = studentUi.getCourseCode("Enter course code: ");
        int currentIndex = studentUi.getIndex("Enter current index: ");
        int swappingIndex = studentUi.getIndex("Enter index to change to: ");
        Course course = storageManager.getCourse(courseCode);
        ArrayList<IndexNumber> courseIndexNumbers = course.getIndexNumbers();
        int swappingIndexVacancy;
        for (IndexNumber searchCurrent : courseIndexNumbers) {
            if (searchCurrent.getId() == currentIndex) {
                for (IndexNumber searchSwap : courseIndexNumbers) {
                    if (searchSwap.getId() == swappingIndex) {
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
                if (searchSwap.getId() == swappingIndex) {
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
