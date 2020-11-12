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
    private ArrayList<String> registeredCourseCodes;
    private HashMap<String, IndexNumber> registeredIndexNumbers;
    private ArrayList<String> waitListCourseCodes;
    private HashMap<String, IndexNumber> waitListIndexNumbers;

    public Student(String name, String userId, String matricNumber,
                   String nationality, Gender gender, AccessTime accessTime) {
        this.name = name;
        this.userId = userId;
        this.matricNumber = matricNumber;
        this.nationality = nationality;
        this.gender = gender;
        this.accessTime = accessTime;
        registeredCourseCodes = new ArrayList<>();
        registeredIndexNumbers = new HashMap<>();
        waitListCourseCodes = new ArrayList<>();
        waitListIndexNumbers = new HashMap<>();
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getUserId() {
        return  userId;
    }

    public ArrayList<String> getRegisteredCourseCodes() {
        return registeredCourseCodes;
    }

    public ArrayList<String> getWaitListCourseCodes() {
        return waitListCourseCodes;
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
        return registeredIndexNumbers;
    }

    public HashMap<String, IndexNumber> getWaitListIndexNumbers() {
        return waitListIndexNumbers;
    }

    public ArrayList<IndexNumber> getAllIndexNumbersRegistered() {
        ArrayList<IndexNumber> indexNumbersRegistered = new ArrayList<>();
        for (IndexNumber indexNumber: registeredIndexNumbers.values()) {
            indexNumbersRegistered.add(indexNumber);
        }
        return indexNumbersRegistered;
    }

    public ArrayList<IndexNumber> getAllIndexNumbersWaitListed() {
        ArrayList<IndexNumber> indexNumbersWaitListed = new ArrayList<>();
        for (IndexNumber indexNumber: waitListIndexNumbers.values()) {
            indexNumbersWaitListed.add(indexNumber);
        }
        return indexNumbersWaitListed;
    }

    public void registerForCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingRegisteredIndexNumberException,
            NoVacancyException, CourseInWaitListException, ClashingWaitListedIndexNumberException {
        for (String courseCode: getRegisteredCourseCodes()) {
            if (courseCode.equals(courseCodeToBeAdded)) {
                throw new CourseRegisteredException();
            }
        }

        for (String courseCode: getWaitListCourseCodes()) {
            if (courseCode.equals(courseCodeToBeAdded)) {
                throw new CourseInWaitListException();
            }
        }

        if (!getClashingRegisteredIndexNumbers(indexNumberToBeAdded).isEmpty()) {
            throw new ClashingRegisteredIndexNumberException();
        }

        if (!getClashingWaitListedIndexNumbers(indexNumberToBeAdded).isEmpty()) {
            throw new ClashingWaitListedIndexNumberException();
        }

        if (indexNumberToBeAdded.getAvailableVacancy() <= 0) {
            throw new NoVacancyException();
        }

        addCourse(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    private void addCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        registeredCourseCodes.add(courseCodeToBeAdded);
        registeredIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.registerStudent(this);
    }

    private void dropCourse(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        registeredCourseCodes.remove(courseCodeToBeDropped);
        indexNumberToBeDropped.deregisterStudent(this);
        registeredIndexNumbers.remove(indexNumberToBeDropped);
    }

    public void swapIndexNumberWithPeer(String courseCodeToBeSwapped, Student peer)
            throws ClashingRegisteredIndexNumberException, SameIndexNumberSwapException,
            ClashingWaitListedIndexNumberException, PeerClashingRegisteredIndexNumberException,
            PeerClashingWaitListedIndexNumberException {

        IndexNumber myIndexNumber = registeredIndexNumbers.get(courseCodeToBeSwapped);
        IndexNumber peerIndexNumber = peer.getRegisteredIndexNumbers().get(courseCodeToBeSwapped);

        if (myIndexNumber == peerIndexNumber) {
            throw new SameIndexNumberSwapException();
        }

        // deregister myself
        dropCourse(courseCodeToBeSwapped, myIndexNumber);
        // deregister peer
        peer.dropCourse(courseCodeToBeSwapped, peerIndexNumber);

        if (!getClashingRegisteredIndexNumbers(peerIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new ClashingRegisteredIndexNumberException();
        }

        if (!getClashingWaitListedIndexNumbers(peerIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new ClashingWaitListedIndexNumberException();
        }

        if (!peer.getClashingRegisteredIndexNumbers(myIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new PeerClashingRegisteredIndexNumberException();
        }

        if (!peer.getClashingWaitListedIndexNumbers(myIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new PeerClashingWaitListedIndexNumberException();
        }

        addCourse(courseCodeToBeSwapped, peerIndexNumber);
        peer.addCourse(courseCodeToBeSwapped, myIndexNumber);
    }

    public void swapIndexNumber(String courseCodeToBeSwapped, IndexNumber newIndexNumber)
            throws ClashingRegisteredIndexNumberException, NoVacancySwapException, SameIndexNumberSwapException {
        IndexNumber indexNumberToBeSwapped = registeredIndexNumbers.get(courseCodeToBeSwapped);
        if (indexNumberToBeSwapped == newIndexNumber) {
            throw new SameIndexNumberSwapException();
        }

        dropCourse(courseCodeToBeSwapped, indexNumberToBeSwapped);

        if (!getClashingRegisteredIndexNumbers(newIndexNumber).isEmpty()) {
            addCourse(courseCodeToBeSwapped, indexNumberToBeSwapped);
            throw new ClashingRegisteredIndexNumberException();
        }

        if (newIndexNumber.getAvailableVacancy() <= 0) {
            addCourse(courseCodeToBeSwapped, indexNumberToBeSwapped);
            throw new NoVacancySwapException();
        }

        addCourse(newIndexNumber.getCourse().getCourseCode(), newIndexNumber);
    }

    public void addCourseToWaitList(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        waitListCourseCodes.add(courseCodeToBeAdded);
        waitListIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.addStudentToWaitList(this);
    }

    public void dropCourseFromWaitList(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        waitListCourseCodes.remove(courseCodeToBeDropped);
        waitListIndexNumbers.remove(courseCodeToBeDropped);
        indexNumberToBeDropped.removeStudentFromWaitList(this);
    }

    public void dropCourseAndRegisterNextStudentInWaitList(Course course, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException {
        indexNumberToBeDropped.deregisterStudent(this);
        registeredCourseCodes.remove(course.getCourseCode());
        registeredIndexNumbers.remove(course.getCourseCode());

        indexNumberToBeDropped.registerNextStudentInWaitList();
    }

    public ArrayList<IndexNumber> getClashingWaitListedIndexNumbers(IndexNumber indexNumberToBeAdded) {
        ArrayList<IndexNumber> clashingIndexNumbers = new ArrayList<>();
        for (IndexNumber indexNumber: getAllIndexNumbersWaitListed()) {
            if (checkIndexNumberClash(indexNumberToBeAdded, indexNumber)) {
                clashingIndexNumbers.add(indexNumber);
            }
        }

        return clashingIndexNumbers;
    }

    public ArrayList<IndexNumber> getClashingRegisteredIndexNumbers(IndexNumber indexNumberToBeAdded) {
        ArrayList<IndexNumber> clashingIndexNumbers = new ArrayList<>();
        for (IndexNumber indexNumber: getAllIndexNumbersRegistered()) {
            if (checkIndexNumberClash(indexNumberToBeAdded, indexNumber)) {
                clashingIndexNumbers.add(indexNumber);
            }
        }

        return clashingIndexNumbers;
    }

    private boolean checkIndexNumberClash(IndexNumber indexNumberToBeAdded, IndexNumber indexNumberToCheck) {
        for (Lesson lesson: indexNumberToCheck.getLessons()) {
            for (Lesson lessonToBeAdded: indexNumberToBeAdded.getLessons()) {
                if (lessonToBeAdded.getDayOfWeek() == lesson.getDayOfWeek()) {
                    if (lessonToBeAdded.getStartTime().isBefore(lesson.getEndTime())  &&
                            lesson.getStartTime().isBefore(lessonToBeAdded.getEndTime())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Gender: " + gender + ", Nationality: " + nationality;
    }
}
