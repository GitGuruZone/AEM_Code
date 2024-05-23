package com.training.projects.core.models;

//import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.mcm.emailprovider.EmailService;
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

@Component(service = {JobConsumer.class},property = {
        JobConsumer.PROPERTY_TOPICS+"=geeks/jobs"
})
public class AemJobConsumer implements JobConsumer{
    Logger logger= LoggerFactory.getLogger(AemJobConsumer.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;
@Reference
    MessageGatewayService messageGatewayService;

    @Override
    public JobResult process(Job job) {
        ResourceResolver resourceResolver=null;
        try{
            resourceResolver=getDataResource();
           String path= (String) job.getProperty("path");
           String event= (String) job.getProperty("event");
//           String heropage= (String) job.getProperty("heropage");
            logger.info("this is ok "+resourceResolver.getResource(path).getName());

            return JobResult.OK;
        }
        catch(Exception e){
logger.info("some exception");
return JobResult.FAILED;
        }
    }
    public ResourceResolver getDataResource() throws LoginException {
        Map<String,Object> map= new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"testuser");
        return resourceResolverFactory.getServiceResourceResolver(map);
    }

}
