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
import Exception.CourseInWaitListException;

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
                    swapIndex();
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
        } catch (CourseRegisteredException | ClashingIndexNumberException | CourseInWaitListException e) {
            studentUi.printErrorMessage(e.getMessage());
        } catch (NoVacancyException e) {
            studentUi.printMessageWithDivider(e.getMessage());
            storageManager.addCourseToWaitList(student.getUserId(), courseToBeAdded.getCourseCode()
                    , indexNumberToBeAdded);
        }
    }

    private void dropCourse() {
        ArrayList<Course> courses = storageManager.getCoursesTakenByStudent(student);
        int index = studentUi.getIndexOfCourseToDrop(courses);
        Course course = courses.get(index);
        IndexNumber indexNumber = student.getRegisteredIndexNumbers().get(course.getCourseCode());
        try {
            storageManager.dropCourse(student.getUserId(), course.getCourseCode(), indexNumber);
        } catch (NoVacancyException | CourseInWaitListException |
                ClashingIndexNumberException | CourseRegisteredException e) {
            assert false : "These exceptions should have already been accounted for when you add the course into wait list...";
        }
    }

    private void printRegisteredCourses() {
        String registeredCourses = "\n";
        int index = 1;
        for (String courseCode: student.getRegisteredCourseCodes()) {
            Course course = storageManager.getCourse(courseCode);
            registeredCourses += (index) + ". " + course.toString();
            registeredCourses += "\n\t" + student.getRegisteredIndexNumbers().get(courseCode).getFullDescription();
            if (index != student.getRegisteredCourseCodes().size()) {
                registeredCourses += "\n";
            }
            index++;
        }
        studentUi.printMessageWithDivider("Here are your registered courses:", registeredCourses);

        String waitListCourses = "\n";
        for (String courseCode: student.getWaitListCourseCodes()) {
            Course course = storageManager.getCourse(courseCode);
            waitListCourses += (index) + ". " + course.toString();
            waitListCourses += "\n\t" + student.getWaitListIndexNumbers().get(courseCode).getFullDescription();
            if (index != student.getWaitListIndexNumbers().size()) {
                registeredCourses += "\n";
            }
            index++;
        }
        studentUi.printMessageWithDivider("Here are your wait list courses:", waitListCourses);
    }

    private void printVacancies() {
        studentUi.checkVacancyOfIndexNumber(storageManager.getAllCourses());
    }

    private void changeIndex() {

        // Input the course you want to change //
        ArrayList<Course> coursesStudent = storageManager.getCoursesTakenByStudent(student);
        int indexStudent = studentUi.getIndexOfCourseToChange(coursesStudent);
        Course courseStudent = coursesStudent.get(indexStudent);
        IndexNumber indexNumberStudent = student.getRegisteredIndexNumbers().get(courseStudent.getCourseCode());

        // Input the index you want to change to: //
        int index;
        index = studentUi.getIndexOfIndexNumberToChange(courseStudent.getIndexNumbers());
        IndexNumber indexNumberToBeChanged = courseStudent.getIndexNumbers().get(index);

        try {
            if (indexNumberToBeChanged.getAvailableVacancy() != 0) {
                storageManager.dropCourse(student.getUserId(), courseStudent.getCourseCode(), indexNumberStudent);
                storageManager.registerForCourse(student.getUserId(), courseStudent.getCourseCode(),
                        indexNumberToBeChanged);
                System.out.printf("Changed index %d for %d\n", indexNumberStudent.getId(), indexNumberToBeChanged.getId());
            }
            else {
                System.out.printf("No available vacancy for %d\n", index);
            }
        } catch (CourseRegisteredException | ClashingIndexNumberException | NoVacancyException e) {
            studentUi.printErrorMessage(e.getMessage());
        }
    }

    private void swapIndex() {

        // Input the course you want to swap //
        ArrayList<Course> coursesStudent = storageManager.getCoursesTakenByStudent(student);
        int indexStudent = studentUi.getIndexOfCourseToSwap(coursesStudent);
        Course courseStudent = coursesStudent.get(indexStudent);
        IndexNumber indexNumberStudent = student.getRegisteredIndexNumbers().get(courseStudent.getCourseCode());

        // Input the index you want to swap to: //
        int index;
        index = studentUi.getIndexOfIndexNumberToSwap(courseStudent.getIndexNumbers());
        IndexNumber indexNumberToBeSwap = courseStudent.getIndexNumbers().get(index);

        // Checks username & password is correct
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

        // Once verified, check
        if (verified) {
            for (IndexNumber searchSwap : courseStudent.getIndexNumbers()) {
                if (searchSwap == indexNumberToBeSwap) {
                    ArrayList<String> studentList = searchSwap.getStudentUserIds();
                    for (String s : studentList) {
                        if (s.equals(username)) {
                            try {
                                storageManager.dropCourse(student.getUserId(), courseStudent.getCourseCode(), indexNumberStudent);
                                storageManager.registerForCourse(student.getUserId(), courseStudent.getCourseCode(),
                                        indexNumberToBeSwap);
                                System.out.printf("Changed index %d for %d\n", indexNumberStudent.getId(), indexNumberToBeSwap.getId());
                            } catch (CourseRegisteredException | ClashingIndexNumberException | NoVacancyException e) {
                                studentUi.printErrorMessage(e.getMessage());
                            }
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
