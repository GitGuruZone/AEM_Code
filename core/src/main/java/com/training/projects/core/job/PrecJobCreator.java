package com.training.projects.core.job;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * this service used to create job.
 */
@Component(service = EventHandler.class, immediate = true, property = {
        EventConstants.EVENT_TOPIC
                + "=org/apache/sling/api/resource/Resource/REMOVED",
        EventConstants.EVENT_FILTER
                + "=(path=/content/training_projects/us/en/*)"
})
public class PrecJobCreator implements EventHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Reference
    private JobManager jobManager;

    @Override
    public void handleEvent(final Event event) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("path", event.getTopic());
            map.put("event", event.getProperty(SlingConstants.PROPERTY_PATH));
            jobManager.addJob("customjob", map);
            logger.info("job creater is ready :");
        } catch (Exception e) {
            logger.info("we got some error in try block handle it ....");
        }
    }
}
