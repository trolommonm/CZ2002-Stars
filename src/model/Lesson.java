package model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * This class is responsible for modelling Lesson which details the attributes and methods of each Lesson object
 * from a particular Course object.
 */
public class Lesson implements Serializable {

    /**
     * A {@code LessonType} variable declared as lessonType representing the type of the Lesson object.
     * @see LessonType
     */
    private LessonType lessonType;

    /**
     * A DayOfWeek variable declared as dayOfWeek representing the day of week in which Lesson Object is held.
     */
    private DayOfWeek dayOfWeek;

    /**
     * A LocalTime variable declared as startTime representing the start time of the Lesson Object.
     */
    private LocalTime startTime;

    /**
     * A LocalTime variable declared as endTime representing the end time of the Lesson Object.
     */
    private LocalTime endTime;

    /**
     * A constructor which constructs the Lesson object using the following parameters.
     * @param lessonType The {@code LessonType} of the Lesson.
     * @param dayOfWeek The day of the week of the Lesson.
     * @param startTime The start time of the Lesson.
     * @param endTime The end time of the Lesson.
     * @see LessonType
     */
    public Lesson(LessonType lessonType, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.lessonType = lessonType;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * @return The LocalTime variable representing the start time of the Lesson from a particular course.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return The LocalTime variable representing the end time of the Lesson from a particular course.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @return The DayOfWeek variable representing the day of the week in which the Lesson from a
     * particular course is held.
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @return The {@code LessonType} variable representing the type of Lesson from a particular course.
     * @see LessonType
     */
    public LessonType getLessonType() {
        return lessonType;
    }
}
