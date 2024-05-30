package com.training.projects.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Multifield {
    @ChildResource
    private Resource customproduct;

    private List<MultifieldPOJO> dataList = new ArrayList<>();
    private List<String> list = new ArrayList<>();

    /**
     * this method return multifield data.
     */
    @PostConstruct
    public void init() {
        for (Resource resource : customproduct.getChildren()) {

            MultifieldPOJO obj = new MultifieldPOJO();
            ValueMap valueMap = resource.getValueMap();
            obj.setText(valueMap.get("text", String.class));
            obj.setPath(valueMap.get("path", String.class));
            dataList.add(obj);

            if (resource.hasChildren()) {
                Resource childres = resource.getChild("childcustomproduct");
                for (Resource resource1 : childres.getChildren()) {
                    ValueMap valueMap1 = resource1.getValueMap();
                    list.add(valueMap1.get("childtext", String.class));
                }
            }
        }

    }

    public List<MultifieldPOJO> getDataList() {
        return dataList;
    }

    public List<String> getList() {
        return list;
    }
}
