package com.training.projects.core.job;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sample code for job craetion.
 */
@Component(service = JobConsumer.class, immediate = true, property = {
        JobConsumer.PROPERTY_TOPICS + "=customnewjob"
})
public class CustomJobConsumer implements JobConsumer {
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * here we override process method.
     * @param job
     * @return {JobResult}
     */

    @Override
    public JobResult process(final Job job) {
        try {
            String path = (String) job.getProperty("path");
            String event = (String) job.getProperty("event");
            log.info(path);
            log.info(event);
            return JobResult.OK;
        } catch (Exception e) {
            return JobResult.FAILED;
        }
    }
}
