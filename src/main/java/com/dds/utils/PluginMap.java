/**
 * 
 */
package com.dds.utils;

import com.dds.interfaces.APIInterface;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;

import java.lang.String;

import org.apache.log4j.Logger;

/**
 * @author ravid
 *
 */
public class PluginMap<S, T> {

	private static Logger logger = Logger.getLogger(PluginMap.class);

	private static APIInterface api;
	private static HashingInterface hashing;
	private static RoutingInterface routing;
	private static MembershipInterface membership;
	private static ServerInterface server;

	static {
		String storage_type = Property.getProperty().getServerConfigProperties().get("storage_type");
		String routing_type = Property.getProperty().getServerConfigProperties().get("routing_type");
		String server_type = Property.getProperty().getServerConfigProperties().get("server_type");
		String membership_type = Property.getProperty().getServerConfigProperties().get("membership");
		String hashing_type = Property.getProperty().getServerConfigProperties().get("hash_function");

		try {
			if (api == null) {
				add(PluginEnum.API.toString(), getInstance(storage_type));
			}
			if (routing == null) {
				add(PluginEnum.ROUTING.toString(), getInstance(routing_type));
			}
			if (server == null) {
				add(PluginEnum.SERVER.toString(), getInstance(server_type));
			}
			if (membership == null) {
				add(PluginEnum.MEMBERSHIP.toString(), getInstance(membership_type));
			}
			if (hashing == null) {
				add(PluginEnum.HASHING.toString(), getInstance(hashing_type));
			}
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}	

	private static void add(String key, Object t) {
		key = key.toUpperCase();
		switch (PluginEnum.valueOf(key.toString()))	{
		case API:
			api = (APIInterface)t;
			break;
		case HASHING:
			hashing = (HashingInterface)t;
			break;
		case ROUTING:
			routing = (RoutingInterface)t;
			break;
		case MEMBERSHIP:
			membership = (MembershipInterface)t;
			break;
		case SERVER:
			server = (ServerInterface)t;
			break;	
		}
	}

	@SuppressWarnings("unchecked")
	public T get(String key) {
		key = key.toUpperCase();
		switch (PluginEnum.valueOf(key.toString()))	{
		case API:
			return (T)api;
		case HASHING:
			return (T)hashing;
		case ROUTING:
			return (T)routing;
		case MEMBERSHIP:
			return (T)membership;
		case SERVER:
			return (T)server;
		}
		return null;
	}

	private static Object getInstance(String plugin) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Class<?> cls = classLoader.loadClass(plugin);

		Object instance = cls.newInstance();
		return instance;
	}
}
