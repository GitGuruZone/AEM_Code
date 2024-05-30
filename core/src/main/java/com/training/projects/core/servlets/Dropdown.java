package com.training.projects.core.servlets;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component(service = Servlet.class, property = {"sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/dropdown"
})
public class Dropdown extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        String path = "/content/training_projects/dcr";
        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = resolver.getResource(path);
        Iterator<Resource> itr = resource.listChildren();
        List<Keyvalue> caseList = new ArrayList<>();
        while (itr.hasNext()) {
            Resource res = itr.next();
            String name = res.getName();
            ValueMap valueMap = res.getValueMap();
            double cases = valueMap.get(name, Double.class);
            caseList.add(new Keyvalue(name, cases));

        }
        DataSource ds =
                new SimpleDataSource(
                        new TransformIterator(
                                caseList.iterator(),
                                input -> {
                                    Keyvalue keyValue = (Keyvalue) input;
                                    ValueMap vm = new ValueMapDecorator(
                                            new HashMap<>());
                                    vm.put("value", keyValue.getCases());
                                    vm.put("text", keyValue.getState());
                                    return new ValueMapResource(
                                            resolver, new ResourceMetadata(),
                                            JcrConstants.NT_UNSTRUCTURED, vm);
                                }));
        request.setAttribute(DataSource.class.getName(), ds);

    }


}



