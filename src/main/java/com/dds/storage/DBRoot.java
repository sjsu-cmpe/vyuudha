/**
 * 
 */
package com.dds.storage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

import com.dds.utils.Helper;

/**
 * @author ravid
 * 
 */
public class DBRoot {

	private String dbToInstantiate;
	private String coreStorageInterface;
	private String pluginsPath;

	DBRoot() {
		setConfigurations();
	}

	private void setConfigurations() {
		dbToInstantiate = "MongoDB";
		pluginsPath = "com.dds.plugin";
		coreStorageInterface = "com.dds.interfaces.storage.DBInterface";
	}

	public Object invoke(byte[] buffer) throws UnknownHostException, NoSuchMethodException {

		String buf = (String) Helper.getObject(buffer);

		String[] bufArray = buf.split(",");
		String methodName = bufArray[0];

		if (checkMethodExists(methodName)) {
			StringBuilder builder = new StringBuilder(pluginsPath);
			builder.append(".storage." +  dbToInstantiate.toLowerCase() + "." + dbToInstantiate);
			return invokeDBMethod(builder.toString(), methodName);

		} else {
			StringBuilder builder = new StringBuilder(pluginsPath);
			builder.append(".storage." +  dbToInstantiate.toLowerCase() + "." + dbToInstantiate);
			return invokeDBMethod(builder.toString(), methodName);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object invokeDBMethod(String className, String methodName) throws NoSuchMethodException {
		try {
			Class cls = Class.forName(className);
			Object iClass = cls.newInstance();
			Class parameterTypes[] = new Class[0];
			Method method = cls.getMethod(methodName, parameterTypes);
			Object arguments[] = new Object[0];
			Object object = method.invoke(iClass, arguments);
			
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

//	private static void invokeMethod(String name) {
//		try {
//			Class cls = Class.forName("test.DummyClass");
//			Class partypes[] = new Class[2];
//			partypes[0] = Integer.TYPE;
//			partypes[1] = Integer.TYPE;
//			Method meth = cls.getMethod(name, partypes);
//			Method meth2 = cls.getMethod("get", null);
//			MongoDB methobj = new MongoDB();
//			Object arglist[] = new Object[2];
//			arglist[0] = new Integer(37);
//			arglist[1] = new Integer(47);
//			Object retobj = meth.invoke(methobj, arglist);
//			Integer retval = (Integer) retobj;
//			System.out.println(retval.intValue());
//
//			retobj = meth2.invoke(methobj, null);
//			String str = (String) retobj;
//			System.out.println("output : " + str);
//			Method meth3 = cls.getMethod(str, null);
//			retobj = meth3.invoke(methobj, null);
//			str = (String) retobj;
//			System.out.println("output : " + str);
//		} catch (Throwable e) {
//			System.err.println(e);
//		}
//
//	}

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
