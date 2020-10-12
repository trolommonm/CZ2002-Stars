package Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AccessTime implements Serializable {

    private LocalDateTime start;
    private LocalDateTime end;

    public AccessTime(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public boolean isWithinAccessTime(LocalDateTime localDateTime) {
        if (localDateTime.isAfter(start) && localDateTime.isBefore(end)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return start.toString() + " to " + end.toString();
    }
}
