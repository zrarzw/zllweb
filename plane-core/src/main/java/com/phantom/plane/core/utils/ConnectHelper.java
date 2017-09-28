/**
 * Project:				EasyWeb
 * Author:				赵志武
 * Company: 			杭州中软
 * Created Date:		2011-10-09
 * Description:			
 * Copyright @ 2011 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phantom.plane.core.interfacer.IBaseZrarDao;

/**
 * 
 * 数据库连接帮助类
 * 
 */
public class ConnectHelper {

	private static Logger log = LoggerFactory.getLogger(ConnectHelper.class);
	private static Map<String, Context> connectMap = new HashMap<String, Context>();
	private static Context context = null;
	private Properties databaseTypeMappings;

	protected static Properties getDefaultDatabaseTypeMappings() {
		Properties databaseTypeMappings = new Properties();
		databaseTypeMappings.setProperty("H2", "h2");
		databaseTypeMappings.setProperty("MySQL", "mysql");
		databaseTypeMappings.setProperty("Oracle", "oracle");
		databaseTypeMappings.setProperty("PostgreSQL", "postgres");
		databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
		databaseTypeMappings.setProperty("DB2", "db2");
		databaseTypeMappings.setProperty("DB2", "db2");
		databaseTypeMappings.setProperty("DB2/NT", "db2");
		databaseTypeMappings.setProperty("DB2/NT64", "db2");
		databaseTypeMappings.setProperty("DB2 UDP", "db2");
		databaseTypeMappings.setProperty("DB2/LINUX", "db2");
		databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
		databaseTypeMappings.setProperty("DB2/LINUXX8664", "db2");
		databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
		databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
		databaseTypeMappings.setProperty("DB2/6000", "db2");
		databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
		databaseTypeMappings.setProperty("DB2/AIX64", "db2");
		databaseTypeMappings.setProperty("DB2/HPUX", "db2");
		databaseTypeMappings.setProperty("DB2/HP64", "db2");
		databaseTypeMappings.setProperty("DB2/SUN", "db2");
		databaseTypeMappings.setProperty("DB2/SUN64", "db2");
		databaseTypeMappings.setProperty("DB2/PTX", "db2");
		databaseTypeMappings.setProperty("DB2/2", "db2");
		databaseTypeMappings.setProperty("DM DBMS", "dm");
		databaseTypeMappings.setProperty("idb", "intple");
		databaseTypeMappings.setProperty("IDB", "intple");
		return databaseTypeMappings;
	}

	/**
	 * 实例化方法
	 * 
	 * @return
	 */
	public static ConnectHelper getInstance() {
		return ConnectHelper.ApplicationContextHodler.connector;
	}

	/**
	 * 获取数据库连接（JNDI方式）
	 * 
	 * @param contextFactory
	 * @param providerUrl
	 * @param jndiName
	 * @return
	 */
	public static Connection getJNDIConnnection(String contextFactory,
			String providerUrl, String jndiName) {
		String INITIAL_CONTEXT_FACTORY = contextFactory;
		String PROVIDER_URL = providerUrl;
		String JNDI_NAME = jndiName;

		Connection conn = null;
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			ht.put(Context.PROVIDER_URL, PROVIDER_URL);

			context = new InitialContext(ht);
			connectMap.put("default", context);

			javax.sql.DataSource ds = (DataSource) context.lookup(JNDI_NAME);
			conn = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取数据库连接（JDBC方式）
	 * 
	 * @param driver
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 */
	public static Connection getJdbcConnection(String driver, String url,
			String userName, String password) {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取数据库连接接口
	 * 
	 * @return
	 */
	public IBaseZrarDao getBaseZrarDao() {
		return getBaseZrarDao("dao");
	}

	/**
	 * 获取数据库连接接口(指定数据源)
	 * 
	 * @param datasource
	 * @return
	 */
	public IBaseZrarDao getBaseZrarDao(String datasource) {
		Object bean = SpringContext.getContext().getBean(datasource);
		if (bean != null) {
			return (IBaseZrarDao) bean;
		}
		return null;
	}

	public String getDatabaseType(Connection conn) throws ConfigException {
		Connection connection = conn;
		String databaseType = "";
		try {
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			String databaseProductName = databaseMetaData
					.getDatabaseProductName();
			log.debug("数据库产品名称: '{}'", databaseProductName);
			databaseType = databaseTypeMappings
					.getProperty(databaseProductName);
			if (databaseType == null) {
				throw new ConfigException("暂不识别数据库产品类型 '" + databaseProductName
						+ "'");
			}
			log.debug("数据库类型为: {}", databaseType);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return databaseType;
	}

	private static class ApplicationContextHodler {
		private static ConnectHelper connector = new ConnectHelper();
	}

	private ConnectHelper() {
		this.databaseTypeMappings = ConnectHelper
				.getDefaultDatabaseTypeMappings();
	}

}
