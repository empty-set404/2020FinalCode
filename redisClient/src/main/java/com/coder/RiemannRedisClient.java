package com.coder;

/**
 * API 层
 */
public class RiemannRedisClient {

    private RiemannRedisConnection redisConnection;

    public RiemannRedisClient() {
        this("localhost", 6379);
    }

    public RiemannRedisClient(String host, Integer port) {
        this.redisConnection = new RiemannRedisConnection(host, port);
    }

    public String set(String key, String value) {
        this.redisConnection.sendCommand(RiemannCommand.SET, key.getBytes(), value.getBytes());
        return this.redisConnection.response();
    }

    public String get(String key) {
        this.redisConnection.sendCommand(RiemannCommand.GET, key.getBytes());
        return this.redisConnection.response();
    }

    public String ping() {
        this.redisConnection.sendCommand(RiemannCommand.PING);
        return this.redisConnection.response();
    }

    public String lpush(String key, String value) {
        this.redisConnection.sendCommand(RiemannCommand.LPUSH, key.getBytes(), value.getBytes());
        return this.redisConnection.response();
    }

}
