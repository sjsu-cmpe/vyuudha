/**
 * 
 */
package com.dds.utils;

import com.dds.interfaces.APIInterface;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.properties.Property;

import java.lang.String;

import org.apache.log4j.Logger;

/**
 * @author ravid
 *
 */
public class PluginMap<S, T> {

	Logger logger = Logger.getLogger(PluginMap.class);
	/**
	 * @param key
	 * @param api
	 * @param hashing
	 * @param routing
	 * @param membership
	 * @param server
	 */
	public PluginMap() {
		super();
		initialize();

	}

	private APIInterface api;
	private HashingInterface hashing;
	private RoutingInterface routing;
	private MembershipInterface membership;
	private ServerInterface server;

	private void initialize() {
		String db = Property.getProperty().getDatabaseProperties().get("db");
		String routing_type = Property.getProperty().getServerConfigProperties().get("routing_type");
		String server_type = Property.getProperty().getServerConfigProperties().get("server_type");
		String membership_type = Property.getProperty().getServerConfigProperties().get("membership");
		String hashing_type = Property.getProperty().getServerConfigProperties().get("hash_function");

		try {
			add(PluginEnum.API.toString(), instanceOf(db));
			add(PluginEnum.ROUTING.toString(), instanceOf(routing_type));
			add(PluginEnum.SERVER.toString(), instanceOf(server_type));
			add(PluginEnum.MEMBERSHIP.toString(), instanceOf(membership_type));
			add(PluginEnum.HASHING.toString(), instanceOf(hashing_type));
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}	

	public void add(String key, T t) {
		key = key.toUpperCase();
		switch (PluginEnum.valueOf(key.toString()))	{
		case API:
			this.api = (APIInterface)t;
			break;
		case HASHING:
			this.hashing = (HashingInterface)t;
			break;
		case ROUTING:
			this.routing = (RoutingInterface)t;
			break;
		case MEMBERSHIP:
			this.membership = (MembershipInterface)t;
			break;
		case SERVER:
			this.server = (ServerInterface)t;
			break;	
		}
	}

	@SuppressWarnings("unchecked")
	public T get(String key) {
		key = key.toUpperCase();
		switch (PluginEnum.valueOf(key.toString()))	{
		case API:
			return (T)this.api;
		case HASHING:
			return (T)this.hashing;
		case ROUTING:
			return (T)this.routing;
		case MEMBERSHIP:
			return (T)this.membership;
		case SERVER:
			return (T)this.server;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private T instanceOf(String plugin) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Class<?> cls = classLoader.loadClass(plugin);

		Object instance = cls.newInstance();
		return (T)instance;
	}
}
