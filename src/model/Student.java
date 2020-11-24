package model;

import java.io.Serializable;
import java.time.LocalDateTime;
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

/**
 * This class is responsible for modelling Student which details the attributes and methods of each
 * {@code Student} object part of the STARS Planner system.
 */
public class Student implements Serializable {

    /**
     * An integer variable declared as maxAU initialized with 21 representing the maximum number of AUs
     * a particular {@code Student} object can take in a semester
     */
    public static int maxAu = 21;

    /**
     * An String variable declared as name representing the name of the {@code Student} object
     */
    private String name;

    /**
     * An String variable declared as name representing the userId of the {@code Student} object
     */
    private String userId;

    /**
     * An String variable declared as matricNumber representing the matriculation number of the {@code Student} object
     */
    private String matricNumber;

    /**
     * An String variable declared as nationality representing the nationality of the {@code Student} object
     */
    private String nationality;

    /**
     * An String variable declared as emailAddress representing the email address of the {@code Student} object
     */
    private String emailAddress;

    /**
     * A {@code Gender} variable declared as gender representing the gender of the {@code Student} object
     * @see Gender
     */
    private Gender gender;

    /**
     * A {@code AccessTime} variable declared as accessTime representing the access timings of
     * the {@code Student} object
     * @see AccessTime
     */
    private AccessTime accessTime;

    /**
     * A {@code TimeTable} variable declared as timeTable representing the timetable of the {@code Student} object
     * @see TimeTable
     */
    private TimeTable timeTable;

    /**
     * The {@code INotification} interface declared as preferredNotification which enables email notification
     * functionalities
     * @see INotification
     */
    private INotification preferredNotification;

    /**
     * A constructor which constructs the {@code Student} object.
     * @param name The name of the {@code Student} object.
     * @param userId The userId of the {@code Student} object.
     * @param matricNumber The matricNumber of the {@code Student} object.
     * @param nationality The nationality of the {@code Student} object.
     * @param gender The {@code Gender} of the {@code Student} object.
     * @param accessTime The {@code AccessTime} of the {@code Student} object.
     * Constructor initializers a {@code TimeTable} for that particular {@code Student} object
     * as well as creates an instance of the {@code EmailNotification} to enable email notification
     * features for the {@code Student} object
     * @see Gender
     * @see AccessTime
     * @see TimeTable
     * @see EmailNotification
     */
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

    /**
     * @return The String representing the matric number of the {@code Student} object
     */
    public String getMatricNumber() {
        return matricNumber;
    }

    /**
     * @return The String representing the userId of the {@code Student} object
     */
    public String getUserId() {
        return  userId;
    }

    /**
     * @return The ArrayList of String representing the list of courses registered by the {@code Student} object
     * in terms of the respective course codes of the {@code Student} object
     */
    public ArrayList<String> getRegisteredCourseCodes() {
        return timeTable.getRegisteredCourseCodes();
    }

    /**
     * @return The ArrayList of String representing the list of courses on the waitlist from the {@code Student} object
     * in terms of the respective course codes of the {@code Student} object
     */
    public ArrayList<String> getWaitListCourseCodes() {
        return timeTable.getWaitListCourseCodes();
    }

    /**
     * @return The {@code TimeTable} representing the timetable of the {@code Student} object
     * @see TimeTable
     */
    protected TimeTable getTimeTable() {
        return timeTable;
    }

    /**
     * @return The String representing the name of the {@code Student} object
     */
    public String getName() {
        return name;
    }

    /**
     * @return The {@code AccessTime} representing the access time of the {@code Student} object
     */
    public AccessTime getAccessTime() {
        return accessTime;
    }

    /**
     *  Sets the {@code AccessTime} of of the {@code Student} object
     *  @param accessTime The {@code AccessTime} of the {@code Student} object to be set to.
     *  @see AccessTime
     */
    public void setAccessTime(AccessTime accessTime) {
        this.accessTime = accessTime;
    }

