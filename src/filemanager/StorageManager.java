package filemanager;

import exception.CourseRegisteredException;
import exception.ClashingRegisteredIndexNumberException;
import exception.MaxAuExceededException;
import exception.NoVacancyException;
import exception.NoVacancySwapException;
import exception.CourseInWaitListException;
import exception.SameIndexNumberSwapException;
import exception.ClashingWaitListedIndexNumberException;
import exception.PeerClashingWaitListedIndexNumberException;
import exception.PeerClashingRegisteredIndexNumberException;
import model.AccessTime;
import model.Course;
import model.Gender;
import model.IndexNumber;
import model.Lesson;
import model.LessonType;
import model.LoginInfo;
import model.School;
import model.Storage;
import model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class StorageManager implements IStorageManager {
    private Storage storage;

    public StorageManager() {
        load();
    }

    @Override
    public ArrayList<Course> getCoursesTakenByStudent(Student student) {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseCode: student.getRegisteredCourseCodes()) {
            courses.add(storage.getCourse(courseCode));
        }

        return courses;
    }

    @Override
    public ArrayList<Course> getCoursesInWaitListByStudent(Student student) {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseCode: student.getWaitListCourseCodes()) {
            courses.add(storage.getCourse(courseCode));
        }

        return courses;
    }

    @Override
    public void addCourse(Course course) {
        storage.addCourse(course);
        save();
    }

    @Override
    public Course getCourse(String courseCode) {
        return storage.getCourse(courseCode);
    }

    @Override
    public void registerForCourse(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingRegisteredIndexNumberException, NoVacancyException,
            CourseInWaitListException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        storage.registerForCourse(userId, courseCodeToBeAdded, indexNumberToBeAdded);
        save();
    }

    @Override
    public void dropCourseAndRegisterNextStudentInWaitList(String userId, String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        storage.dropCourseAndRegisterNextStudentInWaitList(userId, courseCodeToBeDropped, indexNumberToBeDropped);
        save();
    }

    @Override
    public void swapIndexNumber(String userId, String courseCodeToBeSwapped, IndexNumber newIndexNumber)
            throws ClashingRegisteredIndexNumberException, NoVacancySwapException, SameIndexNumberSwapException {
        storage.swapIndexNumber(userId, courseCodeToBeSwapped, newIndexNumber);
        save();
    }

    @Override
    public void swapIndexWithPeer(String userId, String peerUserId, String courseCodeToBeSwapped)
            throws SameIndexNumberSwapException, ClashingWaitListedIndexNumberException,
            PeerClashingRegisteredIndexNumberException, ClashingRegisteredIndexNumberException,
            PeerClashingWaitListedIndexNumberException {
        storage.swapIndexWithPeer(userId, peerUserId, courseCodeToBeSwapped);
        save();
    }

    @Override
    public void dropCourseFromWaitList(String userId, String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        storage.dropCourseFromWaitList(userId, courseCodeToBeDropped, indexNumberToBeDropped);
        save();
    }

    @Override
    public void addCourseToWaitList(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        storage.addCourseToWaitList(userId, courseCodeToBeAdded, indexNumberToBeAdded);
        save();
    }

    @Override
    public void setNewAccessTime(String userId, AccessTime newAccessTime) {
        storage.setAccessTime(userId, newAccessTime);
        save();
    }

    @Override
    public void setNewCourseCode(String newCourseCode, String forCourseCode) {
        Course oldCourse = getCourse(forCourseCode);
        Course updatedCourse = new Course(oldCourse.getCourseName(), newCourseCode, oldCourse.getSchool(), oldCourse.getAu());
        updatedCourse.setIndexNumbers(oldCourse.getIndexNumbers());

        storage.removeCourse(oldCourse);
        storage.addCourse(updatedCourse);

        save();
    }

    @Override
    public void setNewCourseName(String newCourseName, String forCourseCode) {
        getCourse(forCourseCode).setCourseName(newCourseName);
        save();
    }

    @Override
    public void setNewSchool(School newSchool, String forCourseCode) {
        getCourse(forCourseCode).setSchool(newSchool);
        save();
    }

    @Override
    public void setNewMaxVacancy(String courseCode, int index, int newMaxVacancy) throws Exception {
        getCourse(courseCode).getIndexNumbers().get(index).setMaxVacancy(newMaxVacancy);
        save();
    }

    @Override
    public ArrayList<Course> getAllCourses() {
        return storage.getAllCourses();
    }

    @Override
    public void addStudent(Student student) {
        storage.addStudent(student);
        save();
    }

    @Override
    public void addIndexNumber(IndexNumber indexNumber, String courseCode) {
        storage.addIndexNumber(indexNumber, courseCode);
        save();
    }

    @Override
    public Student getStudent(String userId) {
        return storage.getStudent(userId);
    }

    @Override
    public ArrayList<Student> getAllStudents() {
        return storage.getAllStudents();
    }

    private void preload() {
        try {
            Files.copy(Path.of("data/preload/StudentLoginInfo.txt"), Path.of("data/StudentLoginInfo.txt")
                    , StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Path.of("data/preload/AdminLoginInfo.txt"), Path.of("data/AdminLoginInfo.txt")
                    , StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            if (file.getName().startsWith(".")) {
                continue;
            }
            try {
                Scanner sc = new Scanner(file);
                Course course = parseCourseFromTxt(sc.nextLine());
                ArrayList<IndexNumber> indexNumbers = new ArrayList<>();
                while (sc.hasNext()) {
                    indexNumbers.add(parseIndexNumberFromTxt(sc.nextLine(), course));
                }

                course.setIndexNumbers(indexNumbers);
                storage.addCourse(course);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        save();
    }

    private IndexNumber parseIndexNumberFromTxt(String line, Course course) {
        String[] lineSplit = line.split("\\|");
        int id = Integer.parseInt(lineSplit[0]);
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

        return new IndexNumber(id, course, lessons, maxVacancy);
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
        int au = Integer.parseInt(lineSplit[3]);

        return new Course(courseName, courseCode, school, au);
    }

    private Student parseStudentFromTxt(String line) {
        String[] lineSplit = line.split("\\|");
        String name = lineSplit[0];
        String userId = lineSplit[1];
        String matricNumber = lineSplit[2];
        String nationality = lineSplit[3];
        Gender gender = lineSplit[4].equals("MALE") ? Gender.MALE : Gender.FEMALE;
        String emailAddress = lineSplit[5];
        String[] accessStartTimeSplit = lineSplit[6].split("-");
        String[] accessEndTimeStringSplit = lineSplit[7].split("-");
        LocalDateTime acccessStartTime = LocalDateTime.of(Integer.parseInt(accessStartTimeSplit[0]),
                Integer.parseInt(accessStartTimeSplit[1]), Integer.parseInt(accessStartTimeSplit[2]),
                Integer.parseInt(accessStartTimeSplit[3]), Integer.parseInt(accessStartTimeSplit[4]));
        LocalDateTime accessEndTime = LocalDateTime.of(Integer.parseInt(accessEndTimeStringSplit[0]),
                Integer.parseInt(accessEndTimeStringSplit[1]), Integer.parseInt(accessEndTimeStringSplit[2]),
                Integer.parseInt(accessEndTimeStringSplit[3]), Integer.parseInt(accessEndTimeStringSplit[4]));

        return new Student(name, userId, matricNumber, nationality, emailAddress, gender,
                new AccessTime(acccessStartTime, accessEndTime));
    }

    private void load() {
        try {
            FileInputStream fis = new FileInputStream("data/Storage.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            storage = (Storage) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            preload();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidClassException e) {
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
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
