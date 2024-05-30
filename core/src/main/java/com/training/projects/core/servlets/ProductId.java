package com.training.projects.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
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
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


@Component(service = Servlet.class, property =
        {
                "sling.servlet.methods=GET",
                "sling.servlet.paths=/apps/product/id"
        })
public class ProductId extends SlingSafeMethodsServlet {
    private String systemuser = "customuser";
    @Reference
    private QueryBuilder queryBuilder;
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    private String path = "/var/commerce/products/we-retail";
    private List<String> list = new ArrayList<>();

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        response.getWriter().write(id);


        try {
            ResourceResolver resourceResolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemuser);
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(getQuery(id)), session);
            SearchResult result = query.getResult();
            for (Iterator<Resource> it
                 = result.getResources(); it.hasNext();) {
                Resource res = it.next();
                ValueMap valueMap = res.getValueMap();
                list.add(valueMap.get("price", String.class));
            }
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write(new Gson().toJson(list));
    }

    /**
     * this method return map for QueryBuilder.
     * @param pid
     * @return {Map<String, String>}
     */
    public Map<String, String> getQuery(final String pid) {
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        map.put("type", "nt:unstructured");
        map.put("property", "productId");
        map.put("property.value", pid);
        map.put("p.limit", "-1");
        return map;
    }
}
