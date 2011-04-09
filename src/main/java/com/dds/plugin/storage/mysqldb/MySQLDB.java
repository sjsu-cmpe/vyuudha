package com.dds.plugin.storage.mysqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;
import com.dds.utils.Property;
import com.dds.utils.Helper;

/**
 * @author ravid
 *
 */
public class MySQLDB implements APIInterface {

	Logger logger = Logger.getLogger(MySQLDB.class);

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private String driver;
	private String jdbcURL;
	private String user;
	private String password;
	private String database;
	private String table;

    /**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @param jdbcURL the jdbcURL to set
	 */
	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	public MySQLDB() {
		Map<String, String> props = Property.getProperty().getDatabaseProperties();
		setDriver(props.get("mysql_driver"));
		setJdbcURL(props.get("mysql_jdbcUrl"));
		setDatabase(props.get("mysql_database"));
		setTable(props.get("mysql_store"));
		setUser(props.get("mysql_user"));
		setPassword(props.get("mysql_password"));	
	}
	
	/**
	 * Create Database and Table if they don't exist
	 * @throws SQLException 
	 */
	private void initialSetup() throws SQLException {
		String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + this.database;
		String createTableQuery = "CREATE TABLE IF NOT EXISTS  " + this.database + "."+ this.table +"(`KEY` varchar(100) NOT NULL, `VALUE` varchar(500) NOT NULL, `TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`KEY`))"; 
        statement.executeUpdate(createDBQuery);
        statement.executeUpdate(createTableQuery);
	}

	public void createConnection() {
		try {
			Class.forName(this.driver);
			// Need to read from settings file
			connect = DriverManager
			.getConnection(this.jdbcURL + "user=" + this.user + "&password=" + this.password);
			statement = connect.createStatement();
			initialSetup();
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
		logger.info("MySQL connection opened");
	}

	public String get(String key) {
		try {
			resultSet = statement
			.executeQuery("select VYUUDHA.STORE.VALUE from VYUUDHA.STORE where VYUUDHA.STORE.KEY = \"" + key + "\"");

			resultSet.next();

			return resultSet.getString("value");

		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());			
		}
		return null;
	}

	public void put(String key, String value) {
		try {
			if (!contains(key)) {
				preparedStatement = connect
				.prepareStatement("insert into VYUUDHA.STORE values (?, ?, ?)");

				preparedStatement.setString(1, key);
				preparedStatement.setString(2, value);
				preparedStatement.setTimestamp(3, Helper.getCurrentTime());
			} else {
				preparedStatement = connect
				.prepareStatement("update VYUUDHA.STORE set VYUUDHA.STORE.VALUE = ?, VYUUDHA.STORE.TIME = ? where VYUUDHA.STORE.KEY =  ? ");

				preparedStatement.setString(1, value);
				preparedStatement.setTimestamp(2, Helper.getCurrentTime());
				preparedStatement.setString(3, key);
			}
			preparedStatement.executeUpdate();	
			logger.info(key + ", " + value + " inserted into MySQL");
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	public void delete(String key) {
		try {
			if (contains(key)) {
				preparedStatement = connect
				.prepareStatement("delete from  VYUUDHA.STORE where VYUUDHA.STORE.KEY = \'" + key + "\'");
				preparedStatement.executeUpdate();	
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	public void closeConnection() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
			logger.info("MySQL connection closed");
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	public Boolean contains(String key) {
		if (get(key) == null){
			return false;
		} else {
			return true;
		}
	}

	public void createConnection(String bootstrapUrl) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}
	
	public Object nativeAPI(String... args) throws Exception {
		System.out.println("Unsupported");
		return null;
		//throw new UnsupportedException("Unsupported method");
	}

	@Override
	public void replicate(String key, String value, int factor)
			throws Exception {
		throw new UnsupportedException("Unsupported Method");		
	}

	@Override
	public void replicate(String key, String value) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
