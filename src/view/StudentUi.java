package view;

import errormessage.ErrorMessage;
import model.AccountType;
import model.Course;
import model.IndexNumber;
import model.LoginInfo;
import model.Student;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentUi extends Ui {
    private final String[] studentMenuOptions = {
            "Welcome to the STARS!",
            "1. Add a course.",
            "2. Drop a registered course.",
            "3. Drop a wait list course.",
            "4. Check/Print Courses Registered.",
            "5. Check Vacancies Available.",
            "6. Change Index Number of Course.",
            "7. Swap Index Number with Another Student.",
            "8. Quit."
    };

    public void printWelcomeMessage(String studentName) {
        printMessageWithDivider(
                "Welcome, " + studentName + "!"
        );
    }

    public int getMenuInputChoice() {
        return getInputChoice("Enter your choice:", studentMenuOptions);
    }

    public int getIndexOfCourseToChange(ArrayList<Course> courses) {
        int index;
        while (true) {
            index = getInputChoice("Which course would you like to change?",
                    getCoursesDescription(courses, "Here are your registered courses:"));
            if (index < 1 || index > courses.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public int getIndexOfCourseToSwap(Student student) {
        String registeredCourses = "Here are your registered courses:\n";
        int index = 1;
        for (String courseCode: student.getRegisteredCourseCodes()) {
            IndexNumber indexNumber = student.getRegisteredIndexNumbers().get(courseCode);
            registeredCourses += (index) + ". " + indexNumber.getCourse().toString();
            registeredCourses += "\n\t" + indexNumber.getFullDescription();
            if (index != student.getRegisteredCourseCodes().size()) {
                registeredCourses += "\n";
            }
            index++;
        }

        while (true) {
            index = getInputChoice("Which course would you like to swap?", registeredCourses);
            if (index < 1 || index > student.getRegisteredCourseCodes().size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public int getIndexOfIndexNumberToChange(ArrayList<IndexNumber> indexNumbers) {
        int index;
        while (true) {
            index = getInputChoice("Which index number would you like to change to:?",
                    getIndexNumbersDescription(indexNumbers, "Here are the index numbers:"));
            if (index < 1 || index > indexNumbers.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public int getIndexOfCourseToDrop(ArrayList<Course> courses, boolean dropRegistered) {
        int index;
        while (true) {
            if (dropRegistered) {
                index = getInputChoice("Which course would you like to drop?",
                        getCoursesDescription(courses, "Here are your registered courses:"));
            } else {
                index = getInputChoice("Which course would you like to drop?",
                        getCoursesDescription(courses, "Here are your wait listed courses:"));
            }
            if (index < 1 || index > courses.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public int getIndexOfCourseToRegister(ArrayList<Course> courses) {
        int index;
        while (true) {
            index = getInputChoice("Which course would you like to add?",
                    getCoursesDescription(courses, "Here are the available courses: "));
            if (index < 1 || index > courses.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public int getIndexOfIndexNumberToRegister(ArrayList<IndexNumber> indexNumbers) {
        int index;
        while (true) {
            index = getInputChoice("Which index number would you like to add?",
                    getIndexNumbersDescription(indexNumbers, "Here are the index numbers:"));
            if (index < 1 || index > indexNumbers.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public boolean confirmSwapWithPeer(Student student, Student peer, Course courseToBeSwapped) {
        IndexNumber yourIndexNumber = student.getRegisteredIndexNumbers().get(courseToBeSwapped.getCourseCode());
        String yourIndexNumberMessage = "Your index number:\n";
        yourIndexNumberMessage += yourIndexNumber.getFullDescription();

        IndexNumber peerIndexNumber  = peer.getRegisteredIndexNumbers().get(courseToBeSwapped.getCourseCode());
        String peerIndexNumberMessage = "Your peer " + peer.getName() + "'s index number:\n";
        peerIndexNumberMessage += peerIndexNumber.getFullDescription();

        Scanner sc = new Scanner(System.in);
        while (true) {
            printMessageWithDivider("Confirm swap for " + courseToBeSwapped.getCourseCode() + " "
                    + courseToBeSwapped.getCourseName() + " with peer?\n", yourIndexNumberMessage, peerIndexNumberMessage);
            print("Enter (Y for yes, N for no): ");
            String input = sc.next();
            if (input.trim().toUpperCase().equals("Y")) {
                return true;
            } else if (input.trim().toUpperCase().equals("N")) {
                return false;
            } else {
                printErrorMessage(ErrorMessage.INVALID_CONFIRM_SWAP_PEER);
            }
        }
    }

    public LoginInfo getLoginInfoOfPeer() {
        Scanner sc = new Scanner(System.in);
        Console con = System.console();
        String userId;
        //char[] passwordCharArray;
        //String passwordString;
        String password;

        print("Enter your peer's user id:");
        userId = sc.next();

        print("Enter your peer's password:");
        password = sc.next();
        //passwordCharArray = con.readPassword();
        //passwordString = String.valueOf(passwordCharArray);

        return new LoginInfo(AccountType.STUDENT, userId, password);
    }
}

