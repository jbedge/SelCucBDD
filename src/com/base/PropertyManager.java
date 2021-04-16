package com.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertyManager {

    private final static String EMAIL_PROPERTIES_FILE = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "email.properties";
    private final static String APP_PROPERTIES_FILE =   System.getProperty("user.dir") + File.separator + "resources" + File.separator + "app.properties";
    private final static String LOG4J_PROPERTIES_FILE = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "log4j.properties";

    private static Properties emailProperties = new Properties();
    private static Properties appProperties = new Properties();
    private static Properties log4jProperties = new Properties();

    //private Hashtable listeners = null;
    private static final Object lock = new Object();
    private static PropertyManager propertyManager  = null;

    private PropertyManager() {
    }

    public static void initializePropertyManager() throws IOException {
        if (propertyManager == null) {
            synchronized (lock) {
                if (propertyManager == null) {
                    propertyManager = new PropertyManager();
                    propertyManager.loadProperties();
                }
            }
        }
    }

    private void loadProperties()  throws IOException {
        try {
            FileInputStream in;

            in = new FileInputStream(LOG4J_PROPERTIES_FILE);
            log4jProperties.load(in);
            in.close();

            in = new FileInputStream(APP_PROPERTIES_FILE);
            appProperties.load(in);
            in.close();

            in = new FileInputStream(EMAIL_PROPERTIES_FILE);
            emailProperties.load(in);
            in.close();

        } catch (Throwable th) {
            // TODO: log something
        }

    }

    public static String getProperty(String key) throws IOException {
        initializePropertyManager();
        String val = null;
        if (key != null) {
            if (appProperties!= null)
                val = appProperties.getProperty(key);

            if (val == null)
                val = emailProperties.getProperty(key);

            if(val == null)
                val = log4jProperties.getProperty(key);
        }
        return (val);
    }

    public static Properties getLog4jProperties() {
        return log4jProperties;
    }
}