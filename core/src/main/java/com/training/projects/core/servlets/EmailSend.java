package com.training.projects.core.servlets;

import com.adobe.cq.social.notifications.api.NotificationManager;
import com.training.projects.core.services.EmailService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;

@Component(service = Servlet.class,immediate = true,property =
        {
                "sling.servlet.paths=/bin/new/servlet/send",
                "sling.servlet.methods=GET"
        })
public class EmailSend extends SlingSafeMethodsServlet {
    @Reference
    NotificationManager notificationManager;
    @Reference
    EmailService emailService;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String to="sahilkumaryogesh@gmail.com";
        String from="merifakeid5@gmail.com";
        String subject="Sending Email using aem";
        String text="Hi Leeshu, I wanted to send a quick note to thank you for all your support and guidance. ";
        boolean b=emailService.sendEmail(to,from,subject,text);
        if(b){

            response.getWriter().write("Email Sent.....");
        }
        else{
            response.getWriter().write("Email not sent.....");
        }
    }
}
