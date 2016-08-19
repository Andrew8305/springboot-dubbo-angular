package com.hqvoyage.platform.business.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hqvoyage.platform.business.web.common.converter.JsonMapper;
import com.hqvoyage.platform.business.web.common.interceptor.LogInterceptor;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WEB配置类
 * Created by zhangxd on 16/3/10.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置日志拦截器拦截请求路径
        registry
                .addInterceptor(logInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
        ;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new JsonMapper();
    }

    @Bean
    public HttpMessageConverter  httpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(this.objectMapper());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false) // 系统对外暴露的 URL 不会识别和匹配 .* 后缀
                .setUseTrailingSlashMatch(true); // 系统不区分 URL 的最后一个字符是否是斜杠 /
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        // 等价于<mvc:default-servlet-handler />, 对静态资源文件的访问, 将无法 mapping 到 Controller 的 path 交给 default servlet handler 处理
        configurer.enable();
    }

}
