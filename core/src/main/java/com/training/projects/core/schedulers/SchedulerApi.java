package com.training.projects.core.schedulers;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Node;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SchedulerApi is a scheduled task that fetches data from a REST API
 * and updates content in the AEM JCR repository.
 */
@Component(service = Runnable.class)
@Designate(ocd = SchedulerApi.Config.class)
public class SchedulerApi implements Runnable {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @ObjectClassDefinition(name = "SchedulerApi Configuration")
    public @interface Config {
        /**
         * this method return cron exp.
         * @return {String}
         */
        @AttributeDefinition(name = "Cron-job expression")
        String schedulerExpression() default "";
    }

    private final Logger logger
            = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        String api = "https://dummy.restapiexample.com/api/v1/employees";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "testuser");

        try (ResourceResolver resolver
                     = resourceResolverFactory
                .getServiceResourceResolver(paramMap)) {
            URL url = new URL(api);
            HttpsURLConnection connection
                    = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                JSONObject responseObject = new JSONObject(response.toString());
                List<String> employeeNames = new ArrayList<>();
                int length = responseObject.getJSONArray("data").length();

                for (int i = 0; i < length; i++) {
                    employeeNames.add(responseObject.getJSONArray(
                       "data").getJSONObject(i).getString("employee_name"));
                }

                Session session = resolver.adaptTo(Session.class);
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                Page newPage = pageManager.create(
                        "/content/training_projects/us/en", "NEWPAGE",
                        "/conf/we-retail/settings/wcm/templates/content-page",
                        "NEWPAGE");
                String path = newPage.getPath()
                        + "/jcr:content/root/container/title";
                Node node = session.getNode(path);
                node.setProperty("jcr:title", "new node");
                session.save();

                logger.info("Employee names: {}", employeeNames);

            } catch (IOException | JSONException | RepositoryException e) {
                logger.error("Error reading or parsing the API response", e);
            }

        } catch (LoginException | WCMException | IOException e) {
            logger.error("Error accessing the JCR repository", e);
        }
    }
}
