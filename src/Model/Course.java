package Model;

import Enum.School;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable, Cloneable {
    private String courseName;
    private String courseCode;
    private ArrayList<IndexNumber> indexNumbers;
    private School school;
    private Storage storage;

    public Course(String courseName, String courseCode, School school) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.school = school;
    }

    public ArrayList<IndexNumber> getIndexNumbers() {
        return indexNumbers;
    }

    public void setIndexNumbers(ArrayList<IndexNumber> indexNumbers) {
        this.indexNumbers = indexNumbers;
    }

    public void addIndexNumber(IndexNumber indexNumber) {
        indexNumbers.add(indexNumber);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return courseCode + " " + courseName;
    }

    @Override
    public Course clone() throws CloneNotSupportedException {
        return (Course) super.clone();
    }
}
