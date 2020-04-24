package pl.kuglin.agile.persistence.cache;

import java.util.List;

public interface RedisConnectionClient<K, V, P> {
    void set(K key, V value);
    void delete(K key);
    V get(K key);
    List<K> keys(P pattern);
    void asyncSet(K key, V value);
}
