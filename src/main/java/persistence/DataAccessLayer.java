package persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataAccessLayer {
    private static final String PATH = "src/main/resources/jdbc.properties";
    private static final String URL_PROPERTY = "URL";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private String databaseURL;
    private Properties connectionProperties;

    private static DataAccessLayer dataAccessLayer;

    private DataAccessLayer() {
        initializeProperties();
    }

    public static DataAccessLayer getInstance(){
        if(dataAccessLayer == null)
            dataAccessLayer = new DataAccessLayer();

        return dataAccessLayer;
    }

    public void executeQuery(ConsumerSQL<Connection> consumer) throws SQLException {
        Connection conn = null;
        try {
            conn = establishConnection();

            consumer.accept(conn);

        } catch (SQLException ex) {
            handleEstablishConnectionException(ex);
        }
        finally {
            closeConnection(conn);
        }
    }

    public <R> R executeQuery(FunctionSQL<Connection, R> function) throws SQLException {
        Connection conn = null;
        R result = null;
        try {
            conn = establishConnection();

            result = function.apply(conn);

        } catch (SQLException ex) {
            handleEstablishConnectionException(ex);
        } finally {
            closeConnection(conn);
        }

        return result;
    }

    private void initializeProperties() {
        try (FileInputStream file = new FileInputStream(PATH)) {
            loadPropertiesFromFile(file);
        } catch (FileNotFoundException ex) {
            log.error("Cannot find jdbc.properties file", ex);
        } catch (IOException ex) {
            log.error("JDBC properties cannot be load", ex);
        }
    }

    private void loadPropertiesFromFile(FileInputStream file) throws IOException {
        connectionProperties = new Properties();
        connectionProperties.load(file);
        databaseURL = connectionProperties.getProperty(URL_PROPERTY);
    }

    private void handleEstablishConnectionException(SQLException ex) throws SQLException {
        log.warn("Cannot establish connection or execute SQL query", ex);
        throw new SQLException(ex);
    }

    private synchronized Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(databaseURL, connectionProperties);
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
