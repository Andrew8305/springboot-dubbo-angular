package com.hqvoyage.platform.admin.web.common.config;

import com.hqvoyage.platform.common.redis.RedisRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置
 * Created by zhangxd on 16/3/15.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisRepository redisRepository() {
        return new RedisRepository();
    }

}
