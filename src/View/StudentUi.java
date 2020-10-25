package View;

import ErrorMessage.ErrorMessage;
import Model.Course;
import Enum.School;
import Enum.LessonType;
import Model.IndexNumber;
import Model.Lesson;
import Model.Student;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public int getIndexOfCourseToSwap(ArrayList<Course> courses) {
        int index;
        while (true) {
            index = getInputChoice("Which course would you like to swap?",
                    getCoursesDescription(courses, "Here are your registered courses:"));
            if (index < 1 || index > courses.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        return index - 1;
    }

    public int getIndexOfIndexNumberToSwap(ArrayList<IndexNumber> indexNumbers) {
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

    public int getIndex(String s) {
        System.out.print(s);
        Scanner sc = new Scanner(System.in);
        String input = "";

        while (true) {
            try {
                input = sc.next();
                if (input.equals("Q")) {
                    return 0;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printErrorMessage(ErrorMessage.INVALID_INDEX_NUMBER);
            }
        }
    }

    public String getCourseCode(String s) {
        System.out.print(s);
        Scanner sc = new Scanner(System.in);
        String input;

        while (true) {
            try {
                input = sc.next();
                if (input.equals("Q")) {
                    return null;
                }
                return input;
            } catch (NumberFormatException e) {
                printErrorMessage(ErrorMessage.INVALID_INDEX_NUMBER);
            }
        }
    }

    public String getLoginInfo(String s) {
        System.out.print(s);
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }
}

