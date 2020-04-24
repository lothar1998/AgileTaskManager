package pl.kuglin.agile.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import pl.kuglin.agile.persistence.DataAccessLayer.ExceptionMessage;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.kuglin.agile.persistence.DataAccessLayer.ExceptionMessage.CLOSE_CONNECTION_ERROR;
import static pl.kuglin.agile.persistence.DataAccessLayer.ExceptionMessage.ESTABLISH_CONNECTION_ERROR;


@ExtendWith(MockitoExtension.class)
class DataAccessLayerTest {
    @Spy
    @InjectMocks
    private final DataAccessLayer dataAccessLayer = new DataAccessLayer();
    @Mock
    private Logger log;
    @Captor
    private ArgumentCaptor<Enum<ExceptionMessage>> message;
    @Captor
    private ArgumentCaptor<Throwable> throwable;

    @Test
    void establishConnectionThrowsSQLExceptionTest(){
        assertThrows(SQLException.class, () -> dataAccessLayer.executeQuery(connection -> {}));

        verify(log).warn(anyString(), message.capture(), throwable.capture());

        assertEquals(ESTABLISH_CONNECTION_ERROR, message.getValue());
        assertEquals(SQLException.class, throwable.getValue().getClass());
    }

    @Test
    void executeQueryWithConsumerSQLArgumentThrowsSQLExceptionTest() throws SQLException {
        doReturn(null).when(dataAccessLayer).establishConnection();

        assertThrows(SQLException.class, () ->
                dataAccessLayer.executeQuery((ConsumerSQL<Connection>) connection -> {throw new SQLException();}));

        verify(log).warn(anyString(), message.capture(), throwable.capture());

        assertEquals(ESTABLISH_CONNECTION_ERROR, message.getValue());
        assertEquals(SQLException.class, throwable.getValue().getClass());
    }

    @Test
    void executeQueryWithFunctionSQLArgumentThrowsSQLExceptionTest() throws SQLException {
        doReturn(null).when(dataAccessLayer).establishConnection();

        assertThrows(SQLException.class, () ->
                dataAccessLayer.executeQuery((FunctionSQL<Connection, ?>) connection -> {throw new SQLException();}));

        verify(log).warn(anyString(), message.capture(), throwable.capture());

        assertEquals(ESTABLISH_CONNECTION_ERROR, message.getValue());
        assertEquals(SQLException.class, throwable.getValue().getClass());
    }

    @Test
    void executeQueryWithConsumerSQLArgumentProperlyTest() throws SQLException {
        doReturn(null).when(dataAccessLayer).establishConnection();

        assertDoesNotThrow(() -> dataAccessLayer.executeQuery(connection -> {}));
    }

    @Test
    void executeQueryWithFunctionSQLArgumentProperlyTest() throws SQLException {
        doReturn(null).when(dataAccessLayer).establishConnection();

        assertDoesNotThrow(() -> dataAccessLayer.executeQuery(connection -> null));
    }

    @Test
    void closeConnectionThrowsSQLExceptionTest() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        doThrow(SQLException.class).when(connectionMock).close();

        doReturn(connectionMock).when(dataAccessLayer).establishConnection();

        assertDoesNotThrow(() -> dataAccessLayer.executeQuery(connection -> {}));

        verify(log).warn(anyString(), message.capture(), throwable.capture());

        assertEquals(CLOSE_CONNECTION_ERROR, message.getValue());
        assertEquals(SQLException.class, throwable.getValue().getClass());
    }
}