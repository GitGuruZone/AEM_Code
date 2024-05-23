package com.training.projects.core.servlets;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(name = "Enter your details: ")
public  @interface SortConfig{
    @AttributeDefinition(name = "Enter Your Path: ")
    String path()default "/content";
    @AttributeDefinition(name = "Enter your limit")
    String limit()default "5";
    @AttributeDefinition(name="Choose any one: ",options =
            {
                    @Option(label = "High to Low",value="desc"),
                    @Option(label = "Low to High",value="@jcr:content/jcr:title")
            })
    public String sort() default "";

}