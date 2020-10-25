package Controller;

import ErrorMessage.ErrorMessage;
import FileManager.StorageManager;
import Model.Course;
import Model.IndexNumber;
import Model.Student;
import View.StudentUi;
import Exception.CourseRegisteredException;
import Exception.ClashingIndexNumberException;
import Exception.NoVacancyException;
import Exception.NoVacancySwapException;
import Exception.CourseInWaitListException;
import Exception.SameIndexNumberSwapException;

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
                    //swapIndex();
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
        } catch (CourseRegisteredException | ClashingIndexNumberException | CourseInWaitListException e) {
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
                ClashingIndexNumberException | CourseRegisteredException e) {
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
                , "You are currently registered for Index Number: " + indexNumberToBeChanged.getId());

        // Input the index you want to change to: //
        int index;
        index = studentUi.getIndexOfIndexNumberToChange(courseToBeChanged.getIndexNumbers());
        IndexNumber newIndexNumber = courseToBeChanged.getIndexNumbers().get(index);

        try {
            storageManager.swapIndexNumber(student.getUserId(), courseToBeChanged.getCourseCode(), newIndexNumber);
        } catch (NoVacancySwapException | ClashingIndexNumberException | SameIndexNumberSwapException e) {
            studentUi.printErrorMessage(e.getMessage());
        }
    }

//    private void swapIndex() {
//
//        // Input the course you want to swap //
//        ArrayList<Course> coursesStudent = storageManager.getCoursesTakenByStudent(student);
//        int indexStudent = studentUi.getIndexOfCourseToSwap(coursesStudent);
//        Course courseStudent = coursesStudent.get(indexStudent);
//        IndexNumber indexNumberStudent = student.getRegisteredIndexNumbers().get(courseStudent.getCourseCode());
//
//        // Input the index you want to swap to: //
//        int index;
//        index = studentUi.getIndexOfIndexNumberToSwap(courseStudent.getIndexNumbers());
//        IndexNumber indexNumberToBeSwap = courseStudent.getIndexNumbers().get(index);
//
//        // Checks username & password is correct
//        String username = studentUi.getLoginInfo("Enter username of student swapping with you: ");
//        String password = studentUi.getLoginInfo("Enter username of student swapping with you: ");
//        boolean verified = false;
//        LoginInfo toSwapInfo = new LoginInfo(username, password);
//        LoginInfoFileManager verify = new LoginInfoFileManager();
//        try {
//            ArrayList<LoginInfo> loginInfoList = verify.retrieveStudentLoginInfoList();
//            for (LoginInfo loginInfo: loginInfoList) {
//                if (loginInfo.equals(toSwapInfo)) {
//                    verified = true;
//                    break;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        // Once verified, check
//        if (verified) {
//            for (IndexNumber searchSwap : courseStudent.getIndexNumbers()) {
//                if (searchSwap == indexNumberToBeSwap) {
//                    ArrayList<String> studentList = searchSwap.getRegisteredStudents();
//                    for (String s : studentList) {
//                        if (s.equals(username)) {
//                            try {
//                                storageManager.dropCourseAndRegisterNextStudentInWaitList(student.getUserId(), courseStudent.getCourseCode(), indexNumberStudent);
//                                storageManager.registerForCourse(student.getUserId(), courseStudent.getCourseCode(),
//                                        indexNumberToBeSwap);
//                                System.out.printf("Changed index %d for %d\n", indexNumberStudent.getId(), indexNumberToBeSwap.getId());
//                            } catch (CourseRegisteredException | ClashingIndexNumberException | NoVacancyException e) {
//                                studentUi.printErrorMessage(e.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        else {
//            System.out.println("Error");
//        }
//    }

}
