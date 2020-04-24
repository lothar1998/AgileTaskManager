package pl.kuglin.agile.persistence.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;
import java.util.Properties;

public class RedisConnectionClient {
    private static final String URL_PROPERTY = "redis.url";
    private static final String EXPIRE_TIME_PROPERTY = "redis.expire";

    private final long expiredTime;

    private RedisClient client;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> command;
    private RedisAsyncCommands<String, String> asyncCommand;

    RedisConnectionClient() {
        expiredTime = 0;
    }

    public RedisConnectionClient(Properties properties){
        String url = properties.getProperty(URL_PROPERTY);
        expiredTime = Long.parseLong(properties.getProperty(EXPIRE_TIME_PROPERTY));
        client = RedisClient.create(url);
        connection = client.connect();
        command = connection.sync();
        asyncCommand = connection.async();
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

    public List<String> keys(String pattern){
        return command.keys(pattern);
    }

    public void asyncSet(String key, String value){
        asyncCommand.set(key, value);
        synchronized (this){
            asyncCommand.expire(key, expiredTime);
        }
    }

    public void closeClient(){
        connection.close();
        client.shutdown();
    }
}
