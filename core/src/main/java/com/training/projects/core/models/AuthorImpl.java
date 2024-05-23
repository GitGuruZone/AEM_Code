package com.training.projects.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AuthorImpl implements  Author{

    @ValueMapValue
    private String first;
    @ValueMapValue
    private String last;
    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }


}
