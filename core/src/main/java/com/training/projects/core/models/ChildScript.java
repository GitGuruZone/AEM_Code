package com.training.projects.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * this class used for ScriptVariable from Parent.
 */
@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChildScript {
    @SlingObject
    private ResourceResolver resourceResolver;
    @SlingObject
    private SlingHttpServletRequest request;
    private static final String INHERITED_SCRIPT_PROPERTY = "datatext";
    private String propertValue = StringUtils.EMPTY;

    /**
     * this method used to get script variable.
     */
    @PostConstruct
    public void init() {
        Logger logger = LoggerFactory.getLogger(getClass());
        try {
            PageManager pgMgr = resourceResolver.adaptTo(PageManager.class);
            String[] paths = request.getPathInfo().split("\\.");
            String path = paths[0];
            Page childPage = pgMgr.getPage(path);
            Boolean flag = true;
            while (childPage.getParent() != null && flag) {

                propertValue = childPage.getProperties()
                        .get(INHERITED_SCRIPT_PROPERTY, String.class);
                if (propertValue != null) {
                    flag = false;

                }
                childPage = childPage.getParent();
            }
            logger.info("Script Variable is :" + propertValue);
        } catch (Exception e) {
            logger.info("No Script variable found in any page .......");
        }
    }

    public String getPropertValue() {
        return propertValue;
    }
}
