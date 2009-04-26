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
package org.kuali.kra.kim.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.kra.kim.bo.KimPersonQualifiedRoleAttribute;
import org.kuali.kra.kim.bo.KimQualifiedRolePerson;
import org.kuali.kra.kim.bo.KimRolePermission;
import org.kuali.rice.kns.dao.impl.PlatformAwareDaoBaseOjb;

/**
 * The KIM DAO implementation.  Uses OJB.
 */
public class KimDaoImpl extends PlatformAwareDaoBaseOjb implements KimDao {

    /**
     * @see org.kuali.kra.kim.service.impl.KimDao#getPersonQualifiedRoles(java.lang.Long, java.util.Map)
     */
    public Collection<KimQualifiedRolePerson> getPersonQualifiedRoles(Long personId, Map<String, String> qualifiedRoleAttributes) {

        Entry<String, String> entry = qualifiedRoleAttributes.entrySet().iterator().next();
        String attrName = entry.getKey();
        String attrValue = entry.getValue();

        ReportQueryByCriteria subQuery;
        Criteria subCrit = new Criteria();
        Criteria crit = new Criteria();

        subCrit.addEqualToField("rolePersonId", Criteria.PARENT_QUERY_PREFIX + "id");
        subCrit.addEqualTo("attributeName", attrName);
        subCrit.addEqualTo("attributeValue", attrValue);
        subQuery = QueryFactory.newReportQuery(KimPersonQualifiedRoleAttribute.class, subCrit);

        crit.addExists(subQuery);
        crit.addEqualTo("personId", personId);
        Query q = QueryFactory.newQuery(KimQualifiedRolePerson.class, crit);
        
        return getPersistenceBrokerTemplate().getCollectionByQuery(q);
    }
    
    /**
     * @see org.kuali.kra.kim.service.impl.KimDao#hasPermission(java.util.Collection, java.lang.Long)
     */
    public boolean hasPermission(Collection<Long> roleIds, Long permissionId) {

        boolean hasPermission = false;
        if (roleIds.size() > 0) {
            Criteria crit = new Criteria();
            crit.addIn("roleId", roleIds);
            crit.addEqualTo("permissionId", permissionId);
            Query q = QueryFactory.newQuery(KimRolePermission.class, crit);
    
            hasPermission = getPersistenceBrokerTemplate().getCount(q) > 0;
        }
        return hasPermission;
    }
}
