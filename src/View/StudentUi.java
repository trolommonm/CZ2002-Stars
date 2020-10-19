package View;

import ErrorMessage.ErrorMessage;
import Model.Course;
import Enum.School;
import Enum.LessonType;
import Model.IndexNumber;
import Model.Lesson;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class StudentUi extends Ui {
    private final String[] studentMenuOptions = {
            "Welcome to the STARS!",
            "1. Add a course.",
            "2. Drop a course.",
            "3. Check/Print Courses Registered .",
            "4. Check Vacancies Available.",
            "5. Change Index Number of Course.",
            "6. Swap Index Number with Another Student.",
            "7. Quit."
    };

    public void printWelcomeMessage(String studentName) {
        printMessageWithDivider(
                "Welcome, " + studentName + "!"
        );
    }

    public int getMenuInputChoice() {
        return getInputChoice("Enter your choice:", studentMenuOptions);
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

