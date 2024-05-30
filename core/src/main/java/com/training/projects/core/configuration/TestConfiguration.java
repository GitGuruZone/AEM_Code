package com.training.projects.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

/**
 * OSGi Configuration interface for entering user details.
 */
@ObjectClassDefinition(name = "Enter Details")
public @interface TestConfiguration {
    /**
     * it return person name.
     *
     * @return {String}
     */
    @AttributeDefinition(name = "Enter Your Name")
    String name() default "sahil";

    /**
     * it return age.
     *
     * @return {int}
     */
    @AttributeDefinition(name = "Enter your age")
    int age() default 22;

    /**
     * Choose one thing from it.
     *
     * @return {String}
     */
    @AttributeDefinition(name = "Choose one thing ",
            options = {
                    @Option(label = "Sirsa", value = "ssa"),
                    @Option(label = "Delhi", value = "DL"),
                    @Option(label = "Noida", value = "noida")
            },
            type = AttributeType.STRING
    )
    String City() default "null";

    /**
     * it return country name.
     *
     * @return {String}
     */
    @AttributeDefinition(name = "Choose field")
    String[] getCountry() default {"usa", "india"};
}
