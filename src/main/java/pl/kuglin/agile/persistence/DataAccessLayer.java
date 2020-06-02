package pl.kuglin.agile.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static pl.kuglin.agile.persistence.DataAccessLayer.ExceptionMessage.CLOSE_CONNECTION_ERROR;
import static pl.kuglin.agile.persistence.DataAccessLayer.ExceptionMessage.ESTABLISH_CONNECTION_ERROR;

public class DataAccessLayer {
    private static final String URL_PROPERTY = "URL";

    private Logger log = LoggerFactory.getLogger(getClass());

    private String databaseURL;
    private Properties connectionProperties;

    DataAccessLayer() {

    }

    public DataAccessLayer(Properties properties) {
        connectionProperties = properties;
        databaseURL = connectionProperties.getProperty(URL_PROPERTY);
    }

    public void executeQuery(ConsumerSQL<Connection> consumer) throws SQLException {
        Connection conn = null;
        try {
            conn = establishConnection();

            consumer.accept(conn);

        } catch (SQLException ex) {
            handleEstablishConnectionException(ex);
        } finally {
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
        log.warn("{}", ESTABLISH_CONNECTION_ERROR, ex);
        throw ex;
    }

    synchronized Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(databaseURL, connectionProperties);
    }

    private void closeConnection(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            log.warn("{}", CLOSE_CONNECTION_ERROR, ex);
        }
    }

    enum ExceptionMessage {

        ESTABLISH_CONNECTION_ERROR("Cannot establish connection or execute SQL query"),
        CLOSE_CONNECTION_ERROR("Cannot close connection");

        private final String message;

        ExceptionMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
