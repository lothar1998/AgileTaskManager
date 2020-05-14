package pl.kuglin.agile.persistence.cache;

import com.google.gson.Gson;
import pl.kuglin.agile.persistence.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public class RedisCacheProxy<T extends Identifiable<S>, S> implements CrudRepository<T, S> {

    private final Class<T> classType;
    private final Gson jsonParser = new Gson();
    private RedisConnectionClient<String, String> cacheClient;
    private CrudRepository<T, S> repository;

    RedisCacheProxy(Class<T> classType) {
        this.classType = classType;
    }

    public RedisCacheProxy(RedisConnectionClient<String, String> client, CrudRepository<T, S> repository, Class<T> classType) {
        this.cacheClient = client;
        this.repository = repository;
        this.classType = classType;
    }

    @Override
    public void save(T element) throws SQLException {
        repository.save(element);

        KeyValuePair pair = generateKeyValuePair(element);
        cacheClient.set(pair.getKey(), pair.getValue());
    }

    @Override
    public void update(T element) throws SQLException {
        repository.update(element);

        KeyValuePair pair = generateKeyValuePair(element);
        cacheClient.set(pair.getKey(), pair.getValue());
    }

    @Override
    public void delete(T element) throws SQLException {
        repository.delete(element);

        String key = generateKey(element);
        cacheClient.delete(key);
    }

    @Override
    public T get(S identifier) throws SQLException {
        String key = generateKey(identifier);
        String jsonObject = cacheClient.get(key);

        if (jsonObject != null)
            return jsonParser.fromJson(jsonObject, classType);

        T result = repository.get(identifier);
        cacheClient.asyncSet(key, jsonParser.toJson(result));

        return result;
    }

    @Override
    public List<T> getAll() throws SQLException {
        List<T> allElements = repository.getAll();

        String key;
        for (T element : allElements) {
            key = generateKey(element);
            cacheClient.asyncSet(key, jsonParser.toJson(element));
        }

        return allElements;
    }

    private String generateKey(T element) {
        return generateKey(element.getIdentifier());
    }

    private String generateKey(S identifier) {
        return classType.getName() + identifier;
    }

    private KeyValuePair generateKeyValuePair(T element) {
        String key = generateKey(element);
        String value = jsonParser.toJson(element);

        return new KeyValuePair(key, value);
    }

    private static class KeyValuePair {
        private final String key;
        private final String value;

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
