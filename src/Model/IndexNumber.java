package Model;

import ErrorMessage.ErrorMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexNumber implements Serializable {
    private int indexNumber;
    private int maxVacancy;
    private int numberOfRegisteredStudents;
    private ArrayList<Lesson> lessons;

    public IndexNumber(int indexNumber, ArrayList<Lesson> lessons, int maxVacancy) {
        this.indexNumber = indexNumber;
        this.lessons = lessons;
        this.maxVacancy = maxVacancy;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public int getMaxVacancy() {
        return maxVacancy;
    }

    public void setMaxVacancy(int maxVacancy) throws Exception {
        if (maxVacancy < numberOfRegisteredStudents) {
            throw new Exception(ErrorMessage.INVALID_MAX_VACANCY);
        }
        this.maxVacancy = maxVacancy;
    }
}
