package Model;

import ErrorMessage.ErrorMessage;
import Exception.NoVacancyException;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexNumber implements Serializable {
    private int id;
    private int maxVacancy;
    private ArrayList<Lesson> lessons;
    private ArrayList<String> studentUserIds;

    public IndexNumber(int id, ArrayList<Lesson> lessons, int maxVacancy) {
        this.id = id;
        this.lessons = lessons;
        this.maxVacancy = maxVacancy;
        studentUserIds = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getMaxVacancy() {
        return maxVacancy;
    }

    public int getNumberOfRegisteredStudents() {
        return studentUserIds.size();
    }

    public ArrayList<String> getStudentUserIds() {
        return studentUserIds;
    }

    public void setMaxVacancy(int maxVacancy) throws Exception {
        if (maxVacancy < getNumberOfRegisteredStudents()) {
            throw new Exception(ErrorMessage.INVALID_NEW_MAX_VACANCY);
        }
        this.maxVacancy = maxVacancy;
    }

    public int getAvailableVacancy() {
        return maxVacancy - getNumberOfRegisteredStudents();
    }

    public void registerStudent(Student student) throws NoVacancyException {
        if (!(getAvailableVacancy() > 0)) {
            // add to waitlist?
            throw new NoVacancyException();
        }

        studentUserIds.add(student.getUserId());
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public String getFullDescription() {
        String fullDescription = toString();
        for (Lesson lesson: lessons) {
            fullDescription += "\n\t\t" + lesson.getLessonType() + ", " + lesson.getDayOfWeek() + ", "
                    + lesson.getStartTime() + " to " + lesson.getEndTime();
        }
        return fullDescription + "\n";
    }

    @Override
    public String toString() {
        return "Index Number: " + id + ", "
                + "Vacancy: " + getAvailableVacancy() + " / " + maxVacancy ;
    }
}
