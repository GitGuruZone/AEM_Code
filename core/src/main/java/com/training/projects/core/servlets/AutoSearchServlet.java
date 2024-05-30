package com.training.projects.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.training.projects.core.commonutils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
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
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Component(service = Servlet.class, immediate = true, property = {
        "sling.servlet.paths=/bin/auto/search",
        "sling.servlet.methods=GET"
}
)
public class AutoSearchServlet extends SlingSafeMethodsServlet {
    private String systemUser = "customsuer";
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Reference
    private QueryBuilder queryBuilder;


    private final Logger logger
            = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        List<String> pathList = new ArrayList<>();
        ResourceResolver resourceResolver = null;
        String path = "/content/we-retail";
        String inputBoxData = StringUtils.EMPTY;
        try {
            resourceResolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemUser);
            inputBoxData = request.getParameter("query");
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(
                    getdetail(inputBoxData, path)), session);
            SearchResult result = query.getResult();
            Iterator<Resource> itr = result.getResources();
            while (itr.hasNext()) {
                Resource res = itr.next();
                pathList.add(res.getPath());
            }
            logger.info("dataList is " + pathList);
        } catch (Exception e) {
            logger.info("their is some error in try block....");
        }

        response.getWriter().write(new Gson().toJson(pathList));

    }

    /**
     * this method return map.
     *
     * @param inputData to search.
     * @param path
     * @return {Map<String, String>}
     */
    public Map<String, String> getdetail(final String inputData,
                                         final String path) {
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        map.put("fulltext", inputData);
        map.put("p.limit", "-1");
        return map;
    }
}
