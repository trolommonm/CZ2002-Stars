package Model;

import Enum.School;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {
    private String courseName;
    private String courseCode;
    private ArrayList<IndexNumber> indexNumbers;
    private int totalSize;
    private School school;
    private ArrayList<String> studentMatricNumbers;
    private Storage storage;

    public Course(String courseName, String courseCode, int totalSize, School school) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.totalSize = totalSize;
        this.school = school;
    }

    public ArrayList<IndexNumber> getIndexNumbers() {
        return indexNumbers;
    }

    public void setIndexNumbers(ArrayList<IndexNumber> indexNumbers) {
        this.indexNumbers = indexNumbers;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public ArrayList<String> getStudentMatricNumbers() {
        return studentMatricNumbers;
    }

    @Override
    public String toString() {
        return courseCode + " " + courseName;
    }
}
