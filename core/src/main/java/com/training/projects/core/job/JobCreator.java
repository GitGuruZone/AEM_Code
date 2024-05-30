package com.training.projects.core.job;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC
                        + "=org/apache/sling/api/resource/Resource/ADDED",
                EventConstants.EVENT_FILTER
                        + "=(path=/content/training_projects/us/en/*)"
        }
)
public class JobCreator implements EventHandler {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(JobCreator.class);

    @Reference
    private JobManager jobManager;

    @Override
    public void handleEvent(final Event event) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("event", event.getTopic());
            map.put("path", event.getProperty(SlingConstants.PROPERTY_PATH));
            jobManager.addJob("geeks/jobs", map);
            LOGGER.info(map.values().toString());
        } catch (Exception e) {
            LOGGER.error("Error handling event", e);
        }
    }
}
