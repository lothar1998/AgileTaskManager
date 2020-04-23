package pl.kuglin.agile.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kuglin.agile.utils.PropertyLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataAccessLayer {
    private static final String URL_PROPERTY = "URL";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String databaseURL;
    private final Properties connectionProperties;

    public DataAccessLayer(PropertyLoader propertyLoader) {
        propertyLoader.load();
        connectionProperties = propertyLoader.getProperties();
        databaseURL = propertyLoader.getProperty(URL_PROPERTY);
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
