package com.training.projects.core.models;

import com.training.projects.core.services.TestService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;


@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestModel {
    @OSGiService
    private TestService testService;

    public String getName() {
        return testService.getName();
    }

    public int getAge() {
        return testService.getAge();
    }

    public String getCity() {
        return testService.getCity();
    }
}
