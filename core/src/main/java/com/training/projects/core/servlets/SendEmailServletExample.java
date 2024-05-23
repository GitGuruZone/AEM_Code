package com.training.projects.core.servlets;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

@Component(
        service = { Servlet.class },
        property = {
                SLING_SERVLET_PATHS + "=/bin/send"
        }
)
public class SendEmailServletExample extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(SendEmailServletExample.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        boolean sent = false;

        try {
            sendEmail("This is a test email", "This is the email body", "sahilkumaryogesh@gmail.com");
            response.setStatus(SlingHttpServletResponse.SC_OK);
            sent = true;
        } catch (EmailException | AddressException e) {
            log.error("Error sending email", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        try {
            jsonResponse.put("result", sent ? "done" : "something went wrong");
        } catch (JSONException e) {
            log.error("Error creating JSON response", e);
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    protected void sendEmail(String subjectLine, String msgBody,
                             String recipients)
            throws EmailException, AddressException, IOException {
        HtmlEmail email = new HtmlEmail();
        List<InternetAddress> emailToList = new ArrayList<>();
        InternetAddress emailList = new InternetAddress(recipients);
        emailToList.add(emailList);
//        email.setCharset(UTF_8);
        email.setHtmlMsg(msgBody);
        email.setSubject(subjectLine);
        email.setTo(emailToList);

        log.info("Email properties: Host{} From{} Bcc{} Cc{} To{}", email.getHostName(),
                email.getFromAddress(),
                email.getBccAddresses(),
                email.getCcAddresses(),
                email.getToAddresses());
//        email.setFrom(contactUsConfigurationService.getFromEmail());
        MessageGateway<Email> messageGateway = messageGatewayService.getGateway(Email.class);
        if (messageGateway != null) {
            log.info("Retrieved Message Gateway Successfully and message gateway is {}", messageGateway);
            messageGateway.send(email);
//            CommonUtils.sendResponse(response, 200, "EmailSent Successfully", null);
        } else {
            log.error("The message gateway could not be retrieved.");
//            CommonUtils.sendResponse(response, 400, "Bad Request: Unable to send email " +
//                    "because of MessageGateway.", null);
//        }
        }
    }
}
