package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexNumber implements Serializable {
    private int indexNumber;
    private ArrayList<Lesson> lessons;

    public IndexNumber(int indexNumber, ArrayList<Lesson> lessons) {
        this.indexNumber = indexNumber;
        this.lessons = lessons;
    }
}
