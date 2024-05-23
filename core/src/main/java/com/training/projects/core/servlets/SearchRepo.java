package com.training.projects.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

@Component(service = Servlet.class,property =
        {"sling.servlet.methods=GET","sling.servlet.paths=/bin/searchrepo"})
public class SearchRepo extends SlingSafeMethodsServlet {
    @Reference
    QueryBuilder queryBuilder;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//        response.getWriter().write("hello");

        try {
            String inputData= request.getParameter("inputData");
            ResourceResolver resolver = request.getResourceResolver();
            Session session= resolver.adaptTo(Session.class);
            Query query= queryBuilder.createQuery(PredicateGroup.create(getQuery(inputData)),session);
            SearchResult searchResult= query.getResult();
          List<List<String>>dataList= new ArrayList<>();
            for (Iterator<Resource> it = searchResult.getResources(); it.hasNext(); ) {
                Resource res = it.next();

                String pname=res.getName();
                String ppath=res.getPath();
                List<String>list= new ArrayList<>();
                list.add(pname);
                list.add(ppath);

                dataList.add(list);
            }

            response.getWriter().write(new Gson().toJson(dataList));
        }
        catch(Exception e){
            response.getWriter().write(new Gson().toJson(e));
        }


    }
    public Map<String,String> getQuery(String data){
Map<String,String>map= new HashMap<>();
map.put("path","/content/we-retail");
map.put("fulltext",data);
map.put("p.limit","10");
return  map;

    }
}
