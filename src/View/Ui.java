package View;

public class Ui {
    private final String dividerLine = "____________________________________________________________";

    public void print(String message) {
        System.out.println(message);
    }

    private void printDivider() {
        System.out.println(dividerLine);
    }

    public void printMessageWithDivider(String... messages) {
        printDivider();
        for (String message: messages) {
            print(message);
        }
        printDivider();
    }

}
