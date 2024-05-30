package com.training.projects.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(name = "Enter your details: ")
public @interface SortConfig {
    /**
     * this method return path.
     * @return {String}
     */
    @AttributeDefinition(name = "Enter Your Path: ")
    String path() default "/content";

    /**
     * this method return limit.
     * @return {String}
     */
    @AttributeDefinition(name = "Enter your limit")
    String limit() default "5";

    /**
     * this method used to choose any value.
     * @return {String}
     */
    @AttributeDefinition(name = "Choose any one: ", options =
            {
              @Option(label = "High to Low", value = "desc"),
              @Option(label = "Low to High", value = "@jcr:content/jcr:title")
            })
    String sort() default "";

}
