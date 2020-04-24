package pl.kuglin.agile.persistence.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisConnectionClientTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String MEANINGLESS_STRING = null;
    private static final Boolean MEANINGLESS_BOOLEAN = null;
    private static final Long MEANINGLESS_LONG = null;
    private static final Long DEFAULT_EXPIRE_TIME = 0L;

    @Mock
    private StatefulRedisConnection<String, String> connection;
    @Mock
    private RedisCommands<String, String> syncCommand;
    @Mock
    private RedisAsyncCommands<String, String> asyncCommand;
    @Mock
    private RedisClient client;
    @Spy
    @InjectMocks
    private RedisConnectionClient redisClient = new RedisConnectionClient();
    @Captor
    private ArgumentCaptor<String> keyCaptor;
    @Captor
    private ArgumentCaptor<String> valueCaptor;
    @Captor
    private ArgumentCaptor<Long> expireTimeCaptor;


    @Test
    void setTest(){
        when(syncCommand.set(anyString(), anyString())).thenReturn(MEANINGLESS_STRING);
        when(syncCommand.expire(anyString(), anyLong())).thenReturn(MEANINGLESS_BOOLEAN);

        redisClient.set(KEY, VALUE);

        verify(syncCommand).set(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(KEY, keyCaptor.getValue());
        assertEquals(VALUE, valueCaptor.getValue());
    }

    @Test
    void setExpireTest(){
        when(syncCommand.set(anyString(), anyString())).thenReturn(MEANINGLESS_STRING);
        when(syncCommand.expire(anyString(), anyLong())).thenReturn(MEANINGLESS_BOOLEAN);

        redisClient.set(KEY, VALUE);

        verify(syncCommand).expire(keyCaptor.capture(), expireTimeCaptor.capture());

        assertEquals(KEY, keyCaptor.getValue());
        assertEquals(DEFAULT_EXPIRE_TIME, expireTimeCaptor.getValue());
    }

    @Test
    void deleteTest(){
        when(syncCommand.del(anyString())).thenReturn(MEANINGLESS_LONG);

        redisClient.delete(KEY);

        verify(syncCommand).del(keyCaptor.capture());

        assertEquals(KEY, keyCaptor.getValue());
    }

    @Test
    void getTest(){
        when(syncCommand.get(anyString())).thenReturn(VALUE);

        String obtainedValue = redisClient.get(KEY);

        verify(syncCommand).get(keyCaptor.capture());

        assertEquals(KEY, keyCaptor.getValue());
        assertEquals(VALUE, obtainedValue);
    }

    @Test
    void closeClientNotThrowsException(){
        doNothing().when(connection).close();
        doNothing().when(client).shutdown();

        assertDoesNotThrow(() -> redisClient.closeClient());
    }

}