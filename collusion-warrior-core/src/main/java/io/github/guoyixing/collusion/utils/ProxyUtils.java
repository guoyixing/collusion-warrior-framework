package io.github.guoyixing.collusion.utils;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;

/**
 * 代理对象的工具类
 *
 * @author 敲代码的旺财
 * @date 2022/7/12 15:19
 */
public class ProxyUtils {

    /**
     * 获取Cglib代理的目标对象
     *
     * @param jpaRepository Cglib代理的对象
     * @return 代理的目标对象
     */
    public static JpaRepository<?, ?> getCglibProxyTarget(JpaRepository<?, ?> jpaRepository) {
        try {
            Field h = jpaRepository.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(jpaRepository);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return (JpaRepository<?, ?>) ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取jdk代理的目标对象
     *
     * @param jpaRepository jdk代理的对象
     * @return 代理的目标对象
     */
    public static JpaRepository<?, ?> getJdkProxyTarget(JpaRepository<?, ?> jpaRepository) {
        try {
            Field h = jpaRepository.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            AopProxy aopProxy = (AopProxy) h.get(jpaRepository);
            Field advised = aopProxy.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return (JpaRepository<?, ?>) ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
