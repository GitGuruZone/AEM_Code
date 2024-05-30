//package com.training.projects.core.schedulers;
//
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.metatype.annotations.AttributeDefinition;
//import org.osgi.service.metatype.annotations.Designate;
//import org.osgi.service.metatype.annotations.ObjectClassDefinition;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Component(service = Runnable.class)
//@Designate(ocd=TestScheduler.config.class)
//public class TestScheduler implements Runnable{
//
//    Logger logger= LoggerFactory.getLogger(TestScheduler.class);
//    @ObjectClassDefinition(name = "Testing Scheduler")
//    public @interface config {
//        @AttributeDefinition(name = "Enter cron exp: ")
//        public String scheduler_expression() default "*/30 * * * * ?";
//
//            }
//    @Override
//    public void run() {
//logger.error("Dheeraj Jinda hai! ");
//    }
//}
