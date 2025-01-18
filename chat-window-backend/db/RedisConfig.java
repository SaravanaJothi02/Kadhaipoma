package com.base.db;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    private static JedisPool jedisPool;

    private RedisConfig(){}

    public static synchronized JedisPool getJedisPool(){
        if(jedisPool == null){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10);
            config.setMaxIdle(5);
            config.setMinIdle(2);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            jedisPool = new JedisPool(config, "localhost", 6379);
        }
        return jedisPool;
    }

    public static void closePool(){
        if(jedisPool != null){
            jedisPool.close();
        }
    }
}
