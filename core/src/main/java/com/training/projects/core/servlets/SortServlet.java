package com.training.projects.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.training.projects.core.commonutils.CommonUtils;
import com.training.projects.core.configuration.SortConfig;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


@Component(service = Servlet.class, property =
        {
                "sling.servlet.paths=/bin/sort/servlet",
                "sling.servlet.methods=GET"
        }
)
@Designate(ocd = SortConfig.class)
public class SortServlet extends SlingSafeMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private String path;
    private String sortType;

    /**
     * this activate mathod used for getting value.
     *
     * @param sortConfig
     */
    @Activate
    public void activate(final SortConfig sortConfig) {
        path = sortConfig.path();
        sortType = sortConfig.sort();
    }

    private String systemUser = "customuser";
    private List<String> list = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resolver = null;
        try {

            resolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemUser);
            Session session = resolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(getMap(path, sortType)), session);
            SearchResult result = query.getResult();
            for (Iterator<Resource> it = result.getResources();
                 it.hasNext();) {
                Resource resource = it.next();
                ValueMap valueMap = resource.getValueMap();
                list.add(valueMap.get("jcr:content/jcr:title", String.class));
            }
        } catch (Exception e) {
            logger.info("their is error in try block.......");
        }
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(list));
    }

    /**
     * it return map for queryBuilder.
     *
     * @param path
     * @param sortType
     * @return {Map<String, String>}
     */
    public Map<String, String> getMap(final String path,
                                      final String sortType) {

        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/jcr:title");
        if (sortType.equals("desc")) {
            map.put("orderby", "@jcr:content/jcr:title");
            map.put("orderby.sort", sortType);
        } else {
            map.put("orderby", "@jcr:content/jcr:title");
        }
        map.put("p.limit", "5");
        return map;
    }

}
