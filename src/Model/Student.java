package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import Enum.Gender;
import Exception.CourseRegisteredException;
import Exception.ClashingRegisteredIndexNumberException;
import Exception.NoVacancyException;
import Exception.NoVacancySwapException;
import Exception.CourseInWaitListException;
import Exception.SameIndexNumberSwapException;
import Exception.ClashingWaitListedIndexNumberException;
import Exception.PeerClashingRegisteredIndexNumberException;
import Exception.PeerClashingWaitListedIndexNumberException;

public class Student implements Serializable {
    private String name;
    private String userId;
    private String matricNumber;
    private String nationality;
    private Gender gender;
    private AccessTime accessTime;
    private TimeTable timeTable;

    public Student(String name, String userId, String matricNumber,
                   String nationality, Gender gender, AccessTime accessTime) {
        this.name = name;
        this.userId = userId;
        this.matricNumber = matricNumber;
        this.nationality = nationality;
        this.gender = gender;
        this.accessTime = accessTime;
        timeTable = new TimeTable(this);
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getUserId() {
        return  userId;
    }

    public ArrayList<String> getRegisteredCourseCodes() {
        return timeTable.getRegisteredCourseCodes();
    }

    public ArrayList<String> getWaitListCourseCodes() {
        return timeTable.getWaitListCourseCodes();
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public String getName() {
        return name;
    }

    public AccessTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(AccessTime accessTime) {
        this.accessTime = accessTime;
    }

    public boolean isWithinAccessTime(LocalDateTime localDateTime) {
        return accessTime.isWithinAccessTime(localDateTime);
    }

    public HashMap<String, IndexNumber> getRegisteredIndexNumbers() {
        return timeTable.getRegisteredIndexNumbers();
    }

    public HashMap<String, IndexNumber> getWaitListIndexNumbers() {
        return timeTable.getWaitListIndexNumbers();
    }

    public ArrayList<IndexNumber> getAllIndexNumbersRegistered() {
        return timeTable.getAllIndexNumbersRegistered();
    }

    public ArrayList<IndexNumber> getAllIndexNumbersWaitListed() {
        return timeTable.getAllIndexNumbersWaitListed();
    }

    public void registerForCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingRegisteredIndexNumberException,
            NoVacancyException, CourseInWaitListException, ClashingWaitListedIndexNumberException {
        timeTable.registerForCourse(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    public void swapIndexNumberWithPeer(String courseCodeToBeSwapped, Student peer)
            throws ClashingRegisteredIndexNumberException, SameIndexNumberSwapException,
            ClashingWaitListedIndexNumberException, PeerClashingRegisteredIndexNumberException,
            PeerClashingWaitListedIndexNumberException {
        timeTable.swapIndexNumberWithPeer(courseCodeToBeSwapped, peer);
    }

    public void swapIndexNumber(String courseCodeToBeSwapped, IndexNumber newIndexNumber)
            throws ClashingRegisteredIndexNumberException, NoVacancySwapException, SameIndexNumberSwapException {
        timeTable.swapIndexNumber(courseCodeToBeSwapped, newIndexNumber);
    }

    public void addCourseToWaitList(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        timeTable.addCourseToWaitList(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    public void dropCourseFromWaitList(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        timeTable.dropCourseFromWaitList(courseCodeToBeDropped, indexNumberToBeDropped);
    }

    public void dropCourseAndRegisterNextStudentInWaitList(Course course, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException {
        timeTable.dropCourseAndRegisterNextStudentInWaitList(course, indexNumberToBeDropped);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Gender: " + gender + ", Nationality: " + nationality;
    }
}
