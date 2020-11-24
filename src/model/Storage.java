package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import exception.CourseRegisteredException;
import exception.ClashingRegisteredIndexNumberException;
import exception.MaxAuExceededException;
import exception.NoVacancyException;
import exception.NoVacancySwapException;
import exception.CourseInWaitListException;
import exception.SameIndexNumberSwapException;
import exception.ClashingWaitListedIndexNumberException;
import exception.PeerClashingRegisteredIndexNumberException;
import exception.PeerClashingWaitListedIndexNumberException;

public class Storage implements Serializable {
    private HashMap<String, Student> students;
    private HashMap<String, Course> courses;

    public Storage() {
        students = new HashMap<>();
        courses = new HashMap<>();
    }

    public void addStudent(Student student) {
        students.put(student.getUserId(), student);
    }

    public Student getStudent(String userId) {
        return students.get(userId);
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }

    public void removeCourse(Course course) {
        courses.remove(course.getCourseCode(), course);
    }

    public void addIndexNumber(IndexNumber indexNumber, String courseCode) {
        getCourse(courseCode).addIndexNumber(indexNumber);
    }

    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }

    public void setAccessTime(String userId, AccessTime accessTime) {
        getStudent(userId).setAccessTime(accessTime);
    }

    public void registerForCourse(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingRegisteredIndexNumberException, NoVacancyException,
            CourseInWaitListException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        Student student = getStudent(userId);
        student.registerForCourse(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    public void swapIndexNumber(String userId, String courseCodeToBeSwapped, IndexNumber newIndexNumber)
            throws ClashingRegisteredIndexNumberException, NoVacancySwapException, SameIndexNumberSwapException, ClashingWaitListedIndexNumberException {
        Student student = getStudent(userId);
        student.swapIndexNumber(courseCodeToBeSwapped, newIndexNumber);
    }

    public void swapIndexWithPeer(String userId, String peerUserId, String courseCodeToBeSwapped)
            throws PeerClashingWaitListedIndexNumberException, SameIndexNumberSwapException,
            ClashingWaitListedIndexNumberException, ClashingRegisteredIndexNumberException,
            PeerClashingRegisteredIndexNumberException {
        Student student = getStudent(userId);
        Student peer = getStudent(peerUserId);
        student.swapIndexNumberWithPeer(courseCodeToBeSwapped, peer);
    }

    public void dropCourseFromWaitList(String userId, String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        Student student = getStudent(userId);
        student.dropCourseFromWaitList(courseCodeToBeDropped, indexNumberToBeDropped);
    }

    public void dropCourseAndRegisterNextStudentInWaitList(String userId, String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        Student student = getStudent(userId);
        student.dropCourseAndRegisterNextStudentInWaitList(getCourse(courseCodeToBeDropped), indexNumberToBeDropped);
    }

    public void addCourseToWaitList(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        Student student = getStudent(userId);
        student.addCourseToWaitList(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> coursesArrayList = new ArrayList<>();
        for (Course course: courses.values()) {
            coursesArrayList.add(course);
        }

        return coursesArrayList;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentsArrayList = new ArrayList<>();
        for (Student student: students.values()) {
            studentsArrayList.add(student);
        }

        return studentsArrayList;
    }
}
