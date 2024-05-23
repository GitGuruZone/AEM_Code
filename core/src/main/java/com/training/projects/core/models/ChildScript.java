package com.training.projects.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChildScript {
   @SlingObject
    ResourceResolver resourceResolver;
    @SlingObject
    SlingHttpServletRequest request;
    private static final String INHERITED_SCRIPT_PROPERTY = "datatext";

    @ValueMapValue
    String datatext;
    String propertValue=StringUtils.EMPTY;



    @PostConstruct
    public void init(){
        try {
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            String[] paths = request.getPathInfo().split("\\.");
            String path = paths[0];
//            pageManager.getContainingPage(request.getResource()).getProperties().get
            Page childPage = pageManager.getPage(path);
            Boolean flag = true;
            while (childPage.getParent() != null && flag) {

                propertValue = childPage.getProperties().get(INHERITED_SCRIPT_PROPERTY, String.class);
                if (propertValue != null) {
                    flag = false;

                }
                childPage = childPage.getParent();
            }
        }
            catch(Exception e){

            }
    }

    public String getPropertValue() {
        return propertValue;
    }
}
