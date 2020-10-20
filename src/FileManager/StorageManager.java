package FileManager;

import Model.*;
import Enum.Gender;
import Enum.School;
import Enum.LessonType;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class StorageManager {
    private Storage storage;

    public StorageManager() {
        load();
    }

    public ArrayList<Student> getStudentsInCourse(Course course) {
        ArrayList<Student> students = new ArrayList<>();
        for (IndexNumber indexNumber: course.getIndexNumbers()) {
            for (String matricNumber: indexNumber.getStudentMatricNumbers()) {
                students.add(storage.getStudent(matricNumber));
            }
        }

        return students;
    }

    public ArrayList<Course> getCoursesTakenByStudent(Student student) {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseCode: student.getCourseCodes()) {
            courses.add(storage.getCourse(courseCode));
        }

        return courses;
    }

    public void addCourse(Course course) {
        storage.addCourse(course);
        save();
    }

    public Course getCourse(String courseCode) {
        return storage.getCourse(courseCode);
    }

    public void setNewAccessTime(String userId, AccessTime newAccessTime) {
        storage.setAccessTime(userId, newAccessTime);
        save();
    }

    public void setNewCourseCode(String newCourseCode, String forCourseCode) {
        Course oldCourse = getCourse(forCourseCode);
        Course updatedCourse = new Course(oldCourse.getCourseName(), newCourseCode, oldCourse.getSchool());
        updatedCourse.setIndexNumbers(oldCourse.getIndexNumbers());

        storage.removeCourse(oldCourse);
        storage.addCourse(updatedCourse);

        save();
    }

    public void setNewCourseName(String newCourseName, String forCourseCode) {
        getCourse(forCourseCode).setCourseName(newCourseName);
        save();
    }

    public void setNewSchool(School newSchool, String forCourseCode) {
        getCourse(forCourseCode).setSchool(newSchool);
        save();
    }

    public void setNewMaxVacancy(String courseCode, int index, int newMaxVacancy) throws Exception {
        getCourse(courseCode).getIndexNumbers().get(index).setMaxVacancy(newMaxVacancy);
        save();
    }

    public ArrayList<Course> getAllCourses() {
        return storage.getAllCourses();
    }

    public void addStudent(Student student) {
        storage.addStudent(student);
        save();
    }

    public void addIndexNumber(IndexNumber indexNumber, String courseCode) {
        storage.addIndexNumber(indexNumber, courseCode);
        save();
    }

    public Student getStudent(String userId) {
        return storage.getStudent(userId);
    }

    public ArrayList<Student> getAllStudents() {
        return storage.getAllStudents();
    }

    private void preload() {
        storage = new Storage();
        File studentFile = new File("data/preload/Students.txt");
        try {
            Scanner sc = new Scanner(studentFile);
            while (sc.hasNext()) {
                storage.addStudent(parseStudentFromTxt(sc.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File[] courseFiles = new File("data/preload/Courses").listFiles();
        for (File file: courseFiles) {
            try {
                Scanner sc = new Scanner(file);
                Course course = parseCourseFromTxt(sc.nextLine());
                ArrayList<IndexNumber> indexNumbers = new ArrayList<>();
                while (sc.hasNext()) {
                    indexNumbers.add(parseIndexNumberFromTxt(sc.nextLine()));
                }

                course.setIndexNumbers(indexNumbers);
                storage.addCourse(course);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        save();
    }

    private IndexNumber parseIndexNumberFromTxt(String line) {
        String[] lineSplit = line.split("\\|");
        int indexNumber = Integer.parseInt(lineSplit[0]);
        int maxVacancy = Integer.parseInt(lineSplit[1]);
        ArrayList<Lesson> lessons = new ArrayList<>();
        for (int i = 2; i < lineSplit.length; i++) {
            String[] lessonSplit = lineSplit[i].split("-");
            LessonType lessonType;
            switch (lessonSplit[0]) {
            case "LEC":
                lessonType = LessonType.LECTURE;
                break;
            case "TUT":
                lessonType = LessonType.TUTORIAL;
                break;
            case "LAB":
                lessonType = LessonType.LAB;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + lessonSplit[0]);
            }
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(lessonSplit[1]);
            String startTimeString = lessonSplit[2];
            String endTimeString = lessonSplit[3];
            LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeString.substring(0, 2)),
                    Integer.parseInt(startTimeString.substring(2)));
            LocalTime endTime = LocalTime.of(Integer.parseInt(endTimeString.substring(0, 2)),
                    Integer.parseInt(endTimeString.substring(2)));

            lessons.add(new Lesson(lessonType, dayOfWeek, startTime, endTime));
        }

        return new IndexNumber(indexNumber, lessons, maxVacancy);
    }

    private Course parseCourseFromTxt(String line) {
        String[] lineSplit = line.split("\\|");
        String courseName = lineSplit[0];
        String courseCode = lineSplit[1];
        School school;
        switch (lineSplit[2]) {
        case "SCSE":
            school = School.SCSE;
            break;
        case "SSS":
            school = School.SSS;
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + lineSplit[2]);
        }

        return new Course(courseName, courseCode, school);
    }

    private Student parseStudentFromTxt(String line) {
        String[] lineSplit = line.split("\\|");
        String name = lineSplit[0];
        String userId = lineSplit[1];
        String matricNumber = lineSplit[2];
        String nationality = lineSplit[3];
        Gender gender = lineSplit[4].equals("MALE") ? Gender.MALE : Gender.FEMALE;
        String[] accessStartTimeSplit = lineSplit[5].split("-");
        String[] accessEndTimeStringSplit = lineSplit[6].split("-");
        LocalDateTime acccessStartTime = LocalDateTime.of(Integer.parseInt(accessStartTimeSplit[0]),
                Integer.parseInt(accessStartTimeSplit[1]), Integer.parseInt(accessStartTimeSplit[2]),
                Integer.parseInt(accessStartTimeSplit[3]), Integer.parseInt(accessStartTimeSplit[4]));
        LocalDateTime accessEndTime = LocalDateTime.of(Integer.parseInt(accessEndTimeStringSplit[0]),
                Integer.parseInt(accessEndTimeStringSplit[1]), Integer.parseInt(accessEndTimeStringSplit[2]),
                Integer.parseInt(accessEndTimeStringSplit[3]), Integer.parseInt(accessEndTimeStringSplit[4]));

        return new Student(name, userId, matricNumber, nationality, gender,
                new AccessTime(acccessStartTime, accessEndTime));
    }

    private void load() {
        try {
            FileInputStream fis = new FileInputStream("data/Storage.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            storage = (Storage) ois.readObject();
            ois.close();
            fis.close();
        } catch(FileNotFoundException e) {
            preload();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(InvalidClassException e) {
            preload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            FileOutputStream fos = new FileOutputStream("data/Storage.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storage);
            oos.close();
            fos.close();
            System.out.println("Serialization Done!!");
        } catch(IOException e) {
            System.out.println(e);
        }
    }

}
