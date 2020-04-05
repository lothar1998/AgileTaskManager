package persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAccessLayer {
    private Logger log = LoggerFactory.getLogger(getClass());

    private String databaseURL;
    private Properties connectionProperties;

    private static DataAccessLayer dataAccessLayer;

    private DataAccessLayer() {
        try(FileInputStream stream = new FileInputStream("src/main/resources/jdbc.properties")){
            connectionProperties = new Properties();
            connectionProperties.load(stream);
            databaseURL = connectionProperties.getProperty("URL");
        } catch (FileNotFoundException ex) {
            log.error("Cannot find jdbc.properties file", ex);
        } catch (IOException ex) {
            log.error("JDBC properties cannot be load", ex);
        }
    }

    public static DataAccessLayer getInstance(){
        if(dataAccessLayer == null)
            dataAccessLayer = new DataAccessLayer();

        return dataAccessLayer;
    }

    public void executeQuery(ConsumerSQL<Connection> consumer){
        Connection conn = null;
        try {
            synchronized (this) {
                conn = DriverManager.getConnection(databaseURL, connectionProperties);
            }

            consumer.accept(conn);

        } catch (SQLException ex) {
            log.warn("Cannot establish connection or execute SQL query", ex);
        }
        finally {
            closeConnection(conn);
        }
    }

    public <R> R executeQuery(FunctionSQL<Connection, R> function){
        Connection conn = null;
        R result = null;
        try {
            synchronized (this) {
                conn = DriverManager.getConnection(databaseURL, connectionProperties);
            }

            result = function.apply(conn);

        } catch (SQLException ex) {
            log.warn("Cannot establish connection or execute SQL query", ex);
        }
        finally {
            closeConnection(conn);
        }

        return result;
    }


    private void closeConnection(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            log.warn("Cannot close connection", ex);
        }
    }
}
