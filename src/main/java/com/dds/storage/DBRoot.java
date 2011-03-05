/**
 * 
 */
package com.dds.storage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.properties.Property;
import com.dds.utils.Helper;

/**
 * This class provides the abstraction layer over the Storage calls.
 * All calls to BDB/MongoDB and etc will need to pass thru DBRoot * @author ravid
 * 
 * @author ravid
 *
 */
public class DBRoot {

	private String dbToInstantiate;
	private String coreStorageInterface;
	private String pluginsPath;

	public DBRoot() {
		setConfigurations();
	}

	/**
	 * @param dbToInstantiate the dbToInstantiate to set
	 */
	private void setDbToInstantiate(String dbToInstantiate) {
		this.dbToInstantiate = dbToInstantiate;
	}

	/**
	 * @param coreStorageInterface the coreStorageInterface to set
	 */
	private void setCoreStorageInterface(String coreStorageInterface) {
		this.coreStorageInterface = coreStorageInterface;
	}

	/**
	 * @param pluginsPath the pluginsPath to set
	 */
	private void setPluginsPath(String pluginsPath) {
		this.pluginsPath = pluginsPath;
	}

	/**
	 * Upon object creation of DBRoot, the first thing to do is
	 * set the configurations.
	 * 
	 * These will be read from .properties file.
	 * 
	 * DB to use, pluginsPath and coreInterface variables are
	 * retrieved from properties file
	 * 
	 * @author ravid
	 *
	 */
	private void setConfigurations() {
		Map<String, String> props = Property.getProperty().getDatabaseProperties();
		setDbToInstantiate(props.get("dbToInstantiate"));
		setPluginsPath(props.get("pluginsPath"));
		setCoreStorageInterface(props.get("coreStorageInterface"));
	}

	/**
	 * This function is used to check if the method being called is a native API 
	 * or from our API. 
	 * 
	 * @param buffer
	 * @return Object retrieved from storage layer
	 * @throws UnknownHostException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public Object invoke(byte[] buffer) throws UnknownHostException, NoSuchMethodException, InvocationTargetException {

		String buf = (String) Helper.getObject(buffer);
		String[] bufArray = buf.split(",");
		String methodName = bufArray[0];

		if (checkMethodExists(methodName)) {
			StringBuilder builder = new StringBuilder(pluginsPath);
			builder.append(".storage." +  dbToInstantiate.toLowerCase() + "." + dbToInstantiate);
			return invokeDBMethod(builder.toString(), methodName, bufArray);

		} else {
			StringBuilder builder = new StringBuilder(pluginsPath);
			builder.append(".storage." +  dbToInstantiate.toLowerCase() + "." + dbToInstantiate);
			return invokeDBMethod(builder.toString(), methodName, bufArray);
		}
	}

	/**
	 * This function does the actual hard work of interpreting which storage class to
	 * instantiate and invoke the respective function using reflections.
	 * 
	 * @param className
	 * @param methodName
	 * @param bufArray
	 * @return Object retrieved from storage
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object invokeDBMethod(String className, String methodName, String[] bufArray) 
	throws NoSuchMethodException, InvocationTargetException {
		try {
			int bufArrayLength = bufArray.length;
			Class cls = Class.forName(className);
			Object iClass = cls.newInstance();
			Class parameterTypes[] = new Class[bufArrayLength - 1];
			for (int i = 0; i < bufArrayLength - 1; i++) {
				parameterTypes[i] = String.class;
			}
			Method method = cls.getMethod(methodName, parameterTypes);
			Object arguments[] = new Object[bufArray.length - 1];
			for (int i = 0; i < bufArrayLength - 1; i++) {
				arguments[i] = bufArray[i + 1];
			}
			Method createConMethod = cls.getMethod("createConnection", null);
			createConMethod.invoke(iClass, null);
			Object object = method.invoke(iClass, arguments);
			
			Method closeConMethod = cls.getMethod("closeConnection", null);
			closeConMethod.invoke(iClass, null);
			
			return object;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
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
			Class c = Class.forName(coreStorageInterface);
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
