/**
 * 
 */
package com.dds.storage;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.core.GlobalVariables;
import com.dds.exception.StorageException;
import com.dds.interfaces.APIInterface;
import com.dds.properties.Property;
import com.dds.replication.ReplicationHandler;
import com.dds.utils.Helper;

/**
 * This class provides the abstraction layer over the Storage calls.
 * All calls to BDB/MongoDB and etc will need to pass thru DBRoot 
 * 
 * @author ravid
 *
 */
public class StorageHandler {

	Logger logger = Logger.getLogger(StorageHandler.class);
	
	private static APIInterface dbInterface;
	private String dbToInstantiate;
	private String coreStorageInterface;
	private String pluginsPath;
	private static Map<String, String> props = Property.getProperty().getDatabaseProperties();

	public StorageHandler() {
		if (dbInterface == null) {
			try {
				dbInterface = this.getInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setDbToInstantiate(String dbToInstantiate) {
		this.dbToInstantiate = dbToInstantiate;
	}

	/**
	 * @param coreStorageInterface the coreStorageInterface to set
	 */
	private void setCoreStorageInterface(String coreStorageInterface) {
		this.coreStorageInterface = coreStorageInterface;
	}

	public String getCoreStorageInterface() {
		return this.coreStorageInterface;
	}

	/**
	 * @param pluginsPath the pluginsPath to set
	 */
	private void setPluginsPath(String pluginsPath) {
		this.pluginsPath = pluginsPath;
	}

	private void setConfigurations() {
		setPluginsPath(props.get("pluginsPath"));
		setCoreStorageInterface(props.get("coreStorageInterface"));
		setDbToInstantiate(props.get("dbToInstantiate"));
	}

	private APIInterface getInstance() throws UnknownHostException {
		setConfigurations();
		
		APIInterface retVal = (APIInterface)GlobalVariables.INSTANCE.map.get("API");
		return retVal;
	}
	
	public Object invoke(String buffer) throws Exception {
		return invoke(Helper.getBytes(buffer));
	}

	public Object invoke(byte[] buffer) throws Exception {
		
		ReplicationHandler replicationHandler = new ReplicationHandler();
		replicationHandler.invoke(buffer, Integer.parseInt(props.get("writes")));
		
		String buf = (String) Helper.getObject(buffer);
		String[] bufArray = buf.split(",");
		String methodName = bufArray[0].trim();
		
		if (this.coreStorageInterface == null || this.dbToInstantiate == null 
				|| this.pluginsPath == null) {
			setConfigurations();
		}
		try {
			dbInterface.createConnection();
			if (checkMethodExists(methodName)) {
				return invokeMethod(methodName, bufArray);
			} else {
				return invokeNativeMethod(methodName, bufArray);
			} 
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		} finally {
			dbInterface.closeConnection();
		}
		return null;
	}

	/**
	 * This function is used to invoke native library functions of the db instantiated
	 * 
	 * @param methodName
	 * @param bufArray
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object invokeNativeMethod(String methodName, String[] bufArray) {
		try {
			int bufArrayLength = bufArray.length;
			StringBuilder builder = new StringBuilder(pluginsPath);
			builder.append(".storage." +  dbToInstantiate.toLowerCase() + "." + dbToInstantiate);
			String className = builder.toString();
			
			Class cls = Class.forName(className);
			Class parameterTypes[] = new Class[bufArrayLength - 1];
			for (int i = 0; i < bufArrayLength - 1; i++) {
				parameterTypes[i] = String.class;
			}
			Method method = cls.getMethod(methodName, parameterTypes);
			Object arguments[] = new Object[bufArray.length - 1];
			for (int i = 0; i < bufArrayLength - 1; i++) {
				arguments[i] = bufArray[i + 1];
			}
			Method createConMethod = cls.getMethod("createConnection", (Class[])null);
			createConMethod.invoke((Object)dbInterface, (Object[])null);
			Object object = method.invoke((Object)dbInterface, arguments);
			
			Method closeConMethod = cls.getMethod("closeConnection", (Class[])null);
			closeConMethod.invoke(dbInterface, (Object[])null);
			
			return object;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * This function is used to invoke a function defined in DBInterface
	 * 
	 * @param methodName
	 * @param bufArray
	 * @return
	 * @throws Exception 
	 */
	private Object invokeMethod(String methodName, String[] bufArray) throws Exception {
		if (methodName.equals("put")) {
			dbInterface.put(bufArray[1].trim(), bufArray[2].trim());
			return "put";
		} else if (methodName.equals("get")) {
			return dbInterface.get(bufArray[1]);
		} else if (methodName.equals("delete")) {
			dbInterface.delete(bufArray[1]);
			return "deleted";
		} else if (methodName.equals("contains")) {
			return Boolean.valueOf(dbInterface.contains(bufArray[1]));
		} else {
			throw new StorageException("No matching method found");
		} 
	}

	/**
	 * Function simply checks if the method invoked by Client exists in vyuudha core
	 * storage API.
	 * 
	 * @param methodName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private boolean checkMethodExists(String methodName) {
		String test = methodName;
		try {
			Class c = Class.forName(this.coreStorageInterface);
			Method m[] = c.getDeclaredMethods();
			for (int i = 0; i < m.length; i++) {
				if (m[i].getName().equals(test)) {
					return true;
				}
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
		return false;
	}

}

