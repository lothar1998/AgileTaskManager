package pl.kuglin.agile.utils;

import java.util.Properties;

public interface PropertyLoader {
    void load();

    String getProperty(String propertyName);

    Properties getProperties();
}
