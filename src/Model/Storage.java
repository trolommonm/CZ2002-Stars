package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Exception.CourseRegisteredException;
import Exception.ClashingIndexNumberException;
import Exception.NoVacancyException;

public class Storage implements Serializable {
    private HashMap<String, Student> students;
    private HashMap<String, Course> courses;

    public Storage() {
        students = new HashMap<>();
        courses = new HashMap<>();
    }

    public void addStudent(Student student) {
        students.put(student.getUserId(), student);
    }

    public void removeStudent(Student student) {
        students.remove(student.getUserId(), student);
    }

    public Student getStudent(String userId) {
        return students.get(userId);
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }

    public void removeCourse(Course course) {
        courses.remove(course.getCourseCode(), course);
    }

    public void addIndexNumber(IndexNumber indexNumber, String courseCode) {
        getCourse(courseCode).addIndexNumber(indexNumber);
    }

    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }

    public void setAccessTime(String userId, AccessTime accessTime) {
        getStudent(userId).setAccessTime(accessTime);
    }

    public void registerForCourse(String userId, String courseCodeToBeAdded, IndexNumber indexNumberToBeAdded)
        throws CourseRegisteredException, ClashingIndexNumberException, NoVacancyException {
        Student student = getStudent(userId);
        student.addCourse(getCourse(courseCodeToBeAdded), indexNumberToBeAdded);
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> coursesArrayList = new ArrayList<>();
        for (Course course: courses.values()) {
            coursesArrayList.add(course);
        }

        return coursesArrayList;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentsArrayList = new ArrayList<>();
        for (Student student: students.values()) {
            studentsArrayList.add(student);
        }

        return studentsArrayList;
    }
}
