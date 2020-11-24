package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import errormessage.ErrorMessage;
import exception.CourseRegisteredException;
import exception.CourseInWaitListException;
import exception.ClashingWaitListedIndexNumberException;
import exception.ClashingRegisteredIndexNumberException;
import exception.MaxAuExceededException;
import exception.NoVacancyException;
import exception.NoVacancySwapException;
import exception.SameIndexNumberSwapException;
import exception.PeerClashingWaitListedIndexNumberException;
import exception.PeerClashingRegisteredIndexNumberException;
import view.LoginUi;

/**
 * This class is responsible for modelling TimeTable which details the attributes and methods of each TimeTable object
 * from a particular user logging into the STARS Planner.
 */

public class TimeTable implements Serializable {

    /**
     * A Student object represents each student user.
     * @see Student
     */
    private Student student;

    /**
     * A  hashmap that contains registered IndexNumbers in Strings datatype for the different Courses available
     * @see IndexNumber
     */
    private HashMap<String, IndexNumber> registeredIndexNumbers;

    /**
     * A hashmap that contains the waitlisted IndexNumbers stored in String data type.
     * @see IndexNumber
     */
    private HashMap<String, IndexNumber> waitListIndexNumbers;

    /**
     * A constructor which constructs the Timetable object.
     * @param student The student of the user logging in.
     * @see Student
     */
    public TimeTable(Student student) {
        this.student = student;
        registeredIndexNumbers = new HashMap<>();
        waitListIndexNumbers = new HashMap<>();
    }

    /**
     *Displays the list of Registered Course Codes
     * @return an array list that contains the registered Course Codes
     */
    public ArrayList<String> getRegisteredCourseCodes() {
        return new ArrayList<>(registeredIndexNumbers.keySet());
    }

    /**
     * Displays the Registered Index Numbers
     * @return a HashMap object contain the all the registered index numbers in String format
     * @see IndexNumber
     */
    public HashMap<String, IndexNumber> getRegisteredIndexNumbers() {
        return registeredIndexNumbers;
    }

    /**
     * Displays the Course codes that have been waitlisted
     * @return an array list of the Course codes that have been waitlisted
     */
    public ArrayList<String> getWaitListCourseCodes() {
        return new ArrayList<>(waitListIndexNumbers.keySet());
    }

    /**
     * Displays the Index Numbers that have been waitlisted
     * @return a hashmap objects that contains the Index Numbers that have been wait listed
     */
    public HashMap<String, IndexNumber> getWaitListIndexNumbers() {
        return waitListIndexNumbers;
    }

    /**
     * Counts the amount of Academic Units that a particular student have been registered for using a loop
     * @return the total AU that have been registered by the Student
     */
    public int getRegisteredAu() {
        int registeredAu = 0;
        for (IndexNumber indexNumber: registeredIndexNumbers.values()) {
            registeredAu += indexNumber.getCourse().getAu();
        }
        return registeredAu;
    }

    /**
     * Counts the amount of Academic Units that a particular student have been waitlisted for using a loop
     * @return the total AU that have been waitlisted by the Student
     */
    public int getWaitListAu() {
        int waitListAu = 0;
        for (IndexNumber indexNumber: waitListIndexNumbers.values()) {
            waitListAu += indexNumber.getCourse().getAu();
        }
        return waitListAu;
    }

    /**
     * Counts the sum of the amount of registered AUs and waitlisted AUs
     * @return the count of the total AUs that have been registed and waitlisted
     */
    public int getTotalAuInRegisteredAndWaitList() {
        return getRegisteredAu() + getWaitListAu();
    }

    /**
     * Displays the lists of all the Index Numbers of the Courses that have been successfully registered
     * @return an array list that contains the index numbers that have been registered.
     */
    public ArrayList<IndexNumber> getAllIndexNumbersRegistered() {
        ArrayList<IndexNumber> indexNumbersRegistered = new ArrayList<>();
        for (IndexNumber indexNumber: registeredIndexNumbers.values()) {
            indexNumbersRegistered.add(indexNumber);
        }
        return indexNumbersRegistered;
    }

    /**
     * Displays the lists of all the Index Numbers of the Courses that have been successfully wait listed
     * @return an array list that contains the index numbers that have been waitlisted.
     */
    public ArrayList<IndexNumber> getAllIndexNumbersWaitListed() {
        ArrayList<IndexNumber> indexNumbersWaitListed = new ArrayList<>();
        for (IndexNumber indexNumber: waitListIndexNumbers.values()) {
            indexNumbersWaitListed.add(indexNumber);
        }
        return indexNumbersWaitListed;
    }

