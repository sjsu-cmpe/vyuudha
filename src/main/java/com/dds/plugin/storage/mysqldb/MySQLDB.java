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

	private static boolean openConnection = false;
	private Connection connect;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	private String driver;
	private String jdbcURL;
	private String user;
	private String password;
	private String database;
	private String table;

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String _driver) {
		driver = _driver;
	}

	/**
	 * @param jdbcURL the jdbcURL to set
	 */
	public void setJdbcURL(String _jdbcURL) {
		jdbcURL = _jdbcURL;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String _user) {
		user = _user;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String _password) {
		password = _password;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String _database) {
		database = _database;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(String _table) {
		table = _table;
	}

	public MySQLDB() {
		this(false);

	}

	public MySQLDB(boolean replicate) {
		Map<String, String> properties;
		if (!replicate) {
			properties = Property.getProperty().getDatabaseProperties();
		} else {
			properties = Property.getProperty().getReplicationProperties();
		}
		setup(properties);
	}

	private void setup(Map<String, String> properties) {	
		setDriver(properties.get("mysql_driver"));
		setJdbcURL(properties.get("mysql_jdbcUrl"));
		setDatabase(properties.get("mysql_database"));
		setTable(properties.get("mysql_store"));
		setUser(properties.get("mysql_user"));
		setPassword(properties.get("mysql_password"));
	}

	/**
	 * Create Database and Table if they don't exist
	 * @throws SQLException 
	 */
	private void initialSetup() throws SQLException {
		String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + database;
		String createTableQuery = "CREATE TABLE IF NOT EXISTS  " + database + "."+ table 
		+"(`KEY` varchar(100) NOT NULL, `VALUE` varchar(500) NOT NULL, `TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`KEY`))"; 
		statement.executeUpdate(createDBQuery);
		statement.executeUpdate(createTableQuery);
	}

	@Override
	public void createConnection() {
		createConnection(false);
	}
	
	public void createConnection(boolean replicate) {
		try {
			if (!openConnection || replicate) {
				Class.forName(driver);
				// Need to read from settings file
				connect = DriverManager
				.getConnection(jdbcURL + "user=" + user + "&password=" + password);
				statement = connect.createStatement();
				initialSetup();
				openConnection = true;
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
		logger.info("MySQL connection opened");
	}

	@Override
	public String get(String key) {
		try {
			resultSet = statement
			.executeQuery("select " + database + "."+ table +".VALUE from " + database 
					+ "."+ table +" where " + database + "."+ table +".KEY = \"" + key 
					+ "\"");

			resultSet.next();

			return resultSet.getString("value");

		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());			
		}
		return "Not Found";
	}

	@Override
	public void put(String key, String value) {
		try {
			if (!contains(key)) {
				preparedStatement = connect
				.prepareStatement("insert into " + database + "."+ table +" values (?, ?, ?)");

				preparedStatement.setString(1, key);
				preparedStatement.setString(2, value);
				preparedStatement.setTimestamp(3, Helper.getCurrentTime());
			} else {
				preparedStatement = connect
				.prepareStatement("update " + database + "."+ table +" set " 
						+ database + "."+ table +".VALUE = ?, " + database 
						+ "."+ table +".TIME = ? where " + database + "."+ table 
						+".KEY =  ? ");

				preparedStatement.setString(1, value);
				preparedStatement.setTimestamp(2, Helper.getCurrentTime());
				preparedStatement.setString(3, key);
			}
			preparedStatement.executeUpdate();	
			logger.info(key + ", " + value + " inserted into MySQL");
			return;
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
		logger.info("Unable to insert");
	}

	@Override
	public void delete(String key) {
		try {
			if (contains(key)) {
				preparedStatement = connect
				.prepareStatement("delete from  " + database + "."+ table +" where " 
						+ database + "."+ table +".KEY = \'" + key + "\'");
				preparedStatement.executeUpdate();	
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	@Override
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
			openConnection = false;
			logger.info("MySQL connection closed");
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	@Override
	public Boolean contains(String key) {
		if (get(key).equals("Not Found")){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void createConnection(String bootstrapUrl) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}

	@Override
	public Object nativeAPI(String... args) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}

	@Override
	public void replicate(String key, String value, int factor)
	throws Exception {
		throw new UnsupportedException("Unsupported method");
	}

	@Override
	public void replicate(String key, String value) throws Exception {
		try {
		logger.info("Replicate function");
		MySQLDB mySQL = new MySQLDB(true);
		mySQL.createConnection(true);
		mySQL.put(key, value);
		//mySQL.closeConnection();
		mySQL = null; 
		} catch (Exception ex) {
			System.out.println("Caught!");
		}
	}
}
