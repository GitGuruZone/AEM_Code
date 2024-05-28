package com.training.projects.core.services;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;
import com.adobe.granite.taskmanagement.TaskManagerFactory;
import com.adobe.granite.workflow.exec.InboxItem;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component(service = AemNotificationService.class,immediate = true)

public class AemNotificationService {

    String SYSTEM_USER="customuser";
     @Reference
    ResourceResolverFactory resourceResolverFactory;
   public void sendNotification(String Title, String Description, InboxItem.Priority priority) {
       ResourceResolver resolver=null;

       try {
           resolver=getResolver();
           TaskManager taskManager= resolver.adaptTo(TaskManager.class);
           TaskManagerFactory taskManagerFactory = taskManager.getTaskManagerFactory();
           Task task= taskManagerFactory.newTask(Task.DEFAULT_TASK_TYPE);
           task.setName(Title);
           task.setPriority(InboxItem.Priority.LOW);
           task.setDescription(Description);
           task.setCurrentAssignee("admin");
           taskManager.createTask(task);



       } catch (TaskManagerException | LoginException e) {
           throw new RuntimeException(e);
       }

   }
   public ResourceResolver getResolver() throws LoginException {
       Map<String,Object>map = new HashMap<>();
       map.put(ResourceResolverFactory.SUBSERVICE,SYSTEM_USER);
       return resourceResolverFactory.getServiceResourceResolver(map);
   }
}
