package model;

import exception.CourseInWaitListException;
import exception.CourseRegisteredException;
import exception.ClashingRegisteredIndexNumberException;
import exception.MaxAuExceededException;
import exception.NoVacancyException;
import exception.InvalidNewMaxException;
import exception.ClashingWaitListedIndexNumberException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class IndexNumber implements Serializable {
    private int id;
    private Course course;
    private int maxVacancy;
    private ArrayList<Lesson> lessons;
    private ArrayList<Student> registeredStudents;
    private LinkedList<Student> waitListStudents;

    public IndexNumber(int id, Course course, ArrayList<Lesson> lessons, int maxVacancy) {
        this.id = id;
        this.course = course;
        this.lessons = lessons;
        this.maxVacancy = maxVacancy;
        registeredStudents = new ArrayList<>();
        waitListStudents = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public int getMaxVacancy() {
        return maxVacancy;
    }

    public int getNumberOfRegisteredStudents() {
        return registeredStudents.size();
    }

    public ArrayList<Student> getRegisteredStudents() {
        return registeredStudents;
    }

    public LinkedList<Student> getWaitListStudents() {
        return waitListStudents;
    }

    public void setMaxVacancy(int maxVacancy) throws InvalidNewMaxException {
        if (maxVacancy < getNumberOfRegisteredStudents()) {
            throw new InvalidNewMaxException();
        }
        this.maxVacancy = maxVacancy;
    }

    public int getAvailableVacancy() {
        return maxVacancy - getNumberOfRegisteredStudents();
    }

    public void registerStudent(Student student) {
        registeredStudents.add(student);
    }

    public void deregisterStudent(Student student) {
        registeredStudents.remove(student);
    }

    public void addStudentToWaitList(Student student) {
        waitListStudents.add(student);
    }

    public void removeStudentFromWaitList(Student student) {
        waitListStudents.remove(student);
    }

    public void registerNextStudentInWaitList()
            throws CourseRegisteredException, CourseInWaitListException, NoVacancyException,
            ClashingRegisteredIndexNumberException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        if (!waitListStudents.isEmpty()) {
            Student nextStudentInWaitList = waitListStudents.poll();
            nextStudentInWaitList.getWaitListCourseCodes().remove(course.getCourseCode());
            nextStudentInWaitList.getWaitListIndexNumbers().remove(course.getCourseCode());
            nextStudentInWaitList.registerForCourse(course.getCourseCode(), this);
        }
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public String getFullDescription() {
        String fullDescription = toString();
        for (Lesson lesson: lessons) {
            fullDescription += "\n\t\t" + lesson.getLessonType() + ", " + lesson.getDayOfWeek() + ", "
                    + lesson.getStartTime() + " to " + lesson.getEndTime();
        }
        return fullDescription + "\n";
    }

    @Override
    public String toString() {
        return "Index Number: " + id + ", "
                + "Current Vacancy: " + getAvailableVacancy() + " / " + maxVacancy ;
    }
}
