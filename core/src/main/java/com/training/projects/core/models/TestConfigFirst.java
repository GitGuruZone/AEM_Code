package com.training.projects.core.models;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Enter Name")
public @interface TestConfigFirst {

    @AttributeDefinition(name = "Enter name")
    String name() default "sahil";

    @AttributeDefinition(name = "Enter age", description = "Enter your age")
    int age() default 18;

    @AttributeDefinition(name = "Enable feature", description = "Enable or disable the feature")
    boolean isEnabled() default true;
}