    /**
     * A method that registers the student for a specific course and index numbers if the requirements are fulfilled by calling the addCourse method
     * Displays that they have successfully registered from the course
     * (i.e Au registered has not reached it max, no clashing of timeslots etc)
     * @param courseCodeToBeAdded User selected Course Code to add
     * @param indexNumberToBeAdded User selected Index number to add for that particular course
     * @see IndexNumber
     * @throws CourseRegisteredException throws exception if  the course selected is already registered
     * @throws ClashingRegisteredIndexNumberException throws exception if the index number that has been selected, clashes with the current courses that have been registered already
     * @throws NoVacancyException throws exception if the index of the course selected does not have vacancy anymore
     * @throws CourseInWaitListException throws exception if the course slected is already added to the waitlist
     * @throws ClashingWaitListedIndexNumberException throws exception if the index number of the course that have been selected clashes with the timetable of the courses that have already been waitlisted for
     * @throws MaxAuExceededException throws exception when user is still trying to add course when he/she already has reached the max Au allowable
     */
    public void registerForCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
            throws CourseRegisteredException, ClashingRegisteredIndexNumberException,
            NoVacancyException, CourseInWaitListException, ClashingWaitListedIndexNumberException,
            MaxAuExceededException {
        for (String courseCode: getRegisteredCourseCodes()) {
            if (courseCode.equals(courseCodeToBeAdded)) {
                throw new CourseRegisteredException();
            }
        }

        if (getTotalAuInRegisteredAndWaitList() + indexNumberToBeAdded.getCourse().getAu() > Student.maxAu) {
            throw new MaxAuExceededException(ErrorMessage.MAX_AU_EXCEEDED);
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

        String messageToSend = "You have successfully registered for the course:\n\n"
                + indexNumberToBeAdded.getCourse().toString() + "\n\n" + indexNumberToBeAdded.getFullDescription();
        student.notify(messageToSend);
    }

    /**
     * Method that adds the selected course for the student
     * @param courseCodeToBeAdded User selected Course Code to add
     * @param indexNumberToBeAdded User selected Index number to add for that particular course
     */
    private void addCourse(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        registeredIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.registerStudent(student);
    }

    /**
     * Method that helps the user drop the course that has been registered
     * @param courseCodeToBeDropped User selected course to drop
     * @param indexNumberToBeDropped User selected Index number to drop for that particular course
     */
    private void dropCourse(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        indexNumberToBeDropped.deregisterStudent(student);
        registeredIndexNumbers.remove(courseCodeToBeDropped);
    }

    /**
     *This method allows the swapping of index with a peer when all conditions are met
     * @param courseCodeToBeSwapped the Student/current user index number to be swapped with peer
     * @param peer is a Student object that the current user will be swapping index with
     * @throws ClashingRegisteredIndexNumberException throws exception if the index number that has been selected, clashes with the current courses that have been registered already
     * @throws SameIndexNumberSwapException throws exception if the student have selected the same index number that they are currently registered for
     * @throws ClashingWaitListedIndexNumberException throws exception if the index number Student have selected clashes with the timetable of the courses that he/she is waitlisted for
     * @throws PeerClashingRegisteredIndexNumberException throws exception if the index of the course to be swapped will classh with current timetable
     * @throws PeerClashingWaitListedIndexNumberException throws exception if the Student's peer has a wait listed course that clashes with User's current indexes.
    */
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

        String messageToSend = "You have successfully swapped index with your peer, " + peer.getName() + ", for the course:\n\n"
                + myIndexNumber.getCourse().toString() + "\n\n"
                + "Your old index number was: " + myIndexNumber.getFullDescription() + "\n\n"
                + "You are now registered for index number: " + peerIndexNumber.getFullDescription();
        student.notify(messageToSend);
        String peerMessageToSend = "You have successfully swapped index with your peer, " + student.getName() + ", for the course:\n\n"
                + myIndexNumber.getCourse().toString() + "\n\n"
                + "Your old index number was: " + peerIndexNumber.getFullDescription() + "\n\n"
                + "You are now registered for index number: " + myIndexNumber.getFullDescription();
        peer.notify(peerMessageToSend);
    }

    /**
     * A method that allows swapping of index numbers when a vacant index number has appeared
     * @param courseCodeToBeSwapped the Student/current user index number to be swapped with peer
     * @param newIndexNumber the new index number that has a vacant seat allowing the user to swap
     * @throws ClashingRegisteredIndexNumberException throws exception if the index number that has been selected, clashes with the current courses that have been registered already
     * @throws NoVacancySwapException throws exception when the student tries to swap to an index that has no vacancy
     * @throws SameIndexNumberSwapException throws exception when the Student selects an index number that is the same as his current one
     */

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

