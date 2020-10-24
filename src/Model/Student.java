package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import Enum.Gender;
import Exception.CourseRegisteredException;
import Exception.ClashingIndexNumberException;
import Exception.NoVacancyException;
import Exception.CourseInWaitListException;

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

    public void addCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingIndexNumberException,
            NoVacancyException, CourseInWaitListException {
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

        if (!getClashingIndexNumbers(indexNumberToBeAdded).isEmpty()) {
            throw new ClashingIndexNumberException();
        }

        if (indexNumberToBeAdded.getAvailableVacancy() <= 0) {
            throw new NoVacancyException();
        }

        registeredCourseCodes.add(courseCodeToBeAdded);
        registeredIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.registerStudent(this);
    }

    public void addCourseToWaitList(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        waitListCourseCodes.add(courseCodeToBeAdded);
        waitListIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.addStudentToWaitList(this);
    }

    public void dropCourse(Course course, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingIndexNumberException,
            CourseRegisteredException, NoVacancyException {
        indexNumberToBeDropped.deregisterStudent(this);
        registeredCourseCodes.remove(course.getCourseCode());
        registeredIndexNumbers.remove(course.getCourseCode());

        indexNumberToBeDropped.registerNextStudentInWaitList();
    }

    public ArrayList<IndexNumber> getClashingIndexNumbers(IndexNumber indexNumberToBeAdded) {
        ArrayList<IndexNumber> clashingIndexNumbers = new ArrayList<>();
        for (IndexNumber indexNumber: getAllIndexNumbersRegistered()) {
            for (Lesson lesson: indexNumber.getLessons()) {
                for (Lesson lessonToBeAdded: indexNumberToBeAdded.getLessons()) {
                    if (lessonToBeAdded.getDayOfWeek() == lesson.getDayOfWeek()) {
                        if (lessonToBeAdded.getStartTime().isBefore(lesson.getEndTime())  &&
                                lesson.getStartTime().isBefore(lessonToBeAdded.getEndTime())) {
                            clashingIndexNumbers.add(indexNumber);
                        }
                    }
                }
            }
        }

        return clashingIndexNumbers;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Gender: " + gender + ", Nationality: " + nationality;
    }
}
