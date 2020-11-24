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

/**
 * This class is responsible for modelling IndexNumber which details the attributes and methods of each IndexNumber
 * object from a particular Course object.
 */
public class IndexNumber implements Serializable {

    /**
     * An integer declared as id representing the IndexNumber's identification number.
     */
    private int id;

    /**
     * A {@code Course} object declared as course representing the course in which the IndexNumber belongs to.
     * @see Course
     */
    private Course course;

    /**
     * An integer declared as maxVacancy representing the maximum number of Students that can be enrolled
     * into this particular IndexNumber of a course.
     */
    private int maxVacancy;

    /**
     * An ArrayList of {@code Lesson} declared as lessons representing the list of lessons each IndexNumber has
     * @see Lesson
     */
    private ArrayList<Lesson> lessons;

    /**
     * An ArrayList of {@code Student} declared as registeredStudents representing the list of
     * students registered into this IndexNumber
     * @see Student
     */
    private ArrayList<Student> registeredStudents;

    /**
     * A LinkedList of {@code Student} declared as waitListStudents representing the list of
     * students on the wait list for this IndexNumber
     * @see Student
     */
    private LinkedList<Student> waitListStudents;

    /**
     * A constructor which constructs the IndexNumber object.
     * @param id The ID of the Lesson.
     * @param course The {@code Course} in which the IndexNumber belongs to.
     * @param lessons The {@code ArrayList<Lesson>} of the IndexNumber.
     * @param maxVacancy The maximum number of students that can be enrolled in this IndexNumber object.
     * Constructor initializes an ArrayList and LinkedList to store the {@code Student} enrolled and on wait list
     * @see Student
     * @see Lesson
     * @see Course
     */
    public IndexNumber(int id, Course course, ArrayList<Lesson> lessons, int maxVacancy) {
        this.id = id;
        this.course = course;
        this.lessons = lessons;
        this.maxVacancy = maxVacancy;
        registeredStudents = new ArrayList<>();
        waitListStudents = new LinkedList<>();
    }

    /**
     * @return The integer variable representing the ID of the IndexNumber object.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The {@code Course} representing the Course of the IndexNumber object.
     * @see Course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @return The integer variable representing the maximum vacancy of the IndexNumber object.
     */
    public int getMaxVacancy() {
        return maxVacancy;
    }

    /**
     * @return The integer variable representing the number of registered students in the IndexNumber object.
     */
    public int getNumberOfRegisteredStudents() {
        return registeredStudents.size();
    }

    /**
     * @return The ArrayList of {@code Student} representing the list of students registered in the IndexNumber object.
     * @see Student
     */
    public ArrayList<Student> getRegisteredStudents() {
        return registeredStudents;
    }

    /**
     * @return The LinkedList of {@code Student}  representing the list of students on the wait list
     * for the IndexNumber object.
     * @see Student
     */
    public LinkedList<Student> getWaitListStudents() {
        return waitListStudents;
    }

    /**
     * Sets the maximum vacancy of the IndexNumber
     * @param maxVacancy The maximum number of students that can be enrolled in this IndexNumber
     * @throws InvalidNewMaxException if the maximum vacancy to be set is less than the number of students
     * already registered for the particular indexnumber
     */
    public void setMaxVacancy(int maxVacancy) throws InvalidNewMaxException {
        if (maxVacancy < getNumberOfRegisteredStudents()) {
            throw new InvalidNewMaxException();
        }
        this.maxVacancy = maxVacancy;
    }

    /**
     * @return The available remainding vacancy of the IndexNumber
     */
    public int getAvailableVacancy() {
        return maxVacancy - getNumberOfRegisteredStudents();
    }

    /**
     * Adds a {@code Student} to the registeredStudents ArrayList of the IndexNumber
     * @see Student
     */
    public void registerStudent(Student student) {
        registeredStudents.add(student);
    }

    /**
     * Removes a {@code Student} from the registeredStudents ArrayList of the IndexNumber
     * @see Student
     */
    public void deregisterStudent(Student student) {
        registeredStudents.remove(student);
    }

    /**
     * Adds a {@code Student} to the waitListStudents LinkedList of the IndexNumber
     * @see Student
     */
    public void addStudentToWaitList(Student student) {
        waitListStudents.add(student);
    }

    /**
     * Removes a {@code Student} from the waitListStudents LinkedList of the IndexNumber
     * @see Student
     */
    public void removeStudentFromWaitList(Student student) {
        waitListStudents.remove(student);
    }

    /**
     * Removes the front most {@code Student} in the waitListStudents LinkedList of the IndexNumber and adds that
     * {@code Student} into the registeredStudents ArrayList of the IndexNumber
     * @throws CourseRegisteredException if course is already registered
     * @throws CourseInWaitListException if course is already in wait list
     * @throws NoVacancyException if there are no more vacancies in the course
     * @throws ClashingRegisteredIndexNumberException if the indexnumber to be registered clashes with any of
     * the already registered indexnumbers
     * @throws ClashingWaitListedIndexNumberException if the indexnumber to be wait listed clashes with any of
     * the already registered indexnumbers
     * @throws MaxAuExceededException if Student has reached the maximum amount of AUs
     * @see Student
     */
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

    /**
     * @return The ArrayList of {@code Lesson} representing the list of {@code Lesson} of the IndexNumber
     * @see Lesson
     */
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    /**
     * @return The String which details the type of {@code Lesson}, day of week in which {@code Lesson} occur,
     * start time of {@code Lesson} and end time of {@code Lesson} of every {@code Lesson} in the IndexNumber
     * @see Lesson
     */
    public String getFullDescription() {
        String fullDescription = toString();
        for (Lesson lesson: lessons) {
            fullDescription += "\n\t\t" + lesson.getLessonType() + ", " + lesson.getDayOfWeek() + ", "
                    + lesson.getStartTime() + " to " + lesson.getEndTime();
        }
        return fullDescription + "\n";
    }

    /**
     * @return The String which details the ID, current vacancy, and maximum vacancy of the IndexNumber
     */
    @Override
    public String toString() {
        return "Index Number: " + id + ", "
                + "Current Vacancy: " + getAvailableVacancy() + " / " + maxVacancy ;
    }
}
