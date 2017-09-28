package com.phantom.plane.core.utils;

import java.util.*;
import java.io.*;

/**
 * Manages properties for the entire Application system. Properties are merely
 * pieces of information that need to be saved in between server restarts. The
 * class also reports the version of Application.
 * <p>
 * At the moment, properties are stored in a Java Properties file. In a version
 * of Application coming soon, the properties file format will move to XML. XML
 * properties will allow hierarchical property structures which may mean the API
 * of this class will have to change.
 * <p>
 * Application properties are only meant to be set and retrevied by core
 * Application classes. Therefore, skin writers should probably ignore this
 * class.
 * <p>
 * This class is implemented as a singleton since many classloaders seem to take
 * issue with doing classpath resource loading from a static context.
 */
public class PropertyManager {
	private final static Map<String, PropertyManager> map = new HashMap<String, PropertyManager>();
	private static Object managerLock = new Object();
	private static String propPath = "/";
	private static String defaultName = "application";

	/**
	 * Returns a Application property.
	 * if the propertyFileName is null,use the defaultName instead
	 * @param name
	 *          the name of the property to return.
	 * @return the property value specified by name.
	 */
	public static String getProperty(String name, String... propertyFileName) {
		return getPropertyObject(propertyFileName).getProp(name);
	}

	private static PropertyManager getPropertyObject(String... propertyName) {
		String name;
		if (propertyName == null || propertyName.length == 0
				|| propertyName[0] == null || propertyName[0].trim().equals(""))
			name = defaultName;
		else
			name = propertyName[0];
		PropertyManager manager = map.get(name);
		if (manager == null) {
			String fullPath = propPath + name + ".properties";
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(fullPath);
					map.put(name, manager);
				}
			}
		}
		return manager;
	}

	/**
	 * Sets a Application property. If the property doesn't already exists, a new
	 * one will be created.
	 * 
	 * @param name
	 *          the name of the property being set.
	 * @param value
	 *          the value of the property being set.
	 */
	public static void setProperty(String name, String value,
			String... propertyName) {
		getPropertyObject(propertyName).setProp(name, value);
	}

	/**
	 * Deletes a Application property. If the property doesn't exist, the method
	 * does nothing.
	 * 
	 * @param name
	 *          the name of the property to delete.
	 */
	public static void deleteProperty(String name,String... propertyName) {
		getPropertyObject(propertyName).deleteProp(name);
	}

	/**
	 * Returns the names of the Application properties.
	 * 
	 * @return an Enumeration of the Application property names.
	 */
	public static Enumeration propertyNames(String... propertyName) {
		return getPropertyObject(propertyName).propNames();
	}

	/**
	 * Returns true if the properties are readable. This method is mainly valuable
	 * at setup time to ensure that the properties file is setup correctly.
	 */
	public static boolean propertyFileIsReadable(String... propertyName) {
		return getPropertyObject(propertyName).propFileIsReadable();
	}

	/**
	 * Returns true if the properties are writable. This method is mainly valuable
	 * at setup time to ensure that the properties file is setup correctly.
	 */
	public static boolean propertyFileIsWritable(String... propertyName) {
		return getPropertyObject(propertyName).propFileIsWritable();
	}

	/**
	 * Returns true if the application.properties file exists where the path
	 * property purports that it does.
	 */
	public static boolean propertyFileExists(String... propertyName) {
		return getPropertyObject(propertyName).propFileExists();
	}

	private Properties properties = null;
	private Object propertiesLock = new Object();
	private String resourceURI;

	/**
	 * Creates a new PropertyManager. Singleton access only.
	 */
	private PropertyManager(String resourceURI) {
		this.resourceURI = resourceURI;
	}

	/**
	 * Gets a Application property. Application properties are stored in
	 * application.properties. The properties file should be accesible from the
	 * classpath. Additionally, it should have a path field that gives the full
	 * path to where the file is located. Getting properties is a fast operation.
	 * 
	 * @param name
	 *          the name of the property to get.
	 * @return the property specified by name.
	 */
	protected String getProp(String name) {
		// If properties aren't loaded yet. We also need to make this thread
		// safe, so synchronize...
		if (properties == null) {
			synchronized (propertiesLock) {
				// Need an additional check
				if (properties == null) {
					loadProps();
				}
			}
		}
		String property = properties.getProperty(name);
		if (property == null) {
			return null;
		} else {
			return property.trim();
		}
	}

	/**
	 * Sets a Application property. Because the properties must be saved to disk
	 * every time a property is set, property setting is relatively slow.
	 */
	protected void setProp(String name, String value) {
		// Only one thread should be writing to the file system at once.
		synchronized (propertiesLock) {
			// Create the properties object if necessary.
			if (properties == null) {
				loadProps();
			}
			properties.setProperty(name, value);
			saveProps();
		}
	}

	protected void deleteProp(String name) {
		// Only one thread should be writing to the file system at once.
		synchronized (propertiesLock) {
			// Create the properties object if necessary.
			if (properties == null) {
				loadProps();
			}
			properties.remove(name);
			saveProps();
		}
	}

	protected Enumeration propNames() {
		// If properties aren't loaded yet. We also need to make this thread
		// safe, so synchronize...
		if (properties == null) {
			synchronized (propertiesLock) {
				// Need an additional check
				if (properties == null) {
					loadProps();
				}
			}
		}
		return properties.propertyNames();
	}

	/**
	 * Loads Application properties from the disk.
	 */
	private void loadProps() {
		properties = new Properties();
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream(resourceURI);
			properties.load(in);
		} catch (Exception e) {
			System.err
					.println("Error reading Application properties in PropertyManager.loadProps() "
							+ e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Saves Application properties to disk.
	 */
	private void saveProps() {
		// Now, save the properties to disk. In order for this to work, the user
		// needs to have set the path field in the properties file. Trim
		// the String to make sure there are no extra spaces.
		String path = "";
		OutputStream out = null;
		try {
			path = properties.getProperty("path").trim();
			out = new FileOutputStream(path);
			properties.store(out, "application.properties -- "
					+ (new java.util.Date()));
		} catch (Exception ioe) {
			System.err
					.println("There was an error writing application.properties to "
							+ path
							+ ". "
							+ "Ensure that the path exists and that the Application process has permission "
							+ "to write to it -- " + ioe);
			ioe.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Returns true if the properties are readable. This method is mainly valuable
	 * at setup time to ensure that the properties file is setup correctly.
	 */
	public boolean propFileIsReadable() {
		try {
			InputStream in = getClass().getResourceAsStream(resourceURI);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns true if the application.properties file exists where the path
	 * property purports that it does.
	 */
	public boolean propFileExists() {
		String path = getProp("path");
		if (path == null) {
			return false;
		}
		File file = new File(path);
		if (file.isFile()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the properties are writable. This method is mainly valuable
	 * at setup time to ensure that the properties file is setup correctly.
	 */
	public boolean propFileIsWritable() {
		String path = getProp("path");
		File file = new File(path);
		if (file.isFile()) {
			// See if we can write to the file
			if (file.canWrite()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * The AppServer type and version of Application. i.e. 1.x.x
	 */
	private static String sAppServer = null;

	/**
	 * The Major version number of Application. i.e. 1.x.x
	 */
	private static final int MAJOR_VERSION = 1;

	/**
	 * The Minor version number of Application. i.e. x.1.x.
	 */
	private static final int MINOR_VERSION = 2;

	/**
	 * The revision version number of Application. i.e. x.x.1.
	 */
	private static final int REVISION_VERSION = 4;

	/**
	 * Returns the version number of Application as a String. i.e. --
	 * major.minor.revision
	 */
	public static String getAppVersion() {
		return MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	}

	/**
	 * Returns the major version number of Application. i.e. -- 1.x.x
	 */
	public static int getAppVersionMajor() {
		return MAJOR_VERSION;
	}

	/**
	 * Returns the minor version number of Application. i.e. -- x.1.x
	 */
	public static int getAppVersionMinor() {
		return MINOR_VERSION;
	}

	/**
	 * Returns the revision version number of Application. i.e. -- x.x.1
	 */
	public static int getAppVersionRevision() {
		return REVISION_VERSION;
	}
	public static void main(String[] args) {
		System.out.println(PropertyManager.getProperty("JDBC_TYPE", ""));
		System.out.println(PropertyManager.getProperty("db.driver"));
		System.out.println(PropertyManager.getProperty("1000", "errorConfig"));
	}
}
