package com.training.projects.core.job;

import com.adobe.granite.workflow.exec.InboxItem;
import com.training.projects.core.services.AemNotificationService;
import com.training.projects.core.services.EmailService;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, property =
        {
                JobConsumer.PROPERTY_TOPICS + "=customjob"
        })
public class PrecJobConsumer implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Reference
    private EmailService emailService;
    @Reference
    private AemNotificationService aemNotificationService;

    @Override
    public JobResult process(final Job job) {
        try {

            String path = (String) job.getProperty("path");
            String event = (String) job.getProperty("event");
            aemNotificationService.sendNotification("Page Detail",
                    "Page has been created", InboxItem.Priority.HIGH);

            emailService.sendEmail("aryankumarjha@gmail.com",
                    "merifakeid5@gmail.com", "Delete Node",
                    "Hello,Aryan how are you");

            logger.info("everything is ok!" + path);
            logger.info("what event is actually happening-" + event);

            return JobResult.OK;
        } catch (Exception e) {
            logger.info("Our is not work correctly:-");
            return JobResult.FAILED;
        }

    }

}
