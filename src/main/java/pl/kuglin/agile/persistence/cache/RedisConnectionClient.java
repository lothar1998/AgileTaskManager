package pl.kuglin.agile.persistence.cache;

public interface RedisConnectionClient<K, V> {
    void set(K key, V value);
    void delete(K key);
    V get(K key);
    void asyncSet(K key, V value);
}
