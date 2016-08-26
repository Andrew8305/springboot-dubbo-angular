package me.zhangxudong.platform.system.provider;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

/**
 * Spring-boot 启动入口
 * Created by zhangxd on 16/3/7.
 */
@SpringBootApplication
@EnableTransactionManagement //启用事务
@ImportResource("classpath:dubbo-provider.xml")
public class ProviderApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication application = new SpringApplication(ProviderApplication.class);
        application.setRegisterShutdownHook(false);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        System.out.println("Service provider started!!!");
        System.in.read(); // 按任意键退出
    }
}
