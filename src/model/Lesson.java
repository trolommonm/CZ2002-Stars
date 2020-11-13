package model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class Lesson implements Serializable {
    private LessonType lessonType;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public Lesson(LessonType lessonType, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.lessonType = lessonType;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public DayOfWeek DayOfWeek() {
        return dayOfWeek;
    }
}
