package filemanager;

import model.AccessTime;
import model.Course;
import model.IndexNumber;
import model.School;
import model.Student;

import java.util.ArrayList;

public interface IStorageManager {
    ArrayList<Course> getCoursesTakenByStudent(Student student);

    ArrayList<Course> getCoursesInWaitListByStudent(Student student);

    void addCourse(Course course);

    Course getCourse(String courseCode);

    void registerForCourse(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws Exception;

    void dropCourseAndRegisterNextStudentInWaitList(String userId, String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped)
            throws Exception;

    void swapIndexNumber(String userId, String courseCodeToBeSwapped, IndexNumber newIndexNumber)
            throws Exception;

    void swapIndexWithPeer(String userId, String peerUserId, String courseCodeToBeSwapped)
            throws Exception;

    void dropCourseFromWaitList(String userId, String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped);

    void addCourseToWaitList(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded);

    void setNewAccessTime(String userId, AccessTime newAccessTime);

    void setNewCourseCode(String newCourseCode, String forCourseCode);

    void setNewCourseName(String newCourseName, String forCourseCode);

    void setNewSchool(School newSchool, String forCourseCode);

    void setNewMaxVacancy(String courseCode, int index, int newMaxVacancy) throws Exception;

    ArrayList<Course> getAllCourses();

    void addStudent(Student student);

    void addIndexNumber(IndexNumber indexNumber, String courseCode);

    Student getStudent(String userId);

    ArrayList<Student> getAllStudents();
}
