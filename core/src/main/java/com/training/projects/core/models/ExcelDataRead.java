package com.training.projects.core.models;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = "process.label=Excel-Read-Workflow"
)
public class ExcelDataRead implements WorkflowProcess {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataRead.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String payLoad = (String) workItem.getWorkflowData().getPayload();
        String[] fileType = payLoad.split("\\.");
        String folderPath = StringUtils.substringBeforeLast(payLoad, "/");
        String fileName = StringUtils.substringAfterLast(payLoad, "/");
        String alphaOnlyFileName = fileName.replaceAll("[^a-zA-Z]", "");
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = getResolver();
            Resource excelResource = resourceResolver.getResource(payLoad);
            if ("xlsx".equals(fileType[1]) || "xls".equals(fileType[1])) {
                Asset asset = excelResource.adaptTo(Asset.class);
                Rendition rendition = asset.getOriginal();
                InputStream inputStream = rendition.adaptTo(InputStream.class);
                if (inputStream == null) {
                    logger.error("Unable to adapt resource to InputStream: {}", excelResource.getPath());
                    return;
                }
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                if (sheet == null) {
                    logger.error("No excel sheet is present");
                    return;
                }
                Session session = resourceResolver.adaptTo(Session.class);
                Node folderNode = session.getNode(folderPath);
                Node parentNode = folderNode.addNode(alphaOnlyFileName, "sling:OrderedFolder");
                session.save();
                String parentNodePath = parentNode.getPath();
                Iterator<Row> iterator = sheet.iterator();
Session session1= resourceResolver.adaptTo(Session.class);
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Cell nameCell = row.getCell(0);
                    Cell rollCell = row.getCell(1);
                    if (nameCell != null && rollCell != null) {
                        String name = nameCell.toString();
                        String roll = rollCell.toString();
                        Node node= session1.getNode(parentNodePath);
                        Node childNode = node.addNode(name, "nt:unstructured");
                        childNode.setProperty("roll", roll);
                    }
                }
                session1.save();
            } else {
                logger.error("File type is not Excel");
            }
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }

    public ResourceResolver getResolver() throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "customuser");
        return resourceResolverFactory.getServiceResourceResolver(map);
    }
}
