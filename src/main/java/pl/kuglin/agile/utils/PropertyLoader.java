package pl.kuglin.agile.utils;

import java.util.Properties;

public interface PropertyLoader {
    void loadProperties();

    String getProperty(String propertyName);

    Properties getProperties();
}
