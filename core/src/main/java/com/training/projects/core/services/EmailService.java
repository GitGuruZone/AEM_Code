package com.training.projects.core.services;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Component(service=EmailService.class,immediate = true)

public class EmailService {


public boolean sendEmail(String to,String from,String subject,String text){
    boolean flag= false;
    String SMTP_HOST_NAME="smtp.gmail.com";
    Properties properties = new Properties();
    properties.put("mail.smtp.host", SMTP_HOST_NAME);
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

    String userName="@gmail.com";
    String password="";
    Session session=Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName,password);
        }
    });
    try{
        Message message= new MimeMessage(session);
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
        flag=true;
    }
    catch(Exception e){
e.printStackTrace();
    }
    return flag;
}
}
