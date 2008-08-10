/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kra.dao.ojb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.core.dao.ojb.PlatformAwareDaoBaseOjb;
import org.kuali.core.util.OjbCollectionAware;
import org.kuali.kra.bo.SponsorHierarchy;
import org.kuali.kra.dao.SponsorHierarchyDao;
import org.kuali.kra.infrastructure.Constants;
import org.kuali.rice.exceptions.RiceRuntimeException;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;

public class SponsorHierarchyDaoOjb extends PlatformAwareDaoBaseOjb implements OjbCollectionAware, SponsorHierarchyDao {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory
    .getLog(SponsorHierarchyDaoOjb.class);
    private static final String ERROR_MSG = "Error get sponsor codes.";
    private static final String SPONSOR_CODE_NAME_SQL = "select sponsor_code, (select sponsor_name from sponsor where sponsor_code = sponsor_hierarchy.sponsor_code) from sponsor_hierarchy ";


    public Iterator getTopSponsorHierarchy() {
        
      Criteria criteriaID = new Criteria();
      ReportQueryByCriteria queryID = new ReportQueryByCriteria(SponsorHierarchy.class,criteriaID);
      queryID.setAttributes(new String[] {Constants.HIERARCHY_NAME});
      queryID.setDistinct(true);
      
      return getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(queryID);
      
  }
    
    /**
     * This is much faster than use 'businessobjectservice.findmatching, and then loop thru bo.
     * @see org.kuali.kra.dao.SponsorHierarchyDao#getAllSponsors(java.lang.String)
     */
    public Iterator getAllSponsors(String hierarchyName) {
        
      Criteria criteriaID = new Criteria();
      criteriaID.addEqualTo(Constants.HIERARCHY_NAME, hierarchyName);
      ReportQueryByCriteria queryID = new ReportQueryByCriteria(SponsorHierarchy.class,criteriaID);
      queryID.setAttributes(new String[] {Constants.SPONSOR_CODE});
      
      return getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(queryID);
      
  }
    
    /**
     * 
     * @see org.kuali.kra.dao.SponsorHierarchyDao#getSponsorCodesForGroup(java.lang.String, int, java.lang.String[])
     */
    public String getSponsorCodesForGroup(String hierarchyName, int level, String[] levelName) {
        

        String whereSt = "where hierarchy_name = '"+hierarchyName+"'";
        for (int i = 1; i< level; i++) {
            whereSt = whereSt + " and level"+i+" = '" +levelName[i-1]+"'";
        }

        String sql =    SPONSOR_CODE_NAME_SQL+whereSt+" order by sponsor_code";
        final String selectsql = sql;
        return (String)getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                try {
                    String retStr=Constants.EMPTY_STRING;
                    Connection connection = broker.serviceConnectionManager().getConnection();
                    statement = connection.prepareStatement(selectsql);
                    resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            if (StringUtils.isBlank(retStr)) {
                                retStr = resultSet.getString(1)+":"+resultSet.getString(2);
                               
                            } else {
                                retStr = retStr+Constants.SPONSOR_HIERARCHY_SEPARATOR_C1C+resultSet.getString(1)+":"+resultSet.getString(2);
                            }
                        }
                                           
                    return retStr;
                } catch (Exception e) {
                    LOG.info(e.getStackTrace());
                    throw new RiceRuntimeException(ERROR_MSG, e);
                }  finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {}
                    }
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) 
                        {
                            LOG.info(e.getStackTrace());                           
                        }
                    }
                }
            }
        });
    }
    
    
    /**
     * This much faster then 'businessobjectservice.findmatching'
     * @see org.kuali.kra.dao.SponsorHierarchyDao#getsubGroups(java.lang.String, int, java.lang.String[])
     */
    public String getsubGroups(String hierarchyName, int level, String[] levelName) {
        String whereSt = "where hierarchy_name = '"+hierarchyName+"'";
        for (int i = 1; i< level; i++) {
            whereSt = whereSt + " and level"+i+" = '" +levelName[i-1]+"'";
        }

        String sql ="select unique level"+level+", level"+level+"_sortid from sponsor_hierarchy "+whereSt+" order by level"+level+"_sortid";
        final String selectsql = sql;
        return (String)getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                try {
                    String retStr=Constants.EMPTY_STRING;
                    Connection connection = broker.serviceConnectionManager().getConnection();
                    statement = connection.prepareStatement(selectsql);
                    resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            if (StringUtils.isBlank(retStr)) {
                                retStr = resultSet.getString(1);
                               
                            } else {
                                retStr = retStr+Constants.SPONSOR_HIERARCHY_SEPARATOR_C1C+resultSet.getString(1);
                            }
                        }
                        
                    
                    return retStr;
                } catch (Exception e) {
                    LOG.info(e.getStackTrace());
                    throw new RiceRuntimeException(ERROR_MSG, e);
                } finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException e) {}
                    }
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {}
                    }
                }
            }
        });
    }
    
    /**
     * 
     * @see org.kuali.kra.dao.SponsorHierarchyDao#runScripts(java.lang.String[])
     */
    public void runScripts(final String[] sqls) {
        
        this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker pb) {
                PreparedStatement ps = null;
                
                try {
                    // use batch ?
                    for (int i = 0 ; i < sqls.length; i++) {
                        ps = pb.serviceConnectionManager().getConnection().prepareStatement(sqls[i]);
                        ps.executeUpdate();
                    }
                    //pb.commitTransaction();
                } catch (Exception e) {
                    LOG.error("exception error " +e.getStackTrace());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            LOG.error("error closing preparedstatement", e);
                        }
                    }
                }
                return null;
            }
        });

        }

}
