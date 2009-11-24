/*
 * Copyright 2006-2009 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kuali.rice.core.config.ConfigContext;
import org.kuali.rice.core.config.ConfigurationException;
import org.kuali.rice.core.lifecycle.Lifecycle;
import org.kuali.rice.core.util.ClassLoaderUtils;
import org.kuali.rice.kew.batch.FileXmlDocCollection;
import org.kuali.rice.kew.batch.XmlDoc;
import org.kuali.rice.kew.batch.XmlDocCollection;
import org.kuali.rice.kew.batch.XmlIngesterService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class KraKEWXmlDataLoaderLifecycle implements Lifecycle {

    private boolean started;

    private String pathname;

    public KraKEWXmlDataLoaderLifecycle() {
        this("classpath:kew/xml");
    }

    public KraKEWXmlDataLoaderLifecycle(String pathname) {
        this.pathname = pathname;
    }

    public boolean isStarted() {
        return started;
    }

    public void start() throws Exception {
        if (new Boolean(ConfigContext.getCurrentContextConfig().getProperty("use.kraKewXmlDataLoaderLifecycle"))) {
            loadKraKewXml();
            started = true;
        }
    }

    public void stop() throws Exception {
        started = false;
    }


    
    private void loadKraKewXml() throws Exception {
        List<String> rules = new ArrayList<String>();
        Resource resource = getFileResource(pathname);
        String filename;
        
        List<String> parentKewFiles = new ArrayList<String>();
        parentKewFiles.add("DefaultKewTestData.xml");
        parentKewFiles.add("KualiDocument.xml");
        parentKewFiles.add("RiceDocument.xml");
        parentKewFiles.add("KC.xml"); 
        parentKewFiles.add("KcMaintenanceDocument.xml");
        parentKewFiles.add("KcProposalsMaintenanceDocument.xml");
        parentKewFiles.add("KcAwardsMaintenanceDocument.xml");
        parentKewFiles.add("KcComplianceMaintenanceDocument.xml");
        parentKewFiles.add("KcSharedMaintenanceDocument.xml");
        parentKewFiles.add("KcMiscellaneousMaintenanceDocument.xml");
        
        for(String parentKewFile : parentKewFiles) {
            this.loadXmlFile(pathname + File.separator + parentKewFile);
        }
        for (File file : resource.getFile().listFiles()) {
            filename=file.getName();
            if (parentKewFiles.contains(filename)) {
                // do nothing
            } else if (filename.endsWith("Rules.xml")) {
                rules.add(filename);
            } else if (filename.endsWith(".xml")) {
                this.loadXmlFile(pathname + File.separator + filename);
            }
        }
        for (String fileName : rules) {
            this.loadXmlFile(pathname + File.separator + fileName);
        }

    }

    private Resource getFileResource(String sourceName) {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(ClassLoaderUtils.getDefaultClassLoader());
        return resourceLoader.getResource(sourceName);
    }
    
    /**
     * Gets the base file name from a given file path.  If the base name cannot be determined then "" is returned.
     * @param filePath the path (ex: classpath:kew/xml/Foo.xml)
     * @return a File name w/o extension (ex: Foo)
     */
    public static String getBaseXmlFileName(String filePath) {
        final String[] tokens = filePath.replace(".xml", "").split("[\\\\/]");
        return tokens.length > 0 ? tokens[tokens.length - 1] : "";
    }

    protected void loadXmlFile(String fileName) throws Exception {
        Resource resource = new DefaultResourceLoader().getResource(fileName);
        InputStream xmlFile = resource.getInputStream();
        if (xmlFile == null) {
            throw new ConfigurationException("Didn't find file " + fileName);
        }   
        List<XmlDocCollection> xmlFiles = new ArrayList<XmlDocCollection>();
        XmlDocCollection docCollection = getFileXmlDocCollection(xmlFile, "UnitTestTemp." + getBaseXmlFileName(fileName) + ".");
        xmlFiles.add(docCollection);
        XmlIngesterService service = KEWServiceLocator.getXmlIngesterService();
        service.ingest(xmlFiles);
        for (Iterator iterator = docCollection.getXmlDocs().iterator(); iterator.hasNext();) {
            XmlDoc doc = (XmlDoc) iterator.next();
            if (!doc.isProcessed()) {
                throw new RuntimeException("Failed to ingest xml doc: " + doc.getName());
            }
        }
    }

    protected FileXmlDocCollection getFileXmlDocCollection(InputStream xmlFile, String tempFileName) throws IOException {
        if (xmlFile == null) {
            throw new RuntimeException("Didn't find the xml file " + tempFileName);
        }
        File temp = File.createTempFile(tempFileName, ".xml");
        FileOutputStream fos = new FileOutputStream(temp);
        int data = -1;
        while ((data = xmlFile.read()) != -1) {
            fos.write(data);
        }
        fos.close();
        xmlFile.close();
        return new FileXmlDocCollection(temp);
    }

}
