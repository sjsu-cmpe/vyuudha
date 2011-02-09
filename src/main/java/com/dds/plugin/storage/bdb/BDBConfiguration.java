/**
 * 
 */
package com.dds.plugin.storage.bdb;

import java.io.File;

import com.sleepycat.je.Environment;

/**
 * @author ravid
 * 
 */
public class BDBConfiguration {

	private static boolean readOnly;
	private static String bdbPath;
	private static Environment myEnv;
	private static File envHome;

	BDBConfiguration() {
		setConfiguration();
	}

	private void setConfiguration() {
		// TODO Configurations to be read from file
		setBdbPath(bdbPath);
		setEnvHome(new File(bdbPath));
		setReadOnly(false);
	}

	/**
	 * @param bdbPath
	 *            the bdbPath to set
	 */
	public static void setBdbPath(String _bdbPath) {
		bdbPath = _bdbPath;
	}

	/**
	 * @param envHome
	 *            the envHome to set
	 */
	public static void setEnvHome(File _envHome) {
		envHome = _envHome;
	}

	/**
	 * @param readOnly
	 *            the readOnly to set
	 */
	public static void setReadOnly(boolean _readOnly) {
		readOnly = _readOnly;
	}

	/**
	 * @return
	 */
	public static Environment getEnvironment() {
		return myEnv;
	}

	/**
	 * @return the bdbPath
	 */
	public static String getBdbPath() {
		return bdbPath;
	}

	/**
	 * @return the myEnv
	 */
	public static Environment getMyEnv() {
		return myEnv;
	}

	/**
	 * @return the envHome
	 */
	public static File getEnvHome() {
		return envHome;
	}

	/**
	 * @return
	 */
	public static boolean getReadOnly() {
		return readOnly;
	}
}
