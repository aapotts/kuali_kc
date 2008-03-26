/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.budget.web.struts.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.kuali.core.service.DataDictionaryService;
import org.kuali.core.service.KualiConfigurationService;
import org.kuali.core.web.ui.ExtraButton;
import org.kuali.kra.budget.bo.BudgetCostShare;
import org.kuali.kra.budget.bo.BudgetPeriod;
import org.kuali.kra.budget.bo.BudgetProjectIncome;
import org.kuali.kra.budget.bo.BudgetUnrecoveredFandA;
import org.kuali.kra.budget.document.BudgetDocument;
import org.kuali.kra.budget.document.BudgetDocumentContainer;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.web.struts.form.ProposalFormBase;

public class BudgetForm extends ProposalFormBase implements BudgetDocumentContainer {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(BudgetForm.class);
    
    private String newBudgetPersons;
    private String newBudgetRolodexes;
    private String newTbnPersons;
    
    private BudgetPeriod newBudgetPeriod;
    
    private Integer newBudgetPeriodNumber = 0; 
    
    private BudgetCostShare newBudgetCostShare;
    private BudgetProjectIncome newBudgetProjectIncome;
    private BudgetUnrecoveredFandA newBudgetUnrecoveredFandA;
    
    private List<ExtraButton> extraTopButtons;

    private Integer viewBudgetPeriod;
    private String viewLocation;
    
    public Integer getViewBudgetPeriod() {
        return viewBudgetPeriod;
    }

    public void setViewBudgetPeriod(Integer viewBudgetPeriod) {
        this.viewBudgetPeriod = viewBudgetPeriod;
    }

    public String getViewLocation() {
        return viewLocation;
    }

    public void setViewLocation(String viewLocation) {
        this.viewLocation = viewLocation;
    }

    public BudgetForm() {
        super();
        this.setDocument(new BudgetDocument());
        initialize();
    }

    /**
     * 
     * This method initialize all form variables
     */
    public void initialize() {
        DataDictionaryService dataDictionaryService = (DataDictionaryService) KraServiceLocator.getService(Constants.DATA_DICTIONARY_SERVICE_NAME);
        this.setHeaderNavigationTabs((dataDictionaryService.getDataDictionary().getDocumentEntry(org.kuali.kra.budget.document.BudgetDocument.class.getName())).getHeaderTabNavigation());
        setNewBudgetPeriod(new BudgetPeriod());
        setExtraTopButtons(new ArrayList<ExtraButton>());
        ExtraButton returnToProposal = new ExtraButton();
        returnToProposal.setExtraButtonProperty("methodToCall.returnToProposal");
        KualiConfigurationService configService = KraServiceLocator.getService(KualiConfigurationService.class);
        String imagesUrl = configService.getPropertyString("kra.externalizable.images.url");
        returnToProposal.setExtraButtonSource(imagesUrl + "tinybutton-retprop.gif");
        returnToProposal.setExtraButtonAltText("return to proposal");
        extraTopButtons.add(returnToProposal);
        
        newBudgetProjectIncome = new BudgetProjectIncome();
        newBudgetCostShare = new BudgetCostShare();
        newBudgetUnrecoveredFandA = new BudgetUnrecoveredFandA();
    }
    
    public BudgetDocument getBudgetDocument() {
        return (BudgetDocument) this.getDocument();
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        // if there are more ...
        for(Object displayedErrorsKey: getDisplayedErrors().keySet()) {
            getDisplayedErrors().put(displayedErrorsKey, false);
        }
    }

    public BudgetPeriod getNewBudgetPeriod() {
        return newBudgetPeriod;
    }

    public void setNewBudgetPeriod(BudgetPeriod newBudgetPeriod) {
        Integer budgetPeriod = 1;
        if(getBudgetDocument().getBudgetPeriods() != null) {
            budgetPeriod = getBudgetDocument().getBudgetPeriods().size() + 1;
        }
        newBudgetPeriod.setBudgetPeriod(budgetPeriod);
        this.newBudgetPeriod = newBudgetPeriod;
    }

    @Override
    public List<ExtraButton> getExtraButtons() {
        // clear out the extra buttons array
        extraButtons.clear();
        String externalImageURL = "kra.externalizable.images.url";
        String generatePeriodImage = KraServiceLocator.getService(KualiConfigurationService.class).getPropertyString(externalImageURL) + "buttonsmall_generatePeriods.gif"; 
        String calculatePeriodImage = KraServiceLocator.getService(KualiConfigurationService.class).getPropertyString(externalImageURL) + "buttonsmall_calculatePeriods.gif"; 
        String appExternalImageURL = "ConfigProperties.kra.externalizable.images.url"; 
        addExtraButton("methodToCall.generateAllPeriods", generatePeriodImage, "Generate All Periods");
        addExtraButton("methodToCall.calculateAllPeriods",calculatePeriodImage, "Calculate All Periods");
        
        return extraButtons;
    }

