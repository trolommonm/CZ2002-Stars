package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Exception.CourseRegisteredException;
import Exception.CourseInWaitListException;
import Exception.ClashingWaitListedIndexNumberException;
import Exception.ClashingRegisteredIndexNumberException;
import Exception.NoVacancyException;
import Exception.NoVacancySwapException;
import Exception.SameIndexNumberSwapException;
import Exception.PeerClashingWaitListedIndexNumberException;
import Exception.PeerClashingRegisteredIndexNumberException;


public class TimeTable implements Serializable {
    private Student student;
    private HashMap<String, IndexNumber> registeredIndexNumbers;
    private HashMap<String, IndexNumber> waitListIndexNumbers;

    public TimeTable(Student student) {
        this.student = student;
        registeredIndexNumbers = new HashMap<>();
        waitListIndexNumbers = new HashMap<>();
    }

    public ArrayList<String> getRegisteredCourseCodes() {
        return new ArrayList<>(registeredIndexNumbers.keySet());
    }

    public HashMap<String, IndexNumber> getRegisteredIndexNumbers() {
        return registeredIndexNumbers;
    }

    public ArrayList<String> getWaitListCourseCodes() {
        return new ArrayList<>(waitListIndexNumbers.keySet());
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
        registeredIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.registerStudent(student);
    }

    private void dropCourse(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        indexNumberToBeDropped.deregisterStudent(student);
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
        peer.getTimeTable().dropCourse(courseCodeToBeSwapped, peerIndexNumber);

        if (!getClashingRegisteredIndexNumbers(peerIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.getTimeTable().addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new ClashingRegisteredIndexNumberException();
        }

        if (!getClashingWaitListedIndexNumbers(peerIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.getTimeTable().addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new ClashingWaitListedIndexNumberException();
        }

        if (!peer.getTimeTable().getClashingRegisteredIndexNumbers(myIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.getTimeTable().addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new PeerClashingRegisteredIndexNumberException();
        }

        if (!peer.getTimeTable().getClashingWaitListedIndexNumbers(myIndexNumber).isEmpty()) {
            // register back myself
            addCourse(courseCodeToBeSwapped, myIndexNumber);
            // register back peer
            peer.getTimeTable().addCourse(courseCodeToBeSwapped, peerIndexNumber);
            throw new PeerClashingWaitListedIndexNumberException();
        }

        addCourse(courseCodeToBeSwapped, peerIndexNumber);
        peer.getTimeTable().addCourse(courseCodeToBeSwapped, myIndexNumber);
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
        waitListIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.addStudentToWaitList(student);
    }

    public void dropCourseFromWaitList(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        waitListIndexNumbers.remove(courseCodeToBeDropped);
        indexNumberToBeDropped.removeStudentFromWaitList(student);
    }

    public void dropCourseAndRegisterNextStudentInWaitList(Course course, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException {
        indexNumberToBeDropped.deregisterStudent(student);
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
}