    /**
     *  Checks if the localDateTime in the parameter matches with the {@code Student} object's access time
     *  @param localDateTime The {@code LocalDateTime} to be compared with the Student's access time
     *  @return The boolean value representing whether or not the localDateTime parameter matches with the
     *  {@code Student} object's access time
     */
    public boolean isWithinAccessTime(LocalDateTime localDateTime) {
        return accessTime.isWithinAccessTime(localDateTime);
    }

    /**
     * @return HashMap representing the {@code TimeTable} in key-value pair, where the String represents the
     * running indices of the registered IndexNumbers while the {@code IndexNumber} represents the respective
     * registered IndexNumbers of the student's timetable
     * @see IndexNumber
     * @see TimeTable
     */
    public HashMap<String, IndexNumber> getRegisteredIndexNumbers() {
        return timeTable.getRegisteredIndexNumbers();
    }

    /**
     * @return HashMap representing the {@code TimeTable} in key-value pair, where the String represents the
     * running indices of the IndexNumbers on wait list while the {@code IndexNumber} represents the respective
     * IndexNumbers on wait list of the student's timetable
     * @see IndexNumber
     * @see TimeTable
     */
    public HashMap<String, IndexNumber> getWaitListIndexNumbers() {
        return timeTable.getWaitListIndexNumbers();
    }

    /**
     * @return The integer representing the total amount of AUs from the registered courses of the {@code Student}
     */
    public int getRegisteredAu() {
        return timeTable.getRegisteredAu();
    }

    /**
     * @return The integer representing the total amount of AUs from the courses on wait list of the {@code Student}
     */
    public int getWaitListAu() {
        return timeTable.getWaitListAu();
    }

    /**
     * @return The integer representing the total amount of AUs from the courses on wait list
     * and the registered courses of the {@code Student}
     */
    public int getTotalAuInRegisteredAndWaitList() {
        return timeTable.getTotalAuInRegisteredAndWaitList();
    }

