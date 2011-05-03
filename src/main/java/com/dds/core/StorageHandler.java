/**
 * 
 */
package com.dds.core;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.cluster.Node;
import com.dds.exception.StorageException;
import com.dds.interfaces.APIInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.utils.Helper;
import com.dds.utils.Property;

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
	private static Map<String, String> props = Property.getProperty().getDatabaseProperties();
	private boolean singleInstance = GlobalVariables.INSTANCE.isSingleInstance();

	public StorageHandler() throws UnknownHostException {
		if (dbInterface == null) {
			dbInterface = this.getInstance();
		}
	}

	private void setDbToInstantiate(String dbInstantiate) {
		dbToInstantiate = dbInstantiate;
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

	private void setConfigurations() {
		setCoreStorageInterface(props.get("coreStorageInterface"));
		setDbToInstantiate(props.get("db"));
	}

	private APIInterface getInstance() throws UnknownHostException {
		setConfigurations();
		return GlobalVariables.INSTANCE.getAPI();
	}

	public Object invoke(String buffer) throws Exception {
		return invoke(Helper.getBytes(buffer));
	}

	public Object invoke(byte[] buffer) throws Exception {

		String buf = (String) Helper.getObject(buffer);
		String[] bufArray = buf.split(",");
		String methodName = bufArray[0].trim();

		if (this.coreStorageInterface == null || dbToInstantiate == null) {
			setConfigurations();
		}

		dbInterface.createConnection();
		if (checkMethodExists(methodName)) {
			return invokeMethod(methodName, bufArray);
		} else {
			return invokeNativeMethod(methodName, bufArray);
		} 
	}

	/**
	 * This function is used to invoke native library functions of the db instantiated
	 * 
	 * @param methodName
	 * @param bufArray
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object invokeNativeMethod(String methodName, String[] bufArray) 
	throws Exception {
		int bufArrayLength = bufArray.length;

		String className = dbToInstantiate;

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

			if (!singleInstance) {
				replicateData(bufArray);
			}			
			return "put";
		} else if (methodName.equals("get")) {

			return dbInterface.get(bufArray[1]);
		} else if (methodName.equals("delete")) {

			dbInterface.delete(bufArray[1]);
			return "deleted";
		} else if (methodName.equals("replicate")) {

			replicateData(bufArray);
			return "replicate";
		} else if (methodName.equals("contains")) {

			return Boolean.valueOf(dbInterface.contains(bufArray[1]));
		} else {
			throw new StorageException("No matching method found");
		} 
	}

	private void replicateData(String[] bufArray) throws Exception {
		ReplicationHandler replicationHandler = new ReplicationHandler();
		RoutingInterface routing = GlobalVariables.INSTANCE.getRouting();
		Node nextNode = routing.getNextNode();

		String[] params = null;
		if (bufArray.length == 3) {
			params = new String[]{bufArray[1].trim(), bufArray[2].trim(), null};	
		} else {
			params = new String[]{bufArray[1].trim(), bufArray[2].trim(), bufArray[3].trim()};
		}

		replicationHandler.setNextNodeInfo(nextNode);
		replicationHandler.setDBObject(dbInterface);
		replicationHandler.replicate(params);
	}

	/**
	 * Function simply checks if the method invoked by Client exists in vyuudha core
	 * storage API.
	 * 
	 * @param methodName
	 * @return
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "rawtypes" })
	private boolean checkMethodExists(String methodName) throws ClassNotFoundException {
		String test = methodName;
		Class c = Class.forName(this.coreStorageInterface);
		Method m[] = c.getDeclaredMethods();
		for (int i = 0; i < m.length; i++) {
			if (m[i].getName().equals(test)) {
				return true;
			}
		}
		return false;
	}
}

