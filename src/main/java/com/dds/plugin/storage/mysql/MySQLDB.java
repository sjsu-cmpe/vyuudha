package com.dds.plugin.storage.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.dds.exception.HandleException;
import com.dds.interfaces.storage.DBInterface;
import com.dds.utils.Helper;

public class MySQLDB implements DBInterface {

	Logger logger = Logger.getLogger(MySQLDB.class);
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Need to read from settings file
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/VYUUDHA?"
							+ "user=root&password=");
		} catch (Exception ex) {
			HandleException.handler(ex.getMessage(), this.getClass().getName(),
					Thread.currentThread().getStackTrace()[2].getMethodName());
		}
		logger.info("MySQL connection opened");
	}

	public String get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void put(String key, String value) {
		try {
			preparedStatement = connect
			.prepareStatement("insert into  VYUUDHA.STORE values (? , ?, ?)");

			preparedStatement.setString(1, key);
			preparedStatement.setString(2, "Value");
			preparedStatement.setTimestamp(3, Helper.getCurrentTime());
			preparedStatement.executeUpdate();	
			
		} catch (Exception ex) {
			HandleException.handler(ex.getMessage(), this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
		}
		logger.info(key + ", " + value + " Inserted into MySQL");
	}

	public void delete(String key) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}
}
