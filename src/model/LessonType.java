package model;

import exception.InvalidLessonTypeException;

public enum LessonType {
    LECTURE,
    LAB,
    TUTORIAL;

    public static String getAllLessonType() {
        return LECTURE.name() + ", " + LAB.name() + ", " + TUTORIAL.name();
    }

    public static LessonType getLessonType(String lessonTypeString) throws InvalidLessonTypeException {
        try {
            return LessonType.valueOf(lessonTypeString);
        } catch (IllegalArgumentException e) {
            throw new InvalidLessonTypeException();
        }
    }
}
