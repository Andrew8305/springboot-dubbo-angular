package me.zhangxudong.platform.business.provider.config;

import me.zhangxudong.platform.common.redis.RedisRepository;
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
