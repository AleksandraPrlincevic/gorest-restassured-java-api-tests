package com.aleksandraprlincevic.gorest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            InputStream inputStream = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getToken() {
        return properties.getProperty("token");
    }
    public static String getUserParameter() {
        return properties.getProperty("usersParameter");
    }
    public static String getPostParameter() {
        return properties.getProperty("postsParameter");
    }

}