        String messageToSend = "You have successfully swapped index for the course:\n\n"
                + indexNumberToBeSwapped.getCourse().toString() + "\n\n"
                + "Your old index number was:" + indexNumberToBeSwapped.getFullDescription() + "\n\n"
                + "You are now registered for index number:" + newIndexNumber.getFullDescription();
        student.notify(messageToSend);
    }

    /**
     * Method that waitlists the selected course for the student
     * @param courseCodeToBeAdded User selected Course Code to add
     * @param indexNumberToBeAdded User selected Index number to add for that particular course
     */
    public void addCourseToWaitList(String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded) {
        waitListIndexNumbers.put(courseCodeToBeAdded, indexNumberToBeAdded);
        indexNumberToBeAdded.addStudentToWaitList(student);

        String messageToSend = "You have added to the wait list for the course:\n\n"
                + indexNumberToBeAdded.getCourse().toString() + "\n\n"
                + indexNumberToBeAdded.getFullDescription();
        student.notify(messageToSend);
    }

    /**
     * Method that drops the selected waitlist course for the student
     * @param courseCodeToBeDropped User selected Course Code to drop
     * @param indexNumberToBeDropped User selected Index number to drop for that particular course
     */
    public void dropCourseFromWaitList(String courseCodeToBeDropped, IndexNumber indexNumberToBeDropped) {
        waitListIndexNumbers.remove(courseCodeToBeDropped);
        indexNumberToBeDropped.removeStudentFromWaitList(student);

        String messageToSend = "You have dropped from the wait list for the course:\n\n"
                + indexNumberToBeDropped.getCourse().toString() + "\n\n"
                + indexNumberToBeDropped.getFullDescription();
        student.notify(messageToSend);
    }

    /**
     * A method that drops the Student's current course and register the next Student that is in the waitlist
     * @param course course object
     * @param indexNumberToBeDropped index number to be dropped by the current student
     * @throws CourseInWaitListException throws exception when user have already added this course to the waitlist
     * @throws ClashingRegisteredIndexNumberException throws exception when the index number User have selected clashes with the timetable of the courses that they are currently registered for
     * @throws CourseRegisteredException throws exception when student already registed for the current course
     * @throws NoVacancyException throws exception when student tries to add an index number that already has no vacancy
     * @throws ClashingWaitListedIndexNumberException throws exception when the index number that student have selected classes with the timetable of courses that they have been waitlisted for
     * @throws MaxAuExceededException throws exception when maxAu is reached
     */
    public void dropCourseAndRegisterNextStudentInWaitList(Course course, IndexNumber indexNumberToBeDropped)
            throws CourseInWaitListException, ClashingRegisteredIndexNumberException,
            CourseRegisteredException, NoVacancyException, ClashingWaitListedIndexNumberException, MaxAuExceededException {
        indexNumberToBeDropped.deregisterStudent(student);
        registeredIndexNumbers.remove(course.getCourseCode());

        indexNumberToBeDropped.registerNextStudentInWaitList();

        String messageToSend = "You have successfully dropped from the course:\n\n"
                + indexNumberToBeDropped.getCourse().toString() + "\n\n"
                + indexNumberToBeDropped.getFullDescription();
        student.notify(messageToSend);
    }

    /**
     * Method that returns the wait listed index numbers that are clashing with the index number that the student wants to add
     * @param indexNumberToBeAdded index number to be added by the current student
     * @return wait listed index number that is clashing
     */
    public ArrayList<IndexNumber> getClashingWaitListedIndexNumbers(IndexNumber indexNumberToBeAdded) {
        ArrayList<IndexNumber> clashingIndexNumbers = new ArrayList<>();
        for (IndexNumber indexNumber: getAllIndexNumbersWaitListed()) {
            if (checkIndexNumberClash(indexNumberToBeAdded, indexNumber)) {
                clashingIndexNumbers.add(indexNumber);
            }
        }

        return clashingIndexNumbers;
    }

    /**
     * Method that returns the registered index numbers that are clashing with the index number that the student wants to add
     * @param indexNumberToBeAdded index number to be added by the current student
     * @return registered index numbers that are clashing
     */

    public ArrayList<IndexNumber> getClashingRegisteredIndexNumbers(IndexNumber indexNumberToBeAdded) {
        ArrayList<IndexNumber> clashingIndexNumbers = new ArrayList<>();
        for (IndexNumber indexNumber: getAllIndexNumbersRegistered()) {
            if (checkIndexNumberClash(indexNumberToBeAdded, indexNumber)) {
                clashingIndexNumbers.add(indexNumber);
            }
        }

        return clashingIndexNumbers;
    }

    /**
     * Method that checks if the index number that the student wants to add clashes the the current timetable
     * @param indexNumberToBeAdded index number to be added by the current student
     * @param indexNumberToCheck index number for the different courses that have been registered by the student
     * @return a boolean true if there are clashes and false if there are no clashes with the timetable
     */

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
