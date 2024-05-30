package com.training.projects.core.servlets;

import com.training.projects.core.commonutils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/apps/upload/assets",
        "sling.servlet.methods=GET"
})
public class UploadAsset extends SlingSafeMethodsServlet {
    private String systemUser = "customuser";

    private final Logger log
            = LoggerFactory.getLogger(UploadAsset.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        InputStream is = null;
        ResourceResolver resourceResolver = null;
        try {
            File initialFile = new File("D:\\Desktop\\PDF\\excelsheet.xlsx");
            is = new FileInputStream(initialFile);
            String fileName = "excelsheet"; // Including the extension
            String mimeType = "application/vnd.openxmlformats-officedocument"
                    + ".spreadsheetml.sheet";
            // Set the correct MIME type
            String path = "/content/dam/training_projects/";

            resourceResolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemUser);
            String newFilePath = writeToDam(is, fileName, path,
                    mimeType, resourceResolver);

            if (newFilePath != null) {
                response.getWriter().write("File uploaded successfully to: "
                        + newFilePath);
            } else {
                response.setStatus(
                        SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("File upload failed.");
            }

        } catch (Exception e) {
            log.error("Error uploading file to DAM", e);
            response.setStatus(SlingHttpServletResponse
                    .SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                    "Error uploading file to DAM: " + e.getMessage());
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

    /**
     * this method used to upload data.
     *
     * @param is
     * @param fileName
     * @param path
     * @param mimeType
     * @param resourceResolver
     * @return {String}
     */
    public String writeToDam(final InputStream is, final String fileName,
                             final String path, final String mimeType,
                             final ResourceResolver resourceResolver) {
        try {
            com.day.cq.dam.api.AssetManager assetMgr
                    = resourceResolver.adaptTo(
                    com.day.cq.dam.api.AssetManager.class);
            if (assetMgr != null) {
                String newFilePath = path + fileName;
                assetMgr.createAsset(newFilePath, is, mimeType, true);
                return newFilePath;
            } else {
                log.error("assetMgr could not adapted from ResourceResolver");
            }
        } catch (Exception e) {
            log.error("Error creating asset in DAM", e);
        }
        return null;
    }

}
