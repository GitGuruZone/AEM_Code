package com.training.projects.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.*;

@Component(service = Servlet.class,immediate = true,property = {
        "sling.servlet.paths=/bin/auto/search",
        "sling.servlet.methods=GET"
}
)
public class AutoSearchServlet extends SlingSafeMethodsServlet {
    @Reference
    ResourceResolverFactory resourceResolverFactory;
    @Reference
    QueryBuilder queryBuilder;

    List<String> pathList= new ArrayList<>();
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resourceResolver=null;
        String path="/content/we-retail";
        String inputBoxData= StringUtils.EMPTY;
        try{
            resourceResolver=getResolver();
            inputBoxData = request.getParameter("query");
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(getdetail(inputBoxData,path)),session);
           SearchResult result=query.getResult();
            Iterator<Resource>itr=result.getResources();
            while(itr.hasNext()){
                Resource res= itr.next();
              pathList.add(res.getPath());
            }

        }
        catch(Exception e){

        }

        response.getWriter().write(new Gson().toJson(pathList));
        pathList= new ArrayList<>();
    }
    public ResourceResolver getResolver() throws LoginException {
        Map<String,Object>authMap= new HashMap<>();
        authMap.put(ResourceResolverFactory.SUBSERVICE,"customuser");
        return resourceResolverFactory.getServiceResourceResolver(authMap);
    }
    public Map<String,String> getdetail(String inputData,String path){
        Map<String,String>map=new HashMap<>();
        map.put("path",path);
        map.put("fulltext",inputData);
        map.put("p.limit","-1");
        return map;
    }
}
