package Controller;

import ErrorMessage.ErrorMessage;
import FileManager.LoginInfoFileManager;
import FileManager.LoginManager;
import FileManager.StorageManager;
import Model.Course;
import Model.IndexNumber;
import Model.LoginInfo;
import Model.Student;
import Enum.AccountType;
import View.StudentUi;
import Exception.CourseRegisteredException;
import Exception.ClashingRegisteredIndexNumberException;
import Exception.NoVacancyException;
import Exception.NoVacancySwapException;
import Exception.CourseInWaitListException;
import Exception.SameIndexNumberSwapException;
import Exception.WrongLoginInfoException;
import Exception.WrongAccessPeriodException;
import Exception.ClashingWaitListedIndexNumberException;
import Exception.PeerDoesNotTakeCourseException;
import Exception.PeerClashingRegisteredIndexNumberException;
import Exception.PeerClashingWaitListedIndexNumberException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StudentController {
    private Student student;
    private StudentUi studentUi;
    private StorageManager storageManager;
    private LoginManager loginManager;

    public StudentController(String userId) {
        studentUi = new StudentUi();
        storageManager = new StorageManager();
        student = storageManager.getStudent(userId);
        loginManager = new LoginManager(new LoginInfoFileManager(), storageManager);
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
                    dropRegisteredCourse();
                    break;
                case 3:
                    dropWaitListCourse();
                    break;
                case 4:
                    printRegisteredAndWaitListCourses();
                    break;
                case 5:
                    printVacancies();
                    break;
                case 6:
                    changeIndex();
                    break;
                case 7:
                    swapIndex();
                    break;
                case 8:
                    studentUi.printGoodBye();
                    break;
                default:
                    studentUi.printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        } while (choice != 8);
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
        } catch (CourseRegisteredException | ClashingRegisteredIndexNumberException | CourseInWaitListException
                | ClashingWaitListedIndexNumberException e) {
            studentUi.printErrorMessage(e.getMessage());
        } catch (NoVacancyException e) {
            studentUi.printMessageWithDivider(e.getMessage());
            storageManager.addCourseToWaitList(student.getUserId(), courseToBeAdded.getCourseCode()
                    , indexNumberToBeAdded);
        }
    }

    private void dropRegisteredCourse() {
        ArrayList<Course> registeredCourses = storageManager.getCoursesTakenByStudent(student);
        if (registeredCourses.isEmpty()) {
            studentUi.printMessageWithDivider(ErrorMessage.NO_REGISTERED_COURSES);
            return;
        }
        int index = studentUi.getIndexOfCourseToDrop(registeredCourses, true);
        Course course = registeredCourses.get(index);
        IndexNumber indexNumber = student.getRegisteredIndexNumbers().get(course.getCourseCode());
        try {
            storageManager.dropCourseAndRegisterNextStudentInWaitList(student.getUserId(), course.getCourseCode(), indexNumber);
        } catch (NoVacancyException | CourseInWaitListException |
                ClashingRegisteredIndexNumberException | CourseRegisteredException
                | ClashingWaitListedIndexNumberException e) {
            assert false : "These exceptions should have already been accounted for when you add the course into wait list...";
        }
    }

    private void dropWaitListCourse() {
        ArrayList<Course> waitListCourses = storageManager.getCoursesInWaitListByStudent(student);
        if (waitListCourses.isEmpty()) {
            studentUi.printMessageWithDivider(ErrorMessage.NO_WAITLIST_COURSES);
            return;
        }
        int index = studentUi.getIndexOfCourseToDrop(waitListCourses, false);
        Course course = waitListCourses.get(index);
        IndexNumber indexNumber = student.getWaitListIndexNumbers().get(course.getCourseCode());
        storageManager.dropCourseFromWaitList(student.getUserId(), course.getCourseCode(), indexNumber);
    }

    private void printRegisteredAndWaitListCourses() {
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
        studentUi.printMessageWithDivider("Here are the courses you are registered for:", registeredCourses);

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
        studentUi.printMessageWithDivider("Here are the courses on your wait list:", waitListCourses);
    }

    private void printVacancies() {
        studentUi.checkVacancyOfIndexNumber(storageManager.getAllCourses());
    }

    private void changeIndex() {
        // Input the course you want to change //
        ArrayList<Course> courses = storageManager.getCoursesTakenByStudent(student);
        int indexStudent = studentUi.getIndexOfCourseToChange(courses);
        Course courseToBeChanged = courses.get(indexStudent);
        IndexNumber indexNumberToBeChanged = student.getRegisteredIndexNumbers().get(courseToBeChanged.getCourseCode());
        studentUi.printMessageWithDivider("Swapping index for course: " + courseToBeChanged.toString()
                , "You are currently registered for Index Number:\n" + indexNumberToBeChanged.getId());

        // Input the index you want to change to: //
        int index;
        index = studentUi.getIndexOfIndexNumberToChange(courseToBeChanged.getIndexNumbers());
        IndexNumber newIndexNumber = courseToBeChanged.getIndexNumbers().get(index);

        try {
            storageManager.swapIndexNumber(student.getUserId(), courseToBeChanged.getCourseCode(), newIndexNumber);
            studentUi.printMessageWithDivider("Index Number: " + indexNumberToBeChanged.getId() + " for "
                    + courseToBeChanged.toString() + " has been successfully changed to " + newIndexNumber.getId());
        } catch (NoVacancySwapException | ClashingRegisteredIndexNumberException | SameIndexNumberSwapException e) {
            studentUi.printErrorMessage(e.getMessage());
        }
    }

    private void swapIndex() {
        int index = studentUi.getIndexOfCourseToSwap(student);
        Course courseToBeSwapped = storageManager.getCourse(student.getRegisteredCourseCodes().get(index));

        LoginInfo loginInfoOfPeer = studentUi.getLoginInfoOfPeer();
        try {
            loginManager.verifyLoginInfo(AccountType.STUDENT, loginInfoOfPeer);
        } catch (FileNotFoundException | WrongLoginInfoException e) {
            studentUi.printErrorMessage(e.getMessage());
            return;
        } catch (WrongAccessPeriodException e) {
            // ignore, doesn't matter since we are just swapping index with peer
        }

        Student peer = storageManager.getStudent(loginInfoOfPeer.getUserId());
        if (!studentUi.confirmSwapWithPeer(student, peer, courseToBeSwapped)) {
            return;
        }

        try {
            storageManager.swapIndexWithPeer(student.getUserId(), peer.getUserId(), courseToBeSwapped.getCourseCode());
        } catch (PeerClashingRegisteredIndexNumberException | SameIndexNumberSwapException
                | ClashingRegisteredIndexNumberException | PeerDoesNotTakeCourseException
                | PeerClashingWaitListedIndexNumberException | ClashingWaitListedIndexNumberException e) {
            studentUi.printErrorMessage(e.getMessage());
        }
    }

}
