package com.enao.team2.quanlynhanvien.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;

//@Configuration
public class LocalRedisConfig {
//     @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//
//        try {
//            URI redisUri = new URI(System.getenv("redis-16702.c44.us-east-1-2.ec2.cloud.redislabs.com:16702"));
//            JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
//            redisConnectionFactory.setHostName(redisUri.getHost());
//            redisConnectionFactory.setPort(redisUri.getPort());
//            redisConnectionFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
//            redisConnectionFactory.setPassword(redisUri.getUserInfo().split(":",2)[1]);
//
//        return redisConnectionFactory;
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
