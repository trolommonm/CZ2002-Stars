package Model;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotification implements INotification, Serializable {
    private static String username = "cz2002.ss3.group3@gmail.com";
    private static String password = "cz2002ss3group3";
    private static EmailNotification instance = new EmailNotification();

    public static void setEmailAccount(String username, String password) {
        EmailNotification.username = username;
        EmailNotification.password = password;
    }

    public static EmailNotification getInstance() {
        return instance;
    }

    private EmailNotification() { }

    protected Object readResolve() {
        return getInstance();
    }

    @Override
    public void send(String recipientEmail, String messageToSend) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("MyStars Notification");
            message.setText(messageToSend);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