    /**
     * Registers a course for the {@code Student} and updating the {@code TimeTable}
     * @param courseCodeToBeAdded The String representing the course code of the course to be added
     * @param indexNumberToBeAdded The {@code IndexNumber} of the course to be added
     * @throws CourseRegisteredException if course is already registered
     * @throws CourseInWaitListException if course is already in wait list
     * @throws NoVacancyException if there are no more vacancies in the course
     * @throws ClashingRegisteredIndexNumberException if the indexnumber to be registered clashes with any of
     * the already registered indexnumbers
     * @throws ClashingWaitListedIndexNumberException if the indexnumber to be wait listed clashes with any of
     * the already registered indexnumbers
     * @throws MaxAuExceededException if Student has reached the maximum amount of AUs
     * @see IndexNumber
     * @see TimeTable
     */
    public void registerForCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingRegisteredIndexNumberException,
            NoVacancyException, CourseInWaitListException, ClashingWaitListedIndexNumberException,
            MaxAuExceededException {
        timeTable.registerForCourse(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    /**
     * Swaps IndexNumber for a course with another {@code Student} and updating the {@code TimeTable}
     * @param courseCodeToBeSwapped The String representing the course code of the IndexNumber to be swapped with peer
     * @param peer The {@code Student} in which this instance of {@code Student} is swapping the IndexNumber with
     * @throws ClashingRegisteredIndexNumberException if the indexnumber to be registered clashes with any of
     * the already registered indexnumbers
     * @throws ClashingWaitListedIndexNumberException if the indexnumber to be wait listed clashes with any of
     * the already registered indexnumbers
     * @throws SameIndexNumberSwapException if the pair of indexnumbers are the same
     * @throws PeerClashingRegisteredIndexNumberException if the indexnumber to be swapped with peer clashes with the
     * registered courses on the peer's timetable
     * @throws PeerClashingWaitListedIndexNumberException if the indexnumber to be swapped with peer clashes with the
     * courses on waitlist of peer's timetable
     * @see Student
     * @see TimeTable
     */
    public void swapIndexNumberWithPeer(String courseCodeToBeSwapped, Student peer)
            throws ClashingRegisteredIndexNumberException, SameIndexNumberSwapException,
            ClashingWaitListedIndexNumberException, PeerClashingRegisteredIndexNumberException,
            PeerClashingWaitListedIndexNumberException {
        timeTable.swapIndexNumberWithPeer(courseCodeToBeSwapped, peer);
    }

    /**
     * Swaps IndexNumber that is in {@code Student} object's {@code TimeTable} and updating the {@code TimeTable}
     * @param courseCodeToBeSwapped The String representing the original course code of the IndexNumber
     * to be swapped with
     * @param newIndexNumber The new {@code IndexNumber} to be swapped with the original
     * @throws ClashingRegisteredIndexNumberException if the indexnumber to be registered clashes with any of
     * the already registered indexnumbers
     * @throws NoVacancyException if there are no more vacancies in the course
     * @throws SameIndexNumberSwapException if the pair of indexnumbers are the same
     * @see IndexNumber
     * @see TimeTable
     */
    public void swapIndexNumber(String courseCodeToBeSwapped, IndexNumber newIndexNumber)
            throws ClashingRegisteredIndexNumberException, NoVacancySwapException, SameIndexNumberSwapException, ClashingWaitListedIndexNumberException {
        timeTable.swapIndexNumber(courseCodeToBeSwapped, newIndexNumber);
    }

    /**
     * Adds a course into the wait list for the {@code Student} and updating the {@code TimeTable}
     * @param courseCodeToBeAdded The String representing the course code of the course to be added
     * @param indexNumberToBeAdded The {@code IndexNumber} of the course to be added
     * @see IndexNumber
     * @see TimeTable
     */
    public void addCourseToWaitList(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        timeTable.addCourseToWaitList(courseCodeToBeAdded, indexNumberToBeAdded);
    }

    /**
     * Drops a course into the wait list for the {@code Student} and updating the {@code TimeTable}
     * @param courseCodeToBeDropped The String representing the course code of the course to be dropped
     * @param indexNumberToBeDropped The {@code IndexNumber} of the course to be dropped
     * @see IndexNumber
     * @see TimeTable
     */
    public void dropCourseFromWaitList(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        timeTable.dropCourseFromWaitList(courseCodeToBeDropped, indexNumberToBeDropped);
    }

    /**
     * Drops a {@code Course} from a {@code Student} and registers the front most {@code Student} in the
     * waitlist for that particular {@code Course} IndexNumber and updating the {@code TimeTable}
     * @param course The {@code Course} to be dropped
     * @param indexNumberToBeDropped The {@code IndexNumber} of the course to be dropped
     * @throws CourseRegisteredException if course is already registered
     * @throws CourseInWaitListException if course is already in wait list
     * @throws NoVacancyException if there are no more vacancies in the course
     * @throws ClashingRegisteredIndexNumberException if the indexnumber to be registered clashes with any of
     * the already registered indexnumbers
     * @throws ClashingWaitListedIndexNumberException if the indexnumber to be wait listed clashes with any of
     * the already registered indexnumbers
     * @throws MaxAuExceededException if Student has reached the maximum amount of AUs
     * @see IndexNumber
     * @see Course
     * @see TimeTable
     */
    public void dropCourseAndRegisterNextStudentInWaitList(Course course, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        timeTable.dropCourseAndRegisterNextStudentInWaitList(course, indexNumberToBeDropped);
    }

    /**
     * Sends a String representing the notification to the email address of the {@code Student}
     * @param messageToSend The String which details the specifics of the notification
     */
    protected void notify(String messageToSend) {
        preferredNotification.send(emailAddress, "Dear " + name + ",\n\n" + messageToSend);
    }

    /**
     * Converts the {@code Student} name, gender, and nationality into a single String
     * @return The String representing the converted {@code Student} name, gender, and nationality
     */
    @Override
    public String toString() {
        return "Name: " + name + ", Gender: " + gender + ", Nationality: " + nationality;
    }
}
