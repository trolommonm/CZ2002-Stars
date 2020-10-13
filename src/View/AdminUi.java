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
        String[] coursesStringList = new String[courses.size() + 1];
        coursesStringList[0] = message;
        for (int i = 0; i < courses.size(); i++) {
            coursesStringList[i+1] = (i+1) + ". " + courses.get(i).toString();
        }

        printMessageWithDivider(coursesStringList);
    }

    public void printCourses(ArrayList<Course> courses) {
        printCourses(courses, "Here are your list of courses:");
    }

    public int getUserChoice() {
        return getUserChoice("Enter your choice:");
    }

    public int getUserChoice(String message) {
        Scanner sc = new Scanner(System.in);

        print(message);
        int choice = sc.nextInt();

        return choice;
    }

    public String getCourseToEdit(ArrayList<Course> courses) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                printCourses(courses);

                print("Which course do you want to edit?");
                int index = sc.nextInt();

                return courses.get(index - 1).getCourseCode();
            } catch (InputMismatchException e) {
                print(ErrorMessage.ERROR_INPUT_CHOICE);
            } catch (IndexOutOfBoundsException e) {
                print(ErrorMessage.ERROR_INPUT_CHOICE);
            }
        }
    }

    public int getEditCourseChoice(Course course) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                printMessageWithDivider(
                        "Editing " + course.toString(),
                        "1. Edit course code.",
                        "2. Edit course name.",
                        "3. Edit school.",
                        "4. Add index number.",
                        "5. Edit index number vacancy."
                );

                int choice = sc.nextInt();
                if (choice < 1 || choice > 5) {
                    print(ErrorMessage.ERROR_INPUT_CHOICE);
                    continue;
                }

                return choice;
            } catch (NumberFormatException e) {
                print(ErrorMessage.ERROR_INPUT_CHOICE);
            }
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
        return getSchool("Enter the school offering the course (SCSE, SSS) (Currently: " + course.getSchool().toString() + ":");
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
                print(ErrorMessage.INVALID_SCHOOL);
                break;
            }
        }

        return school;
    }

    public Course getCourseToAdd() {
        Scanner sc = new Scanner(System.in);

        String courseName = getCourseName();
        String courseCode = getCourseCode();

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

        School school = getSchool();

        Course course = new Course(courseName, courseCode, totalSize, school);
        course.setIndexNumbers(getIndexNumbers());

        return course;
    }

    private String ordinal(int i) {
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

    public int getIndexOfIndexNumber(Course course) {
        String[] messages = new String[course.getIndexNumbers().size() + 1];
        messages[0] = "Index numbers for: " + course.toString();
        for (int i = 1; i < messages.length; i++) {
            messages[i] = (i) + ". Index number: " + course.getIndexNumbers().get(i-1).getIndexNumber()
                    + "(Current Max Vacancy: "
                    + course.getIndexNumbers().get(i-1).getMaxVacancy()
                    + ")";
        }
        printMessageWithDivider(messages);

        int index;
        while (true) {
            index = getUserChoice("Which one would you like to change?");
            if (index < 1 || index > course.getIndexNumbers().size()) {
                print(ErrorMessage.ERROR_INPUT_CHOICE);
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
                print(ErrorMessage.INVALID_MAX_VACANCY);
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
                print(ErrorMessage.INVALID_INDEX_NUMBER);
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
