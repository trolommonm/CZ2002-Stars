package Model;

import ErrorMessage.ErrorMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexNumber implements Serializable {
    private int indexNumber;
    private int maxVacancy;
    private ArrayList<Lesson> lessons;
    private ArrayList<String> studentMatricNumbers;

    public IndexNumber(int indexNumber, ArrayList<Lesson> lessons, int maxVacancy) {
        this.indexNumber = indexNumber;
        this.lessons = lessons;
        this.maxVacancy = maxVacancy;
        studentMatricNumbers = new ArrayList<>();
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public int getMaxVacancy() {
        return maxVacancy;
    }

    public int getNumberOfRegisteredStudents() {
        return studentMatricNumbers.size();
    }

    public ArrayList<String> getStudentMatricNumbers() {
        return studentMatricNumbers;
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

    @Override
    public String toString() {
        return "Index Number: " + indexNumber + ", "
                + "Vacancy: " + getAvailableVacancy() + " / " + maxVacancy ;
    }
}
