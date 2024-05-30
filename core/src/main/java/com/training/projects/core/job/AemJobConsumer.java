package com.training.projects.core.job;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * this is AemJobCreater Code.
 */
@Component(service = {JobConsumer.class}, property = {
        JobConsumer.PROPERTY_TOPICS + "=geeks/jobs"
})
public class AemJobConsumer implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(AemJobConsumer.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Override
    public JobResult process(final Job job) {
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = getDataResource();
            String path = (String) job.getProperty("path");

            logger.info("this is ok "
                    + resourceResolver.getResource(path).getName());

            return JobResult.OK;
        } catch (Exception e) {
            logger.info("some exception");
            return JobResult.FAILED;
        }
    }

    /**
     * this method used to get resolver.
     *
     * @return {ResourceResolver}
     * @throws LoginException
     */
    public ResourceResolver getDataResource() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "testuser");
        return resourceResolverFactory.getServiceResourceResolver(map);
    }

}
