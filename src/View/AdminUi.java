package View;

import ErrorMessage.ErrorMessage;
import Exception.InvalidLessonTypeException;
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

public class AdminUi extends Ui {
    private final String[] adminMenuOptions = {
            "Welcome to the Admin panel!",
            "1. Edit student access period.",
            "2. Add a student.",
            "3. Add a course.",
            "4. Edit a course.",
            "5. Check available slot for an index number.",
            "6. Print student list by index number.",
            "7. Print student list by course.",
            "8. Quit."
    };

    private String[] getEditCourseOptions(Course course) {
        return new String[] {
                "Editing " + course.toString(),
                "1. Edit course code.",
                "2. Edit course name.",
                "3. Edit school.",
                "4. Add index number.",
                "5. Edit index number vacancy."
        };
    }

    public void printWelcomeMessage(String adminName) {
        printMessageWithDivider(
                "Welcome, " + adminName + "!"
        );
    }

    public void checkVacancyOfIndexNumber(ArrayList<Course> courses) {
        int choice;
        while (true) {
            choice = getInputChoice("Which course do you want to check?",
                    getCoursesDescription(courses, "Here are your list of courses:"));
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

    public void printCourses(ArrayList<Course> courses, String message) {
        printMessageWithDivider(getCoursesDescription(courses, message));
    }

    public void printCourses(ArrayList<Course> courses) {
        printCourses(courses, "Here are your list of courses:");
    }

    public int getMenuInputChoice() {
        return getInputChoice("Enter your choice:", adminMenuOptions);
    }

    public String getCourseToEdit(ArrayList<Course> courses) {
        while (true) {
            try {
                printCourses(courses);

                int index = getInputChoice("Which course do you want to edit?");
                if (index < 1 || index > courses.size()) {
                    printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                    continue;
                }

                return courses.get(index - 1).getCourseCode();
            }  catch (IndexOutOfBoundsException e) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        }
    }

    public int getEditCourseChoice(Course course) {
        while (true) {
            int choice = getInputChoice("Enter your choice:", getEditCourseOptions(course));
            if (choice < 1 || choice > 5) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }

            return choice;
        }
    }

    public String getCourseCode() {
        return getCourseCode("Enter course code:");
    }

    public String getNewCourseCode(Course course) {
        return getCourseCode("Enter the new course code (currently: " + course.getCourseCode() + "):");
    }

    public String getCourseCode(String message) {
        Scanner sc = new Scanner(System.in);
        print(message);
        String courseCode = sc.next();

        return courseCode;
    }

    public String getCourseName() {
        return getCourseName("Enter course name:");
    }

    public String getNewCourseName(Course course) {
        return getCourseName("Enter the new course name (currently: " + course.getCourseName() + "):");
    }

    public String getCourseName(String message) {
        Scanner sc = new Scanner(System.in);
        print(message);
        String courseName = sc.nextLine();

        return courseName;
    }

    public School getSchool() {
        return getSchool("Enter the school offering the course (SCSE, SSS):");
    }

    public School getNewSchool(Course course) {
        return getSchool("Enter the school offering the course (SCSE, SSS) (Currently: "
                + course.getSchool().toString() + "):");
    }

    public School getSchool(String message) {
        Scanner sc = new Scanner(System.in);
        School school;
        getSchoolLoop: while(true) {
            print(message);
            String schoolString = sc.next();
            switch (schoolString) {
            case "SCSE":
                school = School.SCSE;
                break getSchoolLoop;
            case "SSS":
                school = School.SSS;
                break getSchoolLoop;
            default:
                printErrorMessage(ErrorMessage.INVALID_SCHOOL);
                break;
            }
        }

        return school;
    }

    public Course getCourseToAdd() {
        Scanner sc = new Scanner(System.in);

        String courseName = getCourseName();
        String courseCode = getCourseCode();
        School school = getSchool();

        Course course = new Course(courseName, courseCode, school);
        course.setIndexNumbers(getIndexNumbers());

        return course;
    }

    public int getIndexOfIndexNumberToEdit(Course course) {
        String[] messages = new String[course.getIndexNumbers().size() + 1];
        messages[0] = "Index numbers for: " + course.toString();
        for (int i = 1; i < messages.length; i++) {
            messages[i] = (i) + ". Index number: " + course.getIndexNumbers().get(i-1).getIndexNumber()
                    + " (Current Max Vacancy: "
                    + course.getIndexNumbers().get(i-1).getMaxVacancy()
                    + ")";
        }

        int index;
        while (true) {
            index = getInputChoice("Which one would you like to change?", messages);
            if (index < 1 || index > course.getIndexNumbers().size()) {
                printErrorMessage(ErrorMessage.ERROR_INPUT_CHOICE);
                continue;
            }

            break;
        }

        return index - 1;
    }

    public int getNewMaxVacancy(Course course, int index) {
        return getMaxVacancy("Enter the new maximum vacancy for index "
                + course.getIndexNumbers().get(index).getIndexNumber()
                + " (Currently: "
                + course.getIndexNumbers().get(index).getMaxVacancy() + "):");
    }

    private int getMaxVacancy(String message) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                print(message);
                int maxVacancy = sc.nextInt();
                return maxVacancy;
            } catch (NumberFormatException e) {
                printErrorMessage(ErrorMessage.INVALID_MAX_VACANCY);
            }
        }
    }

    public IndexNumber getIndexNumber(boolean isAdd) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        int counter = 1;

        while (true) {
            try {
                if (isAdd) {
                    print("Enter the new index number to add:");
                }
                input = sc.next();
                if (input.equals("Q")) {
                    return null;
                }
                int indexNumberInt = Integer.parseInt(input);

                int maxVacancy = getMaxVacancy("Enter the maximum vacancy of this index:");

                ArrayList<Lesson> lessons = getLessons(indexNumberInt);

                return new IndexNumber(indexNumberInt, lessons, maxVacancy);
            } catch (NumberFormatException e) {
                printErrorMessage(ErrorMessage.INVALID_INDEX_NUMBER);
            }
        }
    }

    private ArrayList<IndexNumber> getIndexNumbers() {
        ArrayList<IndexNumber> indexNumbers = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String input = "";
        int counter = 1;

        while (true) {
            try {
                print("Enter the " + ordinal(counter) + " index number (Enter Q if you are done):");

                IndexNumber indexNumber = getIndexNumber(false);
                if (indexNumber == null) {
                    break;
                }

                indexNumbers.add(indexNumber);

                counter++;
            } catch (NumberFormatException e) {
                printErrorMessage(ErrorMessage.INVALID_INDEX_NUMBER);
            }
        }

        return indexNumbers;
    }

    private LocalTime getTime(String message) {
        LocalTime time;
        while (true) {
            print(message);
            Scanner sc = new Scanner(System.in);
            String startTimeString = sc.next();
            try {
                time = LocalTime.of(Integer.parseInt(startTimeString.substring(0, 2)),
                        Integer.parseInt(startTimeString.substring(2)));
                break;
            } catch (NumberFormatException | DateTimeException e) {
                printErrorMessage(ErrorMessage.INVALID_TIME);
            }
        }

        return time;
    }

    private ArrayList<Lesson> getLessons(int indexNumber) {
        ArrayList<Lesson> lessons = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int counter = 1;
        getLessonLoop: while (true) {
            print(ordinal(counter) + " Lesson for index " + indexNumber);

            LessonType lessonType;
            getLessonTypeLoop: while(true) {
                print("Enter the type of lesson " + LessonType.getAllLessonType()
                        + " (Enter Q if you are done):");

                String lessonTypeString = sc.next();
                if (lessonTypeString.equals("Q")) {
                    break getLessonLoop;
                }
                try {
                    lessonType = LessonType.getLessonType(lessonTypeString);
                    break getLessonTypeLoop;
                } catch (InvalidLessonTypeException e) {
                    printErrorMessage(e.getMessage());
                }
            }

            DayOfWeek dayOfWeek;
            while (true) {
                print("1. Monday\n" + "2. Tuesday\n" +
                        "3. Wednesday\n" + "4. Thursday\n" +
                        "5. Friday\n" + "6. Saturday\n" + "7. Sunday");
                print("Enter the day of the week of the lesson (e.g. 1 for Monday, 2 for Tuesday):");

                try {
                    int day = sc.nextInt();
                    if (day < 1 || day > 7) {
                        printErrorMessage(ErrorMessage.INVALID_DAY_OF_WEEK);
                        continue;
                    }

                    dayOfWeek = DayOfWeek.of(day);
                    break;
                } catch (InputMismatchException e) {
                    printErrorMessage(ErrorMessage.INVALID_DAY_OF_WEEK);
                }
            }

            LocalTime startTime = getTime("Enter the start time (in 24 hours format e.g. 0830):");
            LocalTime endTime = getTime("Enter the end time (in 24 hours format e.g. 2341):");

            counter++;
            lessons.add(new Lesson(lessonType, dayOfWeek, startTime, endTime));
        }

        return lessons;
    }

}
