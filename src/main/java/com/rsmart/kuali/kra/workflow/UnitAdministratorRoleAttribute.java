package com.rsmart.kuali.kra.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.kuali.kra.bo.UnitAdministrator;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.kra.proposaldevelopment.bo.ProposalPerson;
import org.kuali.kra.service.UnitService;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.GenericRoleAttribute;
import org.kuali.rice.kew.rule.QualifiedRoleName;

/**
 * @author Omar Soto
 * UPRM Special Routing
 */
@SuppressWarnings("unchecked")
public class UnitAdministratorRoleAttribute extends GenericRoleAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2187135270043141363L;

	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(UnitAdministratorRoleAttribute.class);
	private static final String HOMEUNIT = "homeUnit";
	
	// Default Values 
	private boolean getHomeUnit = true;
	private String roleName = "Department Head";
	private String unitAdministratorCode = "3";
	
	UnitService unitService;
	
	public List<String> getQualifiedRoleNames(String roleName,
			DocumentContent documentContent) {
		
		//LOG.info("getQualifiedRoleNames");
		
		// RoleName!Unit Administrator Code!TRUE OF PARENT
		if (roleName != null) {
			String[] info = roleName.split("!");
			//LOG.info("roleName: " + roleName);
			
			if (info.length != 3) {
				LOG.warn("Not enough parameters in the roleName.");
			} else {
				this.roleName = info[0];
				this.unitAdministratorCode = info[1];
				if (info[2].toUpperCase().equals("FALSE")) {
					getHomeUnit = false;
				}
			}
		}
		
		List<String> qualifiedRoleNames = new ArrayList<String>();
		qualifiedRoleNames.add(this.roleName);
	
		return qualifiedRoleNames;
	}
	
	public List<RoleName> getRoleNames() {
		RoleName role = RoleName.Builder.create("org.kuali.kra.workflow.UnitAdministratorRoleAttribute", roleName, roleName).build();
		return Collections.singletonList(role);
	}
	
	@Override
	public Map<String, String> getProperties() {
		// intentionally unimplemented...not intending on using this attribute
		// client-side
		return null;
	}
	
    private UnitService getUnitService() {
    	if (this.unitService == null) {
    		this.unitService = KraServiceLocator.getService(UnitService.class);
    	}
    	
        return unitService;
    }
	
	@Override
	protected List<Id> resolveRecipients(RouteContext routeContext, QualifiedRoleName qualifiedRoleName) {
		List<Id> members = new ArrayList<Id>();
		Collection<Element> personnels = retrieveKeyPersonnel(routeContext);
		String homeUnit;
		Id personId; 
		
		//LOG.info("resolveRecipients");
		//LOG.info("Qualified Role Name: " + qualifiedRoleName);
		
		for(Element keyPerson : personnels) {
			if (getHomeUnit) {
				homeUnit = keyPerson.getChildText(HOMEUNIT);
			} else {
				homeUnit = getUnitService().getUnit(keyPerson.getChildText(HOMEUNIT)).getParentUnit().getUnitNumber();
			}
			
            List<UnitAdministrator> unitAdministrators = getUnitService().retrieveUnitAdministratorsByUnitNumber(homeUnit);
            for ( UnitAdministrator unitAdministrator : unitAdministrators ) {
            	if (StringUtils.isNotBlank(unitAdministrator.getPersonId()) && unitAdministrator.getUnitAdministratorType().getUnitAdministratorTypeCode().equals(unitAdministratorCode)) {
            		personId = new PrincipalId(unitAdministrator.getPersonId());
            		
            		if (!members.contains(personId)) {
            			members.add(personId);
            		}
            	}
            }
		}
		
		return members;
	}
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	private Collection<Element> retrieveKeyPersonnel(RouteContext context) {
	    Document document = XmlHelper.buildJDocument(context.getDocumentContent().getDocument());
	   
	    Collection<Element> personnels = XmlHelper.findElements(document.getRootElement(), ProposalPerson.class.getName());
	    return personnels;
	}
	
}
