package com.training.projects.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * this is configMgr.
 */
@ObjectClassDefinition(name = "Enter Name")
public @interface TestConfigFirst {

    /**
     * this method return name.
     *
     * @return {String}
     */
    @AttributeDefinition(name = "Enter name")
    String name() default "sahil";

    /**
     * this method return age.
     *
     * @return {int}
     */
    @AttributeDefinition(name = "Enter age",
            description = "Enter your age")
    int age() default 18;

    /**
     * this method return enable or desable feature.
     *
     * @return {boolean}
     */
    @AttributeDefinition(name = "Enable feature",
            description = "Enable or disable the feature")
    boolean isEnabled() default true;
}
