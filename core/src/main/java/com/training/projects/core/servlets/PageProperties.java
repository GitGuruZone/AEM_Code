package com.training.projects.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.training.projects.core.commonutils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class, property =
        {
                "sling.servlet.methods=GET",
                "sling.servlet.paths=/apps/Page/Properties"
        })
public class PageProperties extends SlingSafeMethodsServlet {
    private String systemUser = "customuser";
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {

        try {
            ResourceResolver resolver = CommonUtils
                    .getResolver(resourceResolverFactory, systemUser);
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            Page page = pageManager.getPage(
                    "/content/training_projects/us/en/testing-page");
            Session session = resolver.adaptTo(Session.class);
            Node currentnode = session.getNode(
                    page.getPath() + "/jcr:content/root/container/container");
            Node parentNode = session.getNode(page.getParent().getPath()
                    + "/jcr:content/root/container/container");

            Iterator<Node> itr = parentNode.getNodes();
            while (itr.hasNext()) {
                Node nodes = itr.next();

                if (!currentnode.hasNode(nodes.getName())) {
                    String path = nodes.getPath();
                    Resource res = resolver.getResource(path);
                    Node newnode = currentnode.addNode(nodes.getName());
                    ValueMap valueMap = res.getValueMap();
                    String slingtype = valueMap.get(
                            "sling:resourceType", String.class);
                    newnode.setProperty("sling:resourceType", slingtype);
                }
            }
            session.save();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (PathNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
