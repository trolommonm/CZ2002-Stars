package ErrorMessage;

public class ErrorMessage {
    public static final String ERROR_INPUT_CHOICE = "Invalid input choice, please input a valid input choice!";

    // Login
    public static final String MISSING_ADMIN_LOGIN_INFO = "AdminLogoInfo.txt is missing." +
            "Please provide a valid login information for the Admin users!";
    public static final String MISSING_STUDENT_LOGIN_INFO = "StudentLoginInfo.txt is missing." +
            "Please provide a valid login information for the Student users!";
    public static final String WRONG_LOGIN_INFO = "Wrong UserID and/or Password. Please try again!";
    public static final String WRONG_ACCESS_PERIOD = "You are not authorised to access STARS! Your access period is:";
    public static final String COURSE_ALREADY_REGISTERED = "You are already registered for the course!";
    public static final String CLASHING_INDEX_NUMBER = "The index number you have selected clashes with the " +
            "timetable of the courses you have selected! Please try again.";
    public static final String NO_VACANCY = "There is no vacancy for that index number! You will be placed on the " +
            "waitlist.";

    // Admin
    public static final String INVALID_SCHOOL = "Invalid school! Please try again.";
    public static final String INVALID_INDEX_NUMBER = "Invalid index number! Please try again.";
    public static final String INVALID_LESSON_TYPE = "Invalid lesson type! Please try again.";
    public static final String INVALID_TIME = "Invalid time! Please try again.";
    public static final String INVALID_DATE = "Invalid date! Please try again.";
    public static final String INVALID_DATE_TIME = "Invalid date and time! Please try again.";
    public static final String INVALID_DAY_OF_WEEK = "Invalid day of week! Please try again.";
    public static final String INVALID_MAX_VACANCY = "Invalid maximum vacancy! Please try again.";
    public static final String INVALID_NEW_MAX_VACANCY = "The new maximum vacancy for this index is less than"
            + "the number of registered students.\n" + "Please enter a new value.";
    public static final String INVALID_ACCESS_TIME = "Invalid Access Time! The start time is later than end time!";
    public static final String INVALID_GENDER_TYPE = "Invalid gender! Please try again!";
    public static final String USER_ID_EXISTS = "User id already exists! Please enter a new user id.";
}
