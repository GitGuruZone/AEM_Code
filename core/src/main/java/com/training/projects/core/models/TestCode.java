package com.training.projects.core.models;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestCode {

    @ValueMapValue
    String text;
    @ValueMapValue
    String pathfield;

    @ChildResource
     Resource multipleproduct;

    HashMap<String,String>multimap= new HashMap<>();
    @PostConstruct
    public void init() {
        if (multipleproduct != null && multipleproduct.hasChildren()) {
            for (Resource resource : multipleproduct.getChildren()) {
                ValueMap valueMap = resource.getValueMap();
                String multipath = valueMap.get("multipath", String.class);
                String multitext = valueMap.get("multitext", String.class);

                // Check if multipath and multitext are not null before adding them to the map
                if (multipath != null && multitext != null) {
                    multimap.put(multipath, multitext);
                }
            }
        }
    }
    public String getText() {
        return text;
    }

    public String getPathfield() {
        return pathfield;
    }

    public HashMap<String, String> getMultimap() {
        return multimap;
    }
}
