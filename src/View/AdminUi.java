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

public class AdminUi extends Ui {

    public void printWelcomeMessage(String adminName) {
        printMessageWithDivider(
                "Welcome, " + adminName + "!"
        );
    }

    public void printMenu() {
        printMessageWithDivider(
                "Welcome to the Admin panel!",
                "1. Edit student access period.",
                "2. Add a student.",
                "3. Add a course.",
                "4. Edit a course.",
                "5. Check available slot for an index number.",
                "6. Print student list by index number.",
                "7. Print student list by course.",
                "8. Quit."
        );
    }

    public void printCourses(ArrayList<Course> courses, String message) {
        String[] coursesStringList = new String[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            coursesStringList[i] = (i+1) + ". " + courses.get(i).toString();
        }

        printMessageWithDivider(message + coursesStringList);
    }

    public void printCourses(ArrayList<Course> courses) {
        printCourses(courses, "Here are your list of courses:");
    }

    public int getUserChoice() {
        Scanner sc = new Scanner(System.in);

        print("Enter your choice:");
        int choice = sc.nextInt();

        return choice;
    }

    public Course getCourseToAdd() {
        Scanner sc = new Scanner(System.in);

        print("Enter course name:");
        String courseName = sc.nextLine();
        print("Enter course code:");
        String courseCode = sc.next();

        int totalSize;
        while (true) {
          try {
              print("Enter total size:");
              totalSize = sc.nextInt();
              break;
          } catch (InputMismatchException e) {
              print(ErrorMessage.INVALID_TOTAL_SIZE);
              continue;
          }
        }

        School school;
        getSchoolLoop: while(true) {
            print("Enter the school offering the course (SCSE, SSS):");
            String schoolString = sc.next();
            switch (schoolString) {
            case "SCSE":
                school = School.SCSE;
                break getSchoolLoop;
            case "SSS":
                school = School.SSS;
                break getSchoolLoop;
            default:
                print(ErrorMessage.INVALID_SCHOOL);
                break;
            }
        }

        Course course = new Course(courseName, courseCode, totalSize, school);
        course.setIndexNumbers(getIndexNumbers());

        return course;
    }

    private String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
        case 11:
        case 12:
        case 13:
            return i + "th";
        default:
            return i + sufixes[i % 10];
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
                input = sc.next();
                if (input.equals("Q")) {
                    break;
                }
                int indexNumber = Integer.parseInt(input);

                getLessons(indexNumber);

                counter++;
            } catch (NumberFormatException e) {
                print(ErrorMessage.INVALID_INDEX_NUMBER);
            }
        }

        return indexNumbers;
    }

    private ArrayList<Lesson> getLessons(int indexNumber) {
        ArrayList<Lesson> lessons = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int counter = 1;
        getLessonLoop: while (true) {
            print(ordinal(counter) + " Lesson for index " + indexNumber);

            LessonType lessonType;
            getLessonTypeLoop: while(true) {
                print("Enter the type of lesson (LEC, TUT, LAB) (Enter Q if you are done):");
                String lessonTypeString = sc.next();
                switch (lessonTypeString) {
                case "LEC":
                    lessonType = LessonType.LECTURE;
                    break getLessonTypeLoop;
                case "TUT":
                    lessonType = LessonType.TUTORIAL;
                    break getLessonTypeLoop;
                case "LAB":
                    lessonType = LessonType.LAB;
                    break getLessonTypeLoop;
                case "Q":
                    break getLessonLoop;
                default:
                    print(ErrorMessage.INVALID_LESSON_TYPE);
                    break;
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
                        print(ErrorMessage.INVALID_DAY_OF_WEEK);
                        continue;
                    }

                    dayOfWeek = DayOfWeek.of(day);
                    break;
                } catch (InputMismatchException e) {
                    print(ErrorMessage.INVALID_DAY_OF_WEEK);
                }
            }

            LocalTime startTime;
            while (true) {
                print("Enter the start time (in 24 hours format e.g. 0830):");
                String startTimeString = sc.next();
                try {
                    startTime = LocalTime.of(Integer.parseInt(startTimeString.substring(0, 2)),
                            Integer.parseInt(startTimeString.substring(2)));
                    break;
                } catch (NumberFormatException e) {
                    print(ErrorMessage.INVALID_TIME);
                } catch (DateTimeException e) {
                    print(ErrorMessage.INVALID_TIME);
                }
            }

            LocalTime endTime;
            while (true) {
                print("Enter the end time (in 24 hours format e.g. 2341):");
                String endTimeString = sc.next();
                try {
                    endTime = LocalTime.of(Integer.parseInt(endTimeString.substring(0, 2)),
                            Integer.parseInt(endTimeString.substring(2)));
                    break;
                } catch (NumberFormatException e) {
                    print(ErrorMessage.INVALID_TIME);
                } catch (DateTimeException e) {
                    print(ErrorMessage.INVALID_TIME);
                }
            }

            counter++;
            lessons.add(new Lesson(lessonType, dayOfWeek, startTime, endTime));
        }

        return lessons;
    }

}
