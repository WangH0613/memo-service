package cn.com.koriesh.memo.shiro;

import cn.com.koriesh.memo.shiro.MShiroFilterFactoryBean;
import cn.com.koriesh.memo.shiro.filter.BizFilter;
import cn.com.koriesh.memo.shiro.realm.BizRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ShiroConfig {

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    /**
     * @Description: 定义shiro过滤器配置
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        log.info("shiro init start");
        ShiroFilterFactoryBean bean = new MShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setUnauthorizedUrl("/unAuthorized");
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("biz", new BizFilter());
        bean.setFilters(filters);
        Map<String, String> chains = new LinkedHashMap<>();
        chains.put("/pub/login", "anon");
        chains.put("/pub/loginOpcc", "anon");
        chains.put("/pub/logout", "anon");
        chains.put("/**/anon/**", "anon"); // 请求的白名单
        chains.put("/actuator/**", "anon"); // springboot-admin的健康探测请求
        chains.put("/**", "biz");
        bean.setFilterChainDefinitionMap(chains);
        log.info("shiro anon path is {}", "/pub/login , /pub/loginOpcc , /pub/logout , /**/anon/**");
        log.info("shiro biz path is {}", "/**");
        log.info("shiro config end");
        return bean;
    }

    /**
     * @Description: 安全管理器配置
     * 由于项目采用token来管理会话
     * 所以不需要安全管理器来缓存会话session
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 按照用途 可分为
        // biz-realm   |   open-realm   |   iframe-realm
        // 目前暂时先考虑实现 biz 的 realm
        ArrayList<Realm> realmArrayList = new ArrayList<Realm>();
        realmArrayList.add(bizRealm());
        securityManager.setRealms(realmArrayList);
        // 关闭 shiro 自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * @Description: 配置业务用的realm域
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    @Bean
    public BizRealm bizRealm() {
        return new BizRealm();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        advisorAutoProxyCreator.setUsePrefix(true);
        return advisorAutoProxyCreator;
    }

}
