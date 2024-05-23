package com.training.projects.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/apps/upload/assets",
        "sling.servlet.methods=GET"
})
public class UploadAsset extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(UploadAsset.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        InputStream is = null;
        ResourceResolver resourceResolver = null;
        try {
            File initialFile = new File("D:\\Desktop\\PDF\\excelsheet.xlsx");
            is = new FileInputStream(initialFile);
            String fileName = "excelsheet"; // Including the extension
            String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            // Set the correct MIME type
            String path = "/content/dam/training_projects/";

            resourceResolver = request.getResourceResolver();
            String newFilePath = writeToDam(is, fileName, path, mimeType, resourceResolver);

            if (newFilePath != null) {
                response.getWriter().write("File uploaded successfully to: " + newFilePath);
            } else {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("File upload failed.");
            }

        } catch (Exception e) {
            log.error("Error uploading file to DAM", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error uploading file to DAM: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("Error closing InputStream", e);
                }
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }

    public String writeToDam(InputStream is, String fileName, String path, String mimeType, ResourceResolver resourceResolver) {
        try {
            com.day.cq.dam.api.AssetManager assetMgr = resourceResolver.adaptTo(com.day.cq.dam.api.AssetManager.class);
            if (assetMgr != null) {
                String newFilePath = path + fileName;
                assetMgr.createAsset(newFilePath, is, mimeType, true);
                return newFilePath;
            } else {
                log.error("AssetManager could not be adapted from ResourceResolver");
            }
        } catch (Exception e) {
            log.error("Error creating asset in DAM", e);
        }
        return null;
    }

    public ResourceResolver getNewResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "testuser");
        return resourceResolverFactory.getServiceResourceResolver(map);
    }
}
