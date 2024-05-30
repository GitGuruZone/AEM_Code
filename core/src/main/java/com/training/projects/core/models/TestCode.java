package com.training.projects.core.models;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * this is model return data of multifield.
 */

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestCode {
    @ValueMapValue
    private String text;
    @ValueMapValue
    private String pathfield;
    @ChildResource
    private Resource multipleproduct;

    private HashMap<String, String> multimap = new HashMap<>();

    /**
     * this method return multifield data.
     */

    @PostConstruct
    public void init() {
        if (multipleproduct != null && multipleproduct.hasChildren()) {
            for (Resource resource : multipleproduct.getChildren()) {
                ValueMap valueMap = resource.getValueMap();
                String multipath = valueMap.get("multipath", String.class);
                String multitext = valueMap.get("multitext", String.class);
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
