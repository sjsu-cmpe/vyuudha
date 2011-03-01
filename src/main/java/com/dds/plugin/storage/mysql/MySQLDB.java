package com.dds.plugin.storage.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.dds.exception.HandleException;
import com.dds.interfaces.storage.DBInterface;
import com.dds.utils.Helper;

/**
 * @author ravid
 *
 */
public class MySQLDB implements DBInterface {

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
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @return the jdbcURL
	 */
	public String getJdbcURL() {
		return jdbcURL;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

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
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
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
		setDriver("com.mysql.jdbc.Driver");
		setJdbcURL("jdbc:mysql://localhost/?");
		setDatabase("VYUUDHA");
		setTable("STORE");
		setUser("root");
		setPassword("");	
	}
	
	/**
	 * Create Database and Table if they don't exist
	 * @throws SQLException 
	 */
	private void initialSetup() throws SQLException {
		String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + getDatabase();
		String createTableQuery = "CREATE TABLE IF NOT EXISTS  " + getDatabase() + "."+ getTable() +"(`KEY` varchar(100) NOT NULL, `VALUE` varchar(500) NOT NULL, `TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`KEY`))"; 
        statement.executeUpdate(createDBQuery);
        statement.executeUpdate(createTableQuery);
	}

	public void createConnection() {
		try {
			Class.forName(getDriver());
			// Need to read from settings file
			connect = DriverManager
			.getConnection(getJdbcURL() + "user=" + getUser() + "&password=" + getPassword());
			statement = connect.createStatement();
			initialSetup();
		} catch (Exception ex) {
			HandleException.handler(ex.getMessage(), this.getClass().getName(),
					Thread.currentThread().getStackTrace()[2].getMethodName());
		}
		logger.info("MySQL connection opened");
	}

	public String get(String key) {
		try {
			resultSet = statement
			.executeQuery("select VYUUDHA.STORE.VALUE from VYUUDHA.STORE where VYUUDHA.STORE.KEY = \"" + key + "\"");

			resultSet.next();

			return resultSet.getString("value");

		} catch (Exception ex) {
			HandleException.handler(ex.getMessage(), this.getClass().getName(),
					Thread.currentThread().getStackTrace()[2].getMethodName());			
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
				.prepareStatement("update VYUUDHA.STORE set VYUUDHA.STORE.VALUE = \"" 
						+ value + "\" where VYUUDHA.STORE.KEY = \"" + key + "\"");
			}
			preparedStatement.executeUpdate();	

		} catch (Exception ex) {
			HandleException.handler(ex.getMessage(), this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
		}
		logger.info(key + ", " + value + " inserted into MySQL");
	}

	public void delete(String key) {
		try {
			if (contains(key)) {
				preparedStatement = connect
				.prepareStatement("delete from  VYUUDHA.STORE where VYUUDHA.STORE.KEY = \'" + key + "\'");
				preparedStatement.executeUpdate();	
			}
		} catch (Exception ex) {
			HandleException.handler(ex.getMessage(), this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
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
		} catch (Exception e) {
			HandleException.handler(e.getMessage(), this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
		}
		logger.info("MySQL connection closed");
	}

	public boolean contains(String key) {
		if (get(key) == null){
			return false;
		} else {
			return true;
		}
	}
}
