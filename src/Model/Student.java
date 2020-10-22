package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import Enum.Gender;
import Exception.CourseRegisteredException;
import Exception.ClashingIndexNumberException;
import Exception.NoVacancyException;

public class Student implements Serializable {
    private String name;
    private String userId;
    private String matricNumber;
    private String nationality;
    private Gender gender;
    private AccessTime accessTime;
    private ArrayList<String> courseCodes;
    private HashMap<String, IndexNumber> registeredIndexNumbers;

    public Student(String name, String userId, String matricNumber,
                   String nationality, Gender gender, AccessTime accessTime) {
        this.name = name;
        this.userId = userId;
        this.matricNumber = matricNumber;
        this.nationality = nationality;
        this.gender = gender;
        this.accessTime = accessTime;
        courseCodes = new ArrayList<>();
        registeredIndexNumbers = new HashMap<>();
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getUserId() {
        return  userId;
    }

    public ArrayList<String> getCourseCodes() {
        return courseCodes;
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

    public ArrayList<IndexNumber> getAllIndexNumbersRegistered() {
        ArrayList<IndexNumber> indexNumbersRegistered = new ArrayList<>();
        for (IndexNumber indexNumber: registeredIndexNumbers.values()) {
            indexNumbersRegistered.add(indexNumber);
        }
        return indexNumbersRegistered;
    }

    public void addCourse(Course course, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingIndexNumberException, NoVacancyException {
        for (String courseCode: getCourseCodes()) {
            if (courseCode.equals(course.getCourseCode())) {
                throw new CourseRegisteredException();
            }
        }

        if (!getClashingIndexNumbers(indexNumberToBeAdded).isEmpty()) {
            throw new ClashingIndexNumberException();
        }

        try {
            indexNumberToBeAdded.registerStudent(this);
        } catch (NoVacancyException e) {
            throw e;
        }

        courseCodes.add(course.getCourseCode());
        registeredIndexNumbers.put(course.getCourseCode(), indexNumberToBeAdded);
    }

    public void dropCourse(Course course, IndexNumber indexNumberToBeDropped) {
        indexNumberToBeDropped.deregisterStudent(this);
        courseCodes.remove(course.getCourseCode());
        registeredIndexNumbers.remove(course.getCourseCode());
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
