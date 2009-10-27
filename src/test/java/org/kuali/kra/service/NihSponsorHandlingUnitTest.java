package org.kuali.kra.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.bo.SponsorHierarchy;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.kra.proposaldevelopment.bo.ProposalPersonRole;
import org.kuali.kra.proposaldevelopment.service.KeyPersonnelService;
import org.kuali.kra.service.impl.SponsorServiceImpl;
import org.kuali.kra.service.impl.adapters.BusinessObjectServiceAdapter;
import org.kuali.kra.service.impl.adapters.KeyPersonnelServiceAdapter;
import org.kuali.kra.service.impl.adapters.ParameterServiceAdapter;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.service.BusinessObjectService;
import org.kuali.rice.kns.service.ParameterService;

import java.util.*;

public class NihSponsorHandlingUnitTest {
    private NihSponsorHandlingTestHelper helper;
    private List<ProposalPersonRole> roles;

    private static final String NIH = "NIH";
    private static final String GROUP_HIERARCHY_NAME = "Sponsor Groups";
    private static final String PI_PARM_NAME = "personrole.nih.pi";
    private static final String COI_PARM_NAME = "personrole.nih.coi";
    private static final String KEY_PERSON_PARM_NAME = "personrole.nih.kp";
    private static final String KEY_PERSON_DESCRIPTION = "Key Person";
    private static final String NIH_COI_DESCRIPTION = "PI/Multiple";
    private static final String NIH_PI_DESCRIPTION = "PI/Contact";
    private static final String NONNIH_COI_DESCRIPTION = "Co-Investigator";
    private static final String NONNIH_PI_DESCRIPTION = "Principal Investigator";

    @Before
    public void setUp() {
        roles = defineRoles();
        Sponsorable sponsorable = new Award();
        sponsorable.setSponsorCode(NihSponsorHandlingTestHelper.SPONSOR_CODE_FOR_HIERARCHY_A);
        BusinessObjectService bos = getBusinessObjectService();
        helper = new NihSponsorHandlingTestHelper(sponsorable, bos, getSponsorService(bos), getKeyPersonnelService());
    }

    @After
    public void tearDown() {
        helper = null;
        roles = null;
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_NoLevelValuesSpecified() {
        helper.testIsSponsorNih_DevelopmentProposal_NoLevelValuesSpecified();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_NoLevelWithNIH() {
        helper.testIsSponsorNih_DevelopmentProposal_NoLevelWithNIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level1_NIH_AnyNode() {
        helper.testIsSponsorNih_DevelopmentProposal_NoLevelWithNIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level2_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level2_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level3_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level3_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level4_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level4_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level5_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level5_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level6_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level6_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level7_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level7_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level8_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level7_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level9_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level7_NIH();
    }

    @Test
    public void testIsSponsorNih_DevelopmentProposal_Level10_NIH() {
        helper.testIsSponsorNih_DevelopmentProposal_Level10_NIH();
    }

    @Test
    public void testNIHDescriptionsAssigned() {
        helper.testNihDescriptionsAssigned();
    }


    private List<ProposalPersonRole> defineRoles() {
        List<ProposalPersonRole> roles = new ArrayList<ProposalPersonRole>();
        ProposalPersonRole role = new ProposalPersonRole();
        role.setProposalPersonRoleId("PI");
        role.setDescription(NONNIH_PI_DESCRIPTION);
        roles.add(role);

        role = new ProposalPersonRole();
        role.setProposalPersonRoleId("COI");
        role.setDescription(NONNIH_COI_DESCRIPTION);
        roles.add(role);

        role = new ProposalPersonRole();
        role.setProposalPersonRoleId("KP");
        role.setDescription(KEY_PERSON_DESCRIPTION);
        roles.add(role);
        return roles;
    }

    private BusinessObjectService getBusinessObjectService() {
        final Map<String, SponsorHierarchy> sponsorHierarchies = new HashMap<String, SponsorHierarchy>();

        return new BusinessObjectServiceAdapter() {
            public Collection findAll(Class klass) {
                if(ProposalPersonRole.class.equals(klass)) {
                    return roles;
                } else if(SponsorHierarchy.class.equals(klass)) {
                    return sponsorHierarchies.values();
                } else {
                    return null;
                }
            }
            public Collection findMatching(Class klass, Map fieldValues) {
                return SponsorHierarchy.class.equals(klass) ? sponsorHierarchies.values() : null;
            }
            public void save(PersistableBusinessObject bo) {
                if(bo instanceof SponsorHierarchy) {
                    SponsorHierarchy sh = (SponsorHierarchy) bo;
                    sponsorHierarchies.put(String.format("%s:%s", sh.getSponsorCode(), sh.getHierarchyName()), sh);
                }
            }
        };
    }

    public ParameterService getParameterService() {
        return new ParameterServiceAdapter() {
            public String getParameterValue(String namespaceCode, String detailTypeCode, String parameterName) {
                if(Constants.KC_GENERIC_PARAMETER_NAMESPACE.equals(namespaceCode) &&
                   Constants.KC_ALL_PARAMETER_DETAIL_TYPE_CODE.equals(detailTypeCode)) {

                    if(Constants.SPONSOR_HIERARCHY_NAME.equals(parameterName)) {
                        return GROUP_HIERARCHY_NAME;
                    } else if(Constants.SPONSOR_LEVEL_HIERARCHY.equals(parameterName)) {
                        return NIH;
                    } else if(PI_PARM_NAME.equals(parameterName)) {
                        return NIH_PI_DESCRIPTION;
                    } else if(COI_PARM_NAME.equals(parameterName)) {
                        return NIH_COI_DESCRIPTION;
                    } else if(KEY_PERSON_PARM_NAME.equals(parameterName)) {
                        return KEY_PERSON_DESCRIPTION;
                    } else {
                        return null;
                    }
                } else {
                    return super.getParameterValue(namespaceCode, detailTypeCode, parameterName);
                }
            }
        };
    }

    private KeyPersonnelService getKeyPersonnelService() {
        return new KeyPersonnelServiceAdapter() {
            public Map<String, String> loadKeyPersonnelRoleDescriptions(boolean sponsorIsNih) {
                Map<String, String> results = new HashMap<String, String>();
                if(sponsorIsNih) {
                    results.put("PI", NIH_PI_DESCRIPTION);
                    results.put("COI", NIH_COI_DESCRIPTION);
                    results.put("KP", KEY_PERSON_DESCRIPTION);
                } else {
                    results.put("PI", NONNIH_PI_DESCRIPTION);
                    results.put("COI", NONNIH_COI_DESCRIPTION);
                    results.put("KP", KEY_PERSON_DESCRIPTION);
                }
                return results;
            }
        };
    }

    private SponsorService getSponsorService(BusinessObjectService bos) {
        SponsorServiceImpl impl = new SponsorServiceImpl();
        impl.setBusinessObjectService(bos);
        impl.setParameterService(getParameterService());
        return impl;
    }
}