package com.training.projects.core.services;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * this Service used for email service.
 */
@Component(service = EmailService.class, immediate = true)
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * this method send email.
     *
     * @param to
     * @param from
     * @param subject
     * @param text
     * @return {boolean}
     */
    public boolean sendEmail(final String to, final String from,
                             final String subject, final String text) {
        boolean flag = false;
        String smtpHostName = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHostName);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        String userName = "merifakeid5@gmail.com";
        String password = "";
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            flag = true;
            logger.info("Email is sent....");
        } catch (Exception e) {
            logger.info("Email is not sent.... ");
            e.printStackTrace();
        }
        return flag;
    }
}
