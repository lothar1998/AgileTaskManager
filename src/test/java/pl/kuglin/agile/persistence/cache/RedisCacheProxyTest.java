package pl.kuglin.agile.persistence.cache;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kuglin.agile.persistence.CrudRepository;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisCacheProxyTest {

    private static final Integer IDENTIFIER = 1;

    private final Gson gson = new Gson();

    private final Class<PersistentObject> tClass = PersistentObject.class;

    @Mock
    private RedisConnectionClient<String, String> client;
    @Mock
    private CrudRepository<PersistentObject, Integer> repository;
    @InjectMocks
    private final RedisCacheProxy<PersistentObject, Integer> proxy = new RedisCacheProxy<>(tClass);

    @Captor
    private ArgumentCaptor<String> keyCaptor;
    @Captor
    private ArgumentCaptor<String> valueCaptor;

    @Test
    void saveTest() throws SQLException {
        doNothing().when(repository).save(any());

        PersistentObject persistentObject = new PersistentObject(IDENTIFIER);
        String expectedKey = generateKey(persistentObject);
        String expectedValue = gson.toJson(persistentObject);

        proxy.save(persistentObject);

        verify(client).set(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(expectedKey, keyCaptor.getValue());
        assertEquals(expectedValue, valueCaptor.getValue());
    }

    @Test
    void updateTest() throws SQLException {
        doNothing().when(repository).update(any());

        PersistentObject persistentObject = new PersistentObject(IDENTIFIER);
        String expectedKey = generateKey(persistentObject);
        String expectedValue = gson.toJson(persistentObject);

        proxy.update(persistentObject);

        verify(client).set(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(expectedKey, keyCaptor.getValue());
        assertEquals(expectedValue, valueCaptor.getValue());
    }

    @Test
    void deleteTest() throws SQLException {
        doNothing().when(repository).delete(any());

        PersistentObject persistentObject = new PersistentObject(IDENTIFIER);
        String expectedKey = generateKey(persistentObject);

        proxy.delete(persistentObject);

        verify(client).delete(keyCaptor.capture());

        assertEquals(expectedKey, keyCaptor.getValue());
    }

    @Test
    void getWhenDataIsInCacheTest() throws SQLException {
        PersistentObject expectedObject = new PersistentObject(IDENTIFIER);
        String expectedKey = generateKey(IDENTIFIER);

        when(client.get(anyString())).thenReturn(gson.toJson(expectedObject));

        PersistentObject obtainedObject = proxy.get(IDENTIFIER);

        verify(client).get(keyCaptor.capture());

        assertEquals(expectedKey, keyCaptor.getValue());
        assertEquals(expectedObject, obtainedObject);
    }

    @Test
    void getWhenDataIsNotInCacheTest() throws SQLException {
        PersistentObject persistentObject = new PersistentObject(IDENTIFIER);
        String expectedKey = generateKey(IDENTIFIER);
        String expectedValue = gson.toJson(persistentObject);

        when(client.get(anyString())).thenReturn(null);
        when(repository.get(any())).thenReturn(persistentObject);

        PersistentObject obtainedObject = proxy.get(IDENTIFIER);

        verify(client).asyncSet(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(expectedKey, keyCaptor.getValue());
        assertEquals(expectedValue, valueCaptor.getValue());
        assertEquals(persistentObject, obtainedObject);
    }

    @Test
    void getAllWhenDataIsCacheTest() throws SQLException {
        PersistentObject persistentObject = new PersistentObject(IDENTIFIER);
        List<PersistentObject> expectedResult = Collections.singletonList(persistentObject);
        String expectedKey = generateKey(persistentObject);
        String expectedValue = gson.toJson(persistentObject);

        when(repository.getAll()).thenReturn(expectedResult);

        List<PersistentObject> obtainedList = proxy.getAll();

        verify(client).asyncSet(keyCaptor.capture(), valueCaptor.capture());

        assertEquals(expectedKey, keyCaptor.getValue());
        assertEquals(expectedValue, valueCaptor.getValue());
        assertTrue(expectedResult.containsAll(obtainedList));
        assertTrue(obtainedList.containsAll(expectedResult));
    }

    private String generateKey(PersistentObject element){
        return generateKey(element.getIdentifier());
    }

    private String generateKey(Integer identifier){
        return PersistentObject.class.getName() + identifier;
    }

    private static class PersistentObject implements Identifiable<Integer>{

        private final Integer id;

        public PersistentObject(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getIdentifier() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PersistentObject that = (PersistentObject) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}