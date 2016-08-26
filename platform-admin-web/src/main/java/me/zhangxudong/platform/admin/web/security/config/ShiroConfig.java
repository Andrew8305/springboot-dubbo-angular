package me.zhangxudong.platform.admin.web.security.config;

import me.zhangxudong.platform.admin.web.common.base.property.Global;
import me.zhangxudong.platform.admin.web.security.FormAuthenticationFilter;
import me.zhangxudong.platform.admin.web.security.SystemAuthorizingRealm;
import me.zhangxudong.platform.admin.web.security.session.RedisSessionDAO;
import me.zhangxudong.platform.common.shiro.session.SessionDAO;
import me.zhangxudong.platform.common.shiro.session.SessionManager;
import me.zhangxudong.platform.common.shiro.util.IdGen;
import me.zhangxudong.platform.common.utils.NumberHelper;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import java.util.Map;

/**
 * Shiro 配置
 * Created by zhangxd on 16/6/15.
 */
@Configuration
public class ShiroConfig {

    @Bean
    public FilterRegistrationBean shiroFilter(ShiroFilterFactoryBean shiroFilterFactoryBean) throws Exception {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter((Filter) shiroFilterFactoryBean.getObject());
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new FormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/servlet/validateCodeServlet", "anon");
        filterChainDefinitionMap.put("/theme/**", "anon");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "user");

        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(EhCacheManager shiroCacheManager, SessionManager sessionManager) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(systemAuthorizingRealm());
        dwsm.setSessionManager(sessionManager);
        dwsm.setCacheManager(shiroCacheManager);
        return dwsm;
    }

    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        //自定义会话管理配置
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        //会话超时时间，单位：毫秒
        sessionManager.setGlobalSessionTimeout(NumberHelper.toInt(Global.getConfig("session.sessionTimeout")));

        //定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(NumberHelper.toInt(Global.getConfig("session.sessionTimeoutClean")));
        sessionManager.setSessionValidationSchedulerEnabled(true);

        sessionManager.setSessionIdCookie(this.sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean
    public EhCacheManager shiroCacheManager(CacheManager cacheManager) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    @Bean
    public SessionDAO sessionDAO(EhCacheManager shiroCacheManager) {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setSessionIdGenerator(new IdGen());
        sessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        sessionDAO.setCacheManager(shiroCacheManager);
        return sessionDAO;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public SystemAuthorizingRealm systemAuthorizingRealm() {
        return new SystemAuthorizingRealm();
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        //指定本系统SESSIONID, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT 等默认JSESSIONID,
        //当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("platform.admin.session.id");
        return simpleCookie;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        // 保证实现了Shiro内部lifecycle函数的bean执行
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }
}
