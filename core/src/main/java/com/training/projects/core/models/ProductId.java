package com.training.projects.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductId {

    @Inject
    Page currentPage;
    @SlingObject
    ResourceResolver resourceResolver;
@SlingObject
        SlingHttpServletRequest request;
    List<ProductIdPojo> productList = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {

            PageManager pageManager= resourceResolver.adaptTo(PageManager.class);
            String [] paths=request.getPathInfo().split("\\.");
            String path=paths[0];
//            pageManager.getContainingPage(request.getResource()).getProperties().get
            Page page=pageManager.getPage(path);
           String id= page.getProperties().get("productid").toString();
            if (id!=null) {
//                String productId = currentPage.getProperties().get("productid", String.class);
                String apikey = "https://fakestoreapi.com/products/" + id;
                URL url = new URL(apikey);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonObj = new JSONObject(response.toString());
                ProductIdPojo productIdPojo= new ProductIdPojo();
                productIdPojo.setTitle(jsonObj.getString("title"));
                productIdPojo.setImage(jsonObj.getString("image"));

                productList.add(productIdPojo);
            } else {
                // Handle the case where currentPage is null
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging, you might want to log this instead
        }
    }

    public List<ProductIdPojo> getList() {
        return productList;
    }
}
