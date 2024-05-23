package com.training.projects.core.servlets;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/apps/call/workflow",
        "sling.servlet.methods=GET"
})
public class WorkFlowServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String status = "execution_failed";
        ResourceResolver resolver = null;
        try {
            resolver = request.getResourceResolver();
            WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class);

            if (workflowSession == null) {
                throw new ServletException("Unable to obtain WorkflowSession");
            }

            WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/custommodel");

            if (workflowModel == null) {
                throw new ServletException("Unable to obtain WorkflowModel");
            }

            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", "/content/training_projects/us/en");

            if (workflowData == null) {
                throw new ServletException("Unable to create WorkflowData");
            }

            status = workflowSession.startWorkflow(workflowModel, workflowData).getState();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error starting workflow: " + e.getMessage());
            return;
        } finally {
            if (resolver != null) {
                resolver.close();
            }
        }

        response.setStatus(SlingHttpServletResponse.SC_OK);
        response.getWriter().write(status);
    }
}
