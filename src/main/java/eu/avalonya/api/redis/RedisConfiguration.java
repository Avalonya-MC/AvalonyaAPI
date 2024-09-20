package eu.avalonya.api.redis;

import lombok.Getter;

import java.util.Map;

@Getter
public class RedisConfiguration {
    private String host;
    private int port;
    private int maxTimeout;
    private String password;
    public Map<String, String> keys;
}
