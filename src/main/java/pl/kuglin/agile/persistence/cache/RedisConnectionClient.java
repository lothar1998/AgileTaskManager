package pl.kuglin.agile.persistence.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Properties;

public class RedisConnectionClient {
    private static final String URL_PROPERTY = "redis.url";
    private static final String EXPIRE_TIME_PROPERTY = "redis.expire";

    private final long expiredTime;

    private RedisClient client;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> command;

    RedisConnectionClient() {
        expiredTime = 0;
    }

    public RedisConnectionClient(Properties properties){
        String url = properties.getProperty(URL_PROPERTY);
        expiredTime = Long.parseLong(properties.getProperty(EXPIRE_TIME_PROPERTY));
        client = RedisClient.create(url);
        connection = client.connect();
        command = connection.sync();
    }

    public void set(String key, String value){
        command.set(key, value);
        synchronized (this) {
            command.expire(key, expiredTime);
        }
    }

    public void delete(String key){
        command.del(key);
    }

    public String get(String key){
        return command.get(key);
    }

    public void closeClient(){
        connection.close();
        client.shutdown();
    }
}
