/*
 * Copyright 2006-2008 The Kuali Foundation
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

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.rice.core.config.spring.ConfigFactoryBean;
import org.kuali.rice.core.lifecycle.BaseLifecycle;
import org.kuali.rice.core.lifecycle.Lifecycle;
import org.kuali.rice.core.util.OrmUtils;
import org.kuali.rice.kns.UserSession;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.DocumentService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.MessageMap;
import org.kuali.rice.test.RiceTestCase;
import org.kuali.rice.test.TransactionalLifecycle;
import org.kuali.rice.test.lifecycles.JettyServerLifecycle;
import org.kuali.rice.test.lifecycles.SQLDataLoaderLifecycle;
import org.kuali.rice.test.web.HtmlUnitUtil;

/**
 * This class is the base class for all KCRA Tests requiring a Spring context with persistence, but no preloaded test data
 */
public abstract class KcraNoDataTestBase extends RiceTestCase {

    private static final String CONTEXT_NAME = "/kra-dev";
    private static final String RELATIVE_WEB_ROOT = "/src/main/webapp";
    private static final String SQL_FILE_NAME = "classpath:DefaultTestData.sql";
    private static final String XML_FILE_NAME = "classpath:DefaultTestData.xml";
    private static final String SQL_DELIMETER = ";";
    
    protected TransactionalLifecycle transactionalLifecycle;
    private DocumentService documentService;
    private BusinessObjectService businessObjectService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        documentService = getService(DocumentService.class);
        GlobalVariables.setMessageMap(new MessageMap());
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        transactionalLifecycle = new TransactionalLifecycle();
        transactionalLifecycle.setTransactionManager(KNSServiceLocator.getTransactionManager());
        transactionalLifecycle.start();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        if(transactionalLifecycle != null) {
            transactionalLifecycle.stop();
        }
        GlobalVariables.setMessageMap(new MessageMap());
        GlobalVariables.setUserSession(null);
        super.tearDown();
        documentService = null;
    }
    
    @Override
    protected void loadSuiteTestData() throws Exception {
        new SQLDataLoaderLifecycle(getSqlFilename(), getSqlDelimiter()).start();
    }
    
    protected int getPort() {
        return HtmlUnitUtil.getPort();
    }

    @Override
    protected String getModuleName() {
        return "";
    }
    
    @Override
    protected String getModuleTestConfigLocation() {
        return "classpath:META-INF/kc-test-config.xml";
    }
    
    @Override
    protected Lifecycle getLoadApplicationLifecycle() {
        return new BaseLifecycle() {
            private Lifecycle jetty;
            @Override
            public void start() throws Exception {
                jetty = new JettyServerLifecycle(getPort(), getContextName(), getRelativeWebappRoot());
                jetty.start();
                super.start();
            }
            
            @Override
            public void stop() throws Exception {
                if (jetty != null) {
                    jetty.stop();
                }
                super.stop();
            }
        };  
    }
    
    @Override
    public List<Lifecycle> getSuiteLifecycles() {
        ConfigFactoryBean.CONFIG_OVERRIDE_LOCATION = "classpath:META-INF/kc-test-config.xml";
        List<Lifecycle> lifeCycles= super.getSuiteLifecycles();
        lifeCycles.add(new KraKEWXmlDataLoaderLifecycle());
        return lifeCycles;
    }

    /**
     * This method is overridden because the lifecycle starting error thrown in setUpInternal method can be hidden by an 
     * exception in the tearDown method.
     * @see org.kuali.rice.test.RiceTestCase#startLifecycles(java.util.List)
     */
    @Override
    protected void startLifecycles(List<Lifecycle> lifecycles) throws Exception {
        try {
            super.startLifecycles(lifecycles);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null) {
            businessObjectService = KNSServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
    
    protected DocumentService getDocumentService() {
        if(documentService == null) {
            documentService = KNSServiceLocator.getDocumentService();
        }
        return documentService;
    }

    protected void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    protected Document getDocument(String documentNumber) throws Exception {
        
        // Unfortunately, I can only clear the cache for OJB.  I have been
        // unable to force a refresh on a document when it is in the cache.
        // This is a pain if I need to recheck a document after the database
        // has been changed.  Therefore, for OJB, I clear the cache which
        // will force a new instance of the document to be retrieved from the database
        // instead of the cache.
        if (!OrmUtils.isJpaEnabled()) {
            KNSServiceLocator.getPersistenceServiceOjb().clearCache();
        }
        Document doc=getDocumentService().getByDocumentHeaderId(documentNumber);
        return doc;

    }
    
    /**
     *  Delegate to <code>{@link KraServiceLocator#getService(Class)}</code>
     * @param <T>
     * @param serviceClass class of service to get instance for
     * @return Service instance
     */
    protected final <T> T getService(Class<T> serviceClass) {
        return KraServiceLocator.getService(serviceClass);
    }
    
    protected String getContextName() {
        return CONTEXT_NAME;
    }

    protected String getRelativeWebappRoot() {
        return RELATIVE_WEB_ROOT;
    }
    
    protected String getXmlFilename() {
        return XML_FILE_NAME;
    }
    
    protected String getSqlFilename() {
        return SQL_FILE_NAME;
    }
    
    protected String getSqlDelimiter() {
        return SQL_DELIMETER;
    }
    
    @Override
    protected void configureLogging() throws IOException {
        //do nothing let log4j pick up the default config
    }
}