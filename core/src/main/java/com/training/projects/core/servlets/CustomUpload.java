package com.training.projects.core.servlets;

import com.day.cq.dam.api.AssetManager;
import com.training.projects.core.commonutils.CommonUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Component(service = Servlet.class, immediate = true, property =
        {
                "sling.servlet.paths=/bin/custom/upload",
                "sling.servlet.methods=GET"
        })
public class CustomUpload extends SlingSafeMethodsServlet {
    private String systemUser = "customuser";
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resolver = null;
        InputStream is = null;
        try {
            File file = new File("D:\\Desktop\\PDF\\ticket.pdf");
            is = new FileInputStream(file);
            String path = "/content/dam/training_projects/";
            String nameOfFile = "ticket";
            String mimeType = "application/pdf";
            resolver = CommonUtils.getResolver(
                    resourceResolverFactory, systemUser);
            String fileName = writeTodam(is, nameOfFile, path,
                    mimeType, resolver);
            if (fileName != null) {
                response.getWriter().write("Uploaded......");
            } else {
                response.getWriter().write("not uploaded......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            resolver.close();
        }
    }

    /**
     * this method used to upload data in dam.
     *
     * @param is
     * @param name
     * @param path
     * @param mime
     * @param resolver
     * @return {String}
     */
    public String writeTodam(final InputStream is,
                             final String name,
                             final String path,
                             final String mime,
                             final ResourceResolver resolver) {
        AssetManager assetManager = resolver.adaptTo(AssetManager.class);
        if (assetManager != null) {
            String filePath = path + name;
            assetManager.createAsset(filePath, is, mime, true);
            return filePath;
        } else {
            return null;
        }
    }
}
