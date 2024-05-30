package com.training.projects.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * this model return productData.
 */
@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductId {
    @SlingObject
    private ResourceResolver resourceResolver;
    @SlingObject
    private SlingHttpServletRequest request;
    private List<ProductIdPojo> productList = new ArrayList<>();

    /**
     * it gives data by using product,
     * id from PageProperty.
     */
    @PostConstruct
    public void init() {
        Logger logger = LoggerFactory.getLogger(getClass());
        try {

            PageManager pageManager = resourceResolver.adaptTo(
                    PageManager.class);
            String[] paths = request.getPathInfo().split("\\.");
            String path = paths[0];
            Page page = pageManager.getPage(path);
            String id = page.getProperties().get("productid").toString();
            if (id != null) {
                String apikey
                        = "https://fakestoreapi.com/products/" + id;
                URL url = new URL(apikey);
                HttpURLConnection connection
                        = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonObj = new JSONObject(response.toString());
                ProductIdPojo productIdPojo = new ProductIdPojo();
                productIdPojo.setTitle(jsonObj.getString("title"));
                productIdPojo.setImage(jsonObj.getString("image"));

                productList.add(productIdPojo);
                logger.info("our dataList is " + productList);
            } else {
                // Handle the case where currentPage is null
                logger.info("Provide id to get data from API");
            }
        } catch (Exception e) {
            // Handle exceptions
            logger.info("their is some error in try block correct it");
        }
    }

    public List<ProductIdPojo> getList() {
        return productList;
    }
}
