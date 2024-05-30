package com.training.projects.core.services;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EmailSendService.class, immediate = true)
public class EmailSendService {
    @Reference
    private EmailService emailService;

    private final  Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * this method call to EmailService.
     * @param mailAddress
     * @param subject
     * @param text
     */
    public void sendEmailService(final String mailAddress,
                                 final String subject, final String text) {
        String to = mailAddress;
        String from = "merifakeid5@gmail.com";
        String subject1 = subject;
        String text1 = text;
        boolean b = emailService.sendEmail(to, from, subject1, text1);
        if (b) {

            logger.info("Email Sent.....");
        } else {
            logger.info("Email not sent.....");
        }
    }
}