    public List<ExtraButton> getRatesExtraButtons() {
        // clear out the extra buttons array
        extraButtons.clear();
        String externalImageURL = "kra.externalizable.images.url";
        String syncAllImage = KraServiceLocator.getService(KualiConfigurationService.class).getPropertyString(externalImageURL) + "buttonsmall_syncallrates.gif"; 
        String resetAllImage = KraServiceLocator.getService(KualiConfigurationService.class).getPropertyString(externalImageURL) + "buttonsmall_resetallrates.gif"; 
        String appExternalImageURL = "ConfigProperties.kra.externalizable.images.url"; 
        addExtraButton("methodToCall.syncAllRates", syncAllImage, "Sync All Rates");
        addExtraButton("methodToCall.resetAllRates",resetAllImage, "Reset All Rates");
        
        return extraButtons;
    }

    /**
     * This is a utility method to add a new button to the extra buttons
     * collection.
     *   
     * @param property
     * @param source
     * @param altText
     */ 
    protected void addExtraButton(String property, String source, String altText){
        
        ExtraButton newButton = new ExtraButton();
        
        newButton.setExtraButtonProperty(property);
        newButton.setExtraButtonSource(source);
        newButton.setExtraButtonAltText(altText);
        
        extraButtons.add(newButton);
    }

    public List<ExtraButton> getExtraTopButtons() {
        return extraTopButtons;
    }

    public boolean isCostSharingEditFormVisible() {
        BudgetDocument budgetDocument = getBudgetDocument();        
        return budgetDocument != null && budgetDocument.isCostSharingApplicable() && budgetDocument.isCostSharingAvailable(); 
    }
    
    public boolean isUnrecoveredFandAEditFormVisible() {
        BudgetDocument budgetDocument = getBudgetDocument(); 
        return budgetDocument != null && budgetDocument.isUnrecoveredFandAApplicable() && budgetDocument.isCostSharingAvailable(); 
    }
    
    public void setExtraTopButtons(List<ExtraButton> extraTopButtons) {
        this.extraTopButtons = extraTopButtons;
    }

    public String getNewBudgetPersons() {
        return newBudgetPersons;
    }

    public void setNewBudgetPersons(String newBudgetPersons) {
        this.newBudgetPersons = newBudgetPersons;
    }

    public String getNewBudgetRolodexes() {
        return newBudgetRolodexes;
    }

    public BudgetProjectIncome getNewBudgetProjectIncome() {
        return newBudgetProjectIncome;
    }

    public Integer getNewBudgetPeriodNumber() {
        return newBudgetPeriodNumber;
    }

    public void setNewBudgetPeriodNumber(Integer newBudgetPeriodNo) {
        this.newBudgetPeriodNumber = newBudgetPeriodNo;
    }

    public void setNewBudgetProjectIncome(BudgetProjectIncome newBudgetProjectIncome) {
        this.newBudgetProjectIncome = newBudgetProjectIncome;
    }

    public void setNewBudgetRolodexes(String newBudgetRolodexes) {
        this.newBudgetRolodexes = newBudgetRolodexes; 
    }
    
    public String getNewTbnPersons() {
        return newTbnPersons;
    }

    public void setNewTbnPersons(String newTbnPersons) {
        this.newTbnPersons = newTbnPersons;
    }

    /**
     * 
     * This method to suppress copy/reload buttons for 'Totals' page
     */
    public void suppressButtonsForTotalPage() {
        this.getDocumentActionFlags().setCanCopy(false);
        this.getDocumentActionFlags().setCanReload(false);
    }

    public BudgetCostShare getNewBudgetCostShare() {
        return newBudgetCostShare;
    }

    public void setNewBudgetCostShare(BudgetCostShare newBudgetCostShare) {
        this.newBudgetCostShare = newBudgetCostShare;
    }
    
    /**
     * Get the Header Dispatch.  This determines the action that will occur
     * when the user switches tabs for a budget.  If the user can modify
     * the budget, the budget is automatically saved.  If not (view-only),
     * then a reload will be executed instead.
     * @return the Header Dispatch action
     */
    public String getHeaderDispatch() {
        return this.getDocumentActionFlags().getCanSave() ? "save" : "reload";
    }

    public BudgetUnrecoveredFandA getNewBudgetUnrecoveredFandA() {
        return newBudgetUnrecoveredFandA;
    }

    public void setNewBudgetUnrecoveredFandA(BudgetUnrecoveredFandA newBudgetUnrecoveredFandA) {
        this.newBudgetUnrecoveredFandA = newBudgetUnrecoveredFandA;
    }
}
