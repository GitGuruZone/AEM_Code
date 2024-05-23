package com.training.projects.core.servlets;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component(service = Servlet.class,property = {"sling.servlet.paths=/bin/Excel","Sling.servlet.methods=GET"})
public class ExcelReaderServlet extends SlingAllMethodsServlet {

    @Reference
    private AssetManager assetManager;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {


    String assetPath = "/content/dam/training_projects/data.xlsx";
        List<String> excelData = readExcelData(assetPath);
        String jsonData = convertListToJson(excelData);
        response.setContentType("application/json");
        response.getWriter().write(jsonData);
    }

    private List<String> readExcelData(String assetPath) {
        List<String> data = new ArrayList<>();
        try {
            // Get the asset from DAM

            Asset asset = assetManager.getAssetForBinary(assetPath);
            if (asset == null) {
                System.out.println("Asset not found at path: " + assetPath);
                return data;
            }

            // Get the binary data of the asset
            InputStream inputStream = new ByteArrayInputStream(asset.getOriginal().getStream().readAllBytes());

            // Create a Workbook object from the Excel file
            Workbook workbook = WorkbookFactory.create(inputStream);

            // Assuming the first sheet contains the data
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through rows and columns to read data
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Assuming all data is text
                    String cellValue = cell.getStringCellValue();
                    data.add(cellValue);
                }
            }

            // Close the workbook and input stream
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private String convertListToJson(List<String> dataList) {
        // Convert list to JSON (You can use libraries like Gson or Jackson for better JSON handling)
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (String item : dataList) {
            jsonBuilder.append("\"").append(item).append("\",");
        }
        if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
