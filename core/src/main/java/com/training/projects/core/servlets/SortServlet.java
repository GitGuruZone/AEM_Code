package com.training.projects.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
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
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

import javax.inject.Inject;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

@Component(service = Servlet.class,property =
        {
                "sling.servlet.paths=/bin/sort/servlet",
                "sling.servlet.methods=GET"
        }
)
@Designate(ocd=SortConfig.class)
public class SortServlet extends SlingSafeMethodsServlet {

    @Reference
    QueryBuilder queryBuilder;
@Reference
    ResourceResolverFactory resourceResolverFactory;

    String path;
    String limit;
    String sortType;
    @Activate
    public void activate(SortConfig sortConfig){
        path=sortConfig.path();
        limit =sortConfig.limit();
        sortType= sortConfig.sort();
    }
    List<String> list= new ArrayList<>();
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver=null;
        try{
            Map<String,Object> authMap= new HashMap<>();
            authMap.put(ResourceResolverFactory.SUBSERVICE,"testuser");
            resolver=resourceResolverFactory.getServiceResourceResolver(authMap);
            Session session=resolver.adaptTo(Session.class);
            Query query= queryBuilder.createQuery(PredicateGroup.create(getMap(path,limit,sortType)),session);
            SearchResult result= query.getResult();
            for (Iterator<Resource> it = result.getResources(); it.hasNext(); ) {
                Resource resource = it.next();
                ValueMap valueMap=resource.getValueMap();
                list.add(valueMap.get("jcr:content/jcr:title", String.class));


            }
        }
        catch (Exception e){

        }
        Gson gson= new Gson();
        response.getWriter().write(gson.toJson(list));
    }
    public Map<String,String> getMap(String path,String limit,String sortType){
        Map<String ,String >map= new HashMap<>();
        map.put("path",path);
        map.put("type","cq:Page");
        map.put("property","jcr:content/jcr:title");
if(sortType.equals("desc")){
    map.put("orderby","@jcr:content/jcr:title");
    map.put("orderby.sort",sortType);
}else {
    map.put("orderby", "@jcr:content/jcr:title");
}
        map.put("p.limit","5");
        return map;
    }

}
