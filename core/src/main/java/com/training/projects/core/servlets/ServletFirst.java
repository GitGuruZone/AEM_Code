package com.training.projects.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class, property =
        {
                "sling.servlet.paths=/bin/first",
                "sling.servlet.methods=GET"
        })
public class ServletFirst extends SlingSafeMethodsServlet {

    private final Logger logger
            = LoggerFactory.getLogger(getClass());

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {

        String field = request.getParameter("field");
        String area = request.getParameter("area");
        Map<String, Object> serviceUserMap = new HashMap<>();
        serviceUserMap.put(ResourceResolverFactory.SUBSERVICE, "testuser");

        try {
            ResourceResolver resolver = resourceResolverFactory
                    .getServiceResourceResolver(serviceUserMap);


            PageManager pageManager = resolver.adaptTo(PageManager.class);

            // Check if page manager is successfully obtained
            if (pageManager != null) {
                // Create a new page
                Page page = pageManager.create(
                        "/content/training_projects/us/en",
                        "newdata",
                        "/conf/training_projects"
                                + "/settings/wcm/templates/page-content",
                        "NEW");
                String path = page.getPath()
                        + "/jcr:content/root/container/title";
                String path1 = page.getPath()
                        + "/jcr:content/root/container/container/text";
                Session session = resolver.adaptTo(Session.class);
                Node node1 = session.getNode(path);
                node1.setProperty("jcr:title", field);
                Node node2 = session.getNode(path1);
                node2.setProperty("text", area);
                session.save();
                // Check if page creation was successful
                if (page != null) {
                    // Write response indicating success
                    response.getWriter().write(
                          "Page created successfully!" + field + "and" + area);
                } else {
                    // Write response indicating failure
                    response.getWriter().write(
                            "Failed to create page!");
                }
            } else {
                // Write response indicating failure
                response.getWriter().write(
                        "Failed to obtain PageManager!");
            }

        } catch (LoginException | WCMException | RepositoryException e) {
            // Log and handle exceptions
            logger.error("Error occurred ", e);
            response.getWriter().write(
                    "An error occurred" + e.getMessage());

        }
    }
}
