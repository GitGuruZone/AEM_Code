package com.training.projects.core.schedulers;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component(service = Runnable.class)
@Designate(ocd = CoronaAPI.Corona.class)
public class CoronaAPI implements Runnable {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private final Logger logger = LoggerFactory.getLogger(CoronaAPI.class);

    @ObjectClassDefinition(name = "Corona new Cron")
    public @interface Corona {
        /**
         * this method gives cronExp.
         * @return {String}
         */
        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "";

        /**
         * this return boolean value.
         * @return {boolean}
         */
        @AttributeDefinition(
                name = "Concurrent task",
                description = "Whether or not schedule this task concurrently"
        )
        boolean scheduler_concurrent() default false;
    }

    @Override
    public void run() {
        String path = "/content/training_projects/dcr";
        String apiUrl = "https://api.rootnet.in/covid19-in/stats/latest";
        ResourceResolver resourceResolver = null;
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "testuser");

        try {
            resourceResolver
                    = resourceResolverFactory.getServiceResourceResolver(map);
            URL url = new URL(apiUrl);
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder reader = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    reader.append(inputLine);
                }
            }

            JSONObject jsonResponse = new JSONObject(reader.toString());
            JSONArray regionalData = jsonResponse.getJSONObject(
                    "data").getJSONArray("regional");

            Session session = resourceResolver.adaptTo(Session.class);
            if (session != null) {
                Node rootNode = session.getNode(path);
                for (int i = 0; i < regionalData.length(); i++) {
                    JSONObject stateData = regionalData.getJSONObject(i);
                    String stateName = stateData.getString(
                            "loc").replace(" ", "_");
                    int totalCases = stateData.getInt("totalConfirmed");

                    Node stateNode = rootNode.addNode(stateName);
                    stateNode.setProperty("totalCases", totalCases);
                }
                session.save();
            }
        } catch (LoginException | JSONException
                 | IOException | RepositoryException e) {
            logger.error("Error during the execution of the scheduled task", e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
}
