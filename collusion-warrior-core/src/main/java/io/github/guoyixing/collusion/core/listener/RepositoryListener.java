package io.github.guoyixing.collusion.core.listener;

import io.github.guoyixing.collusion.core.EsSyncRepository;
import io.github.guoyixing.collusion.core.handler.RepositoryHandler;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * JpaRepository的后置处理器
 *
 * @author 敲代码的旺财
 * @date 9/7/2022 下午9:59
 */
@Order(1)
@Component
public class RepositoryListener implements ApplicationListener<ApplicationStartedEvent>, ApplicationContextAware {

    private RepositoryHandler repositoryHandler;

    /**
     * spring的容器
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        this.repositoryHandler = applicationContext.getBean(RepositoryHandler.class);
        Map<String, JpaRepository> beansOfType = applicationContext.getBeansOfType(JpaRepository.class);
        //这层循环的是所有的JpaRepository，这里拿到的不是我们自己写的接口，是SimpleJpaRepository对象，这个对象会实现我们自己写的接口
        for (JpaRepository value : beansOfType.values()) {
            Class<?> beanClass = value.getClass();
            //循环这个对象的所有接口，判断是否实现了EsSyncRepository接口，即通过判断是否实现了EsSyncRepository接口，来找到我们自己写的Repository接口，
            jpaRepositoryFor:
            for (Class<?> repository : beanClass.getInterfaces()) {
                if (EsSyncRepository.class.isAssignableFrom(repository)) {
                    //然后通过循环获取到上面的EsSyncRepository接口
                    for (Type genericInterface : repository.getGenericInterfaces()) {
                        if (genericInterface.getTypeName().startsWith(EsSyncRepository.class.getTypeName())) {
                            //获取接口上的泛型
                            Type[] actualTypeArguments = ((ParameterizedType) genericInterface).getActualTypeArguments();
                            //获取第一个泛型
                            repositoryHandler.cacheJpaRepository((Class<?>) actualTypeArguments[0], repository);
                            //直接跳出多层循环，继续循环JpaRepository
                            break jpaRepositoryFor;
                        }
                    }
                }
            }
        }
    }
}
