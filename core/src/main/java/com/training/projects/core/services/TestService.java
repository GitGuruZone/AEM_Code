package com.training.projects.core.services;

import com.training.projects.core.configuration.TestConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = TestService.class)
@Designate(ocd = TestConfiguration.class)
public class TestService {
    private String name = StringUtils.EMPTY;
    private String city = StringUtils.EMPTY;
    private int age = 0;

    /**
     * activate method  used to assign values.
     * @param testConfiguration
     */
    @Activate
    public void activate(final TestConfiguration testConfiguration) {
        name = testConfiguration.name();
        age = testConfiguration.age();
        city = testConfiguration.City();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }
}
