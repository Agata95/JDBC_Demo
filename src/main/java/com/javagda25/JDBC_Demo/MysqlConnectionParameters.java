package com.javagda25.JDBC_Demo;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
@Getter

public class MysqlConnectionParameters {
    private static final String PROPERTIES_FILE_NAME = "/jdbc.properties";
    private Properties properties;

    private String dbHost;
    private String dbPort;
    private String dbUsername;
    private String dbPassword;
    private String dbName;

//    metoda sprawdza poprawność pliku konfiguracyjnego:
    public MysqlConnectionParameters() throws IOException {
        loadConfigurationFrom(PROPERTIES_FILE_NAME);

        dbHost = loadParameter("jdbc.database.host");
        dbPort = loadParameter("jdbc.database.port");
        dbName = loadParameter("jdbc.database.name");
        dbUsername = loadParameter("jdbc.username");
        dbPassword = loadParameter("jdbc.password");
    }

    //    ładowanie do obiektu Properties
    private Properties loadConfigurationFrom(String recourseName) throws IOException {
        properties = new Properties();

        InputStream propertiesStrem = this.getClass().getResourceAsStream(recourseName);
        if (propertiesStrem == null) {
            throw new FileNotFoundException("Resource file cannot be loaded.");
        }
        properties.load(propertiesStrem);
        return properties;
    }

    //    metoda ładująca pola po nazwie
    private String loadParameter(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
