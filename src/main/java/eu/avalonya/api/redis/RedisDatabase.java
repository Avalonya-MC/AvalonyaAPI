package eu.avalonya.api.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;
import java.util.function.Function;

public class RedisDatabase {
    private final JedisPool jedisPool;

    public RedisDatabase(RedisConfiguration config) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxIdle(config.getMaxTimeout());

        if (config.getPassword() == null || config.getPassword().isEmpty()) {
            jedisPool = new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), config.getMaxTimeout());
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), config.getMaxTimeout(), config.getPassword());
        }
    }

    public void execute(Consumer<Jedis> consumer) {
        try (Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        }
    }

    public <R> R execute(Function<Jedis, R> function) {
        try (Jedis jedis = jedisPool.getResource()) {
            return function.apply(jedis);
        }
    }
}
