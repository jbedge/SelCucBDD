package com.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationHelper {
    Properties prop = new Properties();
    private static final String ENCRYPTION_CONFIG_PATH = System.getProperty("user.dir") + File.separator + "resources" + File.separator+"encryptionConfig.properties";

    public ConfigurationHelper(){
        getProperties();
    }

    private void getProperties() {
        FileInputStream input = null;

        try {
            input = new FileInputStream(ENCRYPTION_CONFIG_PATH);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getEncryptionKey() {
        return prop.getProperty("encryptionKey");
    }

    public String getIv() {
        return prop.getProperty("iv");
    }
}