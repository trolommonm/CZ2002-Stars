package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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

    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
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
