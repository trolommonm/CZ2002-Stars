package view;

import errormessage.ErrorMessage;
import model.AccountType;
import model.Course;
import model.IndexNumber;
import model.LoginInfo;
import model.Student;
import utility.SHA256Hasher;

import java.io.Console;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    public void printGoodBye() {
        printMessageWithDivider("Logging off... Hope to see you again in STARS!");
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

    public String[] getIndexNumbersDescription(ArrayList<IndexNumber> indexNumbers, String message) {
        ArrayList<String> indexNumbersDescriptionList = new ArrayList<>();
        if (message != null) {
            indexNumbersDescriptionList.add(message);
        }
        int index = 1;
        for (IndexNumber indexNumber: indexNumbers) {
            indexNumbersDescriptionList.add((index++) + ". " + indexNumber.getFullDescription());
        }

        return indexNumbersDescriptionList.toArray(String[]::new);
    }

    public String[] getStudentsDescription(ArrayList<Student> students, String message) {
        ArrayList<String> studentsDescriptionList = new ArrayList<>();
        if (message != null) {
            studentsDescriptionList.add(message);
        }
        int index = 1;
        for (Student student: students) {
            studentsDescriptionList.add((index++) + ". " + student.toString());
        }

        return studentsDescriptionList.toArray(String[]::new);
    }

    public void checkVacancyOfIndexNumber(ArrayList<Course> courses) {
        int choice;
        while (true) {
            choice = getInputChoice("Which course do you want to check?",
                    getCoursesDescription(courses, "Here are the list of courses:"));
            if (choice < 1 || choice > courses.size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }
            break;
        }
        printIndexNumberVacancies(courses.get(choice-1).getIndexNumbers(), courses.get(choice-1));
    }

    public void printIndexNumberVacancies(ArrayList<IndexNumber> indexNumbers, Course course) {
        List<String> indexNumbersString = indexNumbers
                .stream()
                .map((i) -> i.toString())
                .collect(Collectors.toList());
        indexNumbersString.add(0, "Here are the vacancies for " + course.toString());
        printMessageWithDivider(indexNumbersString.toArray(String[]::new));
    }

    public LoginInfo getLoginInfo(AccountType accountType) {
        Scanner sc = new Scanner(System.in);
        Console con = System.console();
        String userId;
        char[] passwordCharArray;
        String passwordString;
        //String password;

        print("Enter user id:");
        userId = sc.next();

        print("Enter password:");
        //password = sc.next();
        passwordCharArray = con.readPassword();
        passwordString = String.valueOf(passwordCharArray);

        return new LoginInfo(accountType, userId, SHA256Hasher.hash(passwordString));
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
