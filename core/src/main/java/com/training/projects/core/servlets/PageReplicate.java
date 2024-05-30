package com.training.projects.core.servlets;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.training.projects.core.commonutils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class, property =
        {
                "sling.servlet.methods=GET",
                "sling.servlet.paths=/apps/page/replicate"
        })
public class PageReplicate extends SlingSafeMethodsServlet {
    private String systemUser = "customuser";
    @Reference
    private Replicator replicator;
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resourceResolver = null;
        Logger logger = LoggerFactory.getLogger(getClass());
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "testuser");
        try {
            resourceResolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemUser);
        } catch (Exception e) {
            logger.error("" + e);
        }
        try {
            PageManager pageManager
                    = resourceResolver.adaptTo(PageManager.class);
            Page newpage = pageManager.create(
                    "/content/training_projects/us/en",
                    "ReplicatePage",
                    "/conf/training_projects/settings/"
                            + "wcm/templates/page-content",
                    "newpage");
            if (newpage != null) {
                Session session = resourceResolver.adaptTo(Session.class);
                replicator.replicate(session, ReplicationActionType.ACTIVATE,
                        newpage.getPath());
                session.save();
            }
        } catch (Exception e) {
            logger.error("replicate page =>  " + e);
        }
    }

}
