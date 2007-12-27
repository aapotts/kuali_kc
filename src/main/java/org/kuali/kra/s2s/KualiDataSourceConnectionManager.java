/**
 * 
 */
package org.kuali.kra.s2s;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.kuali.kra.infrastructure.KraServiceLocator;
import org.kuali.rice.database.XAPoolDataSource;
import org.kuali.rice.test.SQLDataLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import edu.mit.coeus.utils.dbengine.DBConnection;

/**
 * @author geo
 *
 */
public class KualiDataSourceConnectionManager implements DBConnection {
    private static final String DATA_SOURCE = "dataSource";
    private static final Logger LOG = Logger.getLogger(KualiDataSourceConnectionManager.class);
	
	/* (non-Javadoc)
	 * @see edu.mit.coeus.utils.dbengine.DBConnection#freeConnection(java.lang.String, java.sql.Connection)
	 */
	public void freeDatabaseConnection(Connection conn)
			throws SQLException {
	    try{
	        if(conn!=null){
	            conn.close();
	        }
	    }catch(Throwable ex){
	        LOG.error(ex);
	    }
	}

	/* (non-Javadoc)
	 * @see edu.mit.coeus.utils.dbengine.DBConnection#getConnection(java.lang.String)
	 */
	public Connection getDatabaseConnection() throws SQLException {
	    DataSource ds = (DataSource)KraServiceLocator.getService(DATA_SOURCE);
		return ds==null?null:ds.getConnection();
	}
}
