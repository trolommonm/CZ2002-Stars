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

    // Admin
    public static final String INVALID_TOTAL_SIZE = "Invalid total size! Please try again.";
    public static final String INVALID_SCHOOL = "Invalid school! Please try again.";
    public static final String INVALID_INDEX_NUMBER = "Invalid index number! Please try again.";
    public static final String INVALID_LESSON_TYPE = "Invalid lesson type! Please try again.";
    public static final String INVALID_TIME = "Invalid time! Please try again.";
    public static final String INVALID_DAY_OF_WEEK = "Invalid day of week! Please try again.";
}
