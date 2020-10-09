package com.enao.team2.quanlynhanvien.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Protocol;

import java.net.URISyntaxException;

//@Configuration
public class LocalRedisConfig {
//    @Bean
    public RedisConnectionFactory jedisConnectionFactory() throws URISyntaxException {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName("ec2-34-200-124-128.compute-1.amazonaws.com");
        redisConnectionFactory.setPort(21489);
        redisConnectionFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
        redisConnectionFactory.setPassword("p2423039d60a44d647d414a858cb4a24414d498aad7f3d8752538f7f7bd50087b");

        return redisConnectionFactory;
    }
}
