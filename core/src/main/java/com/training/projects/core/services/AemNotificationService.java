package com.training.projects.core.services;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;
import com.adobe.granite.taskmanagement.TaskManagerFactory;
import com.adobe.granite.workflow.exec.InboxItem;
import com.training.projects.core.commonutils.CommonUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * this service is used to send AEM Console Notification.
 */
@Component(service = AemNotificationService.class, immediate = true)

public class AemNotificationService {

    private String systemUser = "customuser";
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * this method used to send notification.
     * @param title
     * @param description
     * @param priority
     */

    public void sendNotification(final String title, final String description,
                                 final InboxItem.Priority priority) {
        ResourceResolver resolver = null;

        try {
            resolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemUser);
            TaskManager taskManager = resolver.adaptTo(TaskManager.class);
            TaskManagerFactory taskManagerFactory
                    = taskManager.getTaskManagerFactory();
            Task task = taskManagerFactory.newTask(Task.DEFAULT_TASK_TYPE);
            task.setName(title);
            task.setPriority(InboxItem.Priority.LOW);
            task.setDescription(description);
            task.setCurrentAssignee("admin");
            taskManager.createTask(task);


        } catch (TaskManagerException | LoginException e) {
            throw new RuntimeException(e);
        }

    }


}
