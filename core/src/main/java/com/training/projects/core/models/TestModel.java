package com.training.projects.core.models;

import com.training.projects.core.services.TestService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TestModel {
    @OSGiService
    TestService testService;
    public String getName() {
        return testService.getName();
    }
    public int getAge(){return testService.getAge();}
    public String getCity(){return testService.getCity();}
}
