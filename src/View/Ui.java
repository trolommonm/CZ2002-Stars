package View;

import ErrorMessage.ErrorMessage;
import Model.Course;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Ui {
    private final String dividerLine = "____________________________________________________________";
    private final String errorDividerLine = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";

    public void print(String message) {
        System.out.println(message);
    }

    private void printDivider() {
        System.out.println(dividerLine);
    }

    private void printErrorDivider() {
        System.out.println(errorDividerLine);
    }

    public void printMessageWithDivider(String... messages) {
        printDivider();
        for (String message: messages) {
            print(message);
        }
        printDivider();
    }

    public void printErrorMessage(String... messages) {
        printErrorDivider();
        print("Error Message: ");
        for (String message: messages) {
            print(message);
        }
        printErrorDivider();
    }

    public int getInputChoice(String finalMessageForInput, String... options) {
        int choice;
        while (true) {
            try {
                if (options.length != 0) {
                    printMessageWithDivider(options);
                }
                print(finalMessageForInput);
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        }

        return choice;
    }

    public int getInputChoice(String message) {
        return getInputChoice(message, new String[0]);
    }

    public String[] getCoursesDescription(ArrayList<Course> courses, String message) {
        ArrayList<String> coursesDescriptionList = new ArrayList<>();
        if (message != null) {
            coursesDescriptionList.add(message);
        }
        int index = 1;
        for (Course course: courses) {
            coursesDescriptionList.add((index++) + ". " + course.toString());
        }

        return coursesDescriptionList.toArray(String[]::new);
    }

    public String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
        case 11:
        case 12:
        case 13:
            return i + "th";
        default:
            return i + suffixes[i % 10];
        }
    }

}
