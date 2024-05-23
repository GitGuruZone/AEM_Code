package com.training.projects.core.models;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = WorkflowProcess.class, immediate = true, property = {
        "process.label=dam-asset-add"
})
public class WorkflowExcel implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExcel.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String path = (String) workItem.getWorkflowData().getPayload();
        log.info("Payload path: {}", path);

        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = getCustomResource();
            if (StringUtils.isNotEmpty(path)) {
                Resource excelResource = resourceResolver.getResource(path);
                if (excelResource == null) {
                    log.error("Resource not found at path: {}", path);
                    return;
                }
                Asset asset =excelResource.adaptTo(Asset.class);
                Rendition rendition= asset.getOriginal();

                InputStream inputStream = rendition.adaptTo(InputStream.class);
                if (inputStream == null) {
                    log.error("Unable to adapt resource to InputStream: {}", excelResource.getPath());
                    return;
                }

                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell name1 = row.getCell(0);
                    String name=name1+"";
                    Cell roll2 = row.getCell(1);
                    String roll=roll2+"";
                    if (name!= null && roll != null) {
//                        String name = nameCell.getStringCellValue();
//                       int roll= rollCell.getColumnIndex();
                        log.info("Parsed data - Name: {}, Roll: {}", name, roll);

                        // Store data in the node structure
                        String nodePath = "/content" + name;
                        Resource productResource = resourceResolver.getResource(nodePath);
                        if (productResource == null) {
                            productResource = resourceResolver.create(resourceResolver.getResource("/content"), name, null);
                        }

                        ModifiableValueMap properties = productResource.adaptTo(ModifiableValueMap.class);
                        properties.put("roll", roll);
                    }
                }

                resourceResolver.commit();
                workbook.close();
            } else {
                log.warn("Payload path is empty or null");
            }
        } catch (LoginException | IOException e) {
            log.error("Error during workflow process execution", e);
            throw new WorkflowException(e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }

    private ResourceResolver getCustomResource() throws LoginException {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "customuser");
        ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(param);
        log.info("Obtained ResourceResolver for customuser");
        return resolver;
    }
}
