package com.training.projects.core.servlets;

import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component(service = Servlet.class,property = {"sling.servlet.methods=GET","sling.servlet.paths=/apps/Carousel"})
public class Carousel extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        int productSize=10;
      String api="https://api-uat2.royalenfield.com/aem-content/get-data-wanderlust?startDate=2024-04-24%2006:00:00%20PM&endDate=2024-04-24%2007:00:00%20PM&page=1&module=SEARCH&limit="+productSize;
        URL url= new URL(api);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String input;
        StringBuffer reader= new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while((input=in.readLine())!=null){
            reader.append(input);
        }
        try {
            JSONObject obj= new JSONObject(reader.toString());
            List<Product>list= new ArrayList<>();
                     int i=0;
//            product productobj= new product();
            while (i < productSize) {
                String imagePath = obj.getJSONArray("data").getJSONObject(0).getJSONArray("productList").getJSONObject(i).optString("bike_imagePath");
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Create a new Product object
                    Product productObj = new Product();
                    productObj.setName(obj.getJSONArray("data").getJSONObject(0).getJSONArray("productList").getJSONObject(i).getString("bikeName"));
                    productObj.setImg(imagePath);
                    // Add the Product object to the list
                    list.add(productObj);
                }
                i++;
            }

           response.getWriter().write(new Gson().toJson(list));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
class Product{
    private String name;
    private String img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
