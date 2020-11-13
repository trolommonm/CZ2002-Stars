package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import exception.CourseRegisteredException;
import exception.ClashingRegisteredIndexNumberException;
import exception.NoVacancyException;
import exception.NoVacancySwapException;
import exception.CourseInWaitListException;
import exception.SameIndexNumberSwapException;
import exception.ClashingWaitListedIndexNumberException;
import exception.PeerClashingRegisteredIndexNumberException;
import exception.PeerClashingWaitListedIndexNumberException;

public class Student implements Serializable {
    private String name;
    private String userId;
    private String matricNumber;
    private String nationality;
    private String emailAddress;
    private Gender gender;
    private AccessTime accessTime;
    private TimeTable timeTable;
    private INotification preferredNotification;

    public Student(String name, String userId, String matricNumber,
                   String nationality, String emailAddress, Gender gender, AccessTime accessTime) {
        this.name = name;
        this.userId = userId;
        this.matricNumber = matricNumber;
        this.nationality = nationality;
        this.emailAddress = emailAddress;
        this.gender = gender;
        this.accessTime = accessTime;
        timeTable = new TimeTable(this);
        preferredNotification = EmailNotification.getInstance();
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

    protected void notify(String messageToSend) {
        preferredNotification.send(emailAddress, "Dear " + name + ",\n\n" + messageToSend);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Gender: " + gender + ", Nationality: " + nationality;
    }
}
