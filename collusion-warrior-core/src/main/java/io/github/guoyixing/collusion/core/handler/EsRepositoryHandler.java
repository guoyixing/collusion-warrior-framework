package io.github.guoyixing.collusion.core.handler;

import io.github.guoyixing.collusion.core.generator.EsRepositoryGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

/**
 * 通过生成的esRepository接口，创建真正实现对象，注入到spring的容器中
 *
 * @author 敲代码的旺财
 * @date 8/7/2022 下午11:16
 */
@Component
public class EsRepositoryHandler implements ApplicationContextAware {

    private final static Object sync = new Object();

    /**
     * spring的容器
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ElasticsearchRepository getElasticsearchRepository(Class<?> esClass) {
        synchronized (sync) {
            ElasticsearchRepository repository;
            try {
                repository = applicationContext.getBean(esClass.getName(), ElasticsearchRepository.class);
            } catch (NoSuchBeanDefinitionException e) {
                //根据es的对象生成esRepository接口
                Class<? extends Repository> esRepositoryInterface = EsRepositoryGenerator.generate(esClass);
                //通过接口生成esRepository的实现类
                ElasticsearchRepositoryFactoryBean elasticsearchRepositoryFactoryBean = new ElasticsearchRepositoryFactoryBean<>(esRepositoryInterface);
                elasticsearchRepositoryFactoryBean.setElasticsearchOperations(applicationContext.getBean(ElasticsearchOperations.class));
                elasticsearchRepositoryFactoryBean.afterPropertiesSet();
                repository = (ElasticsearchRepository) elasticsearchRepositoryFactoryBean.getObject();

                //将实现类塞入容器中
                //将applicationContext转换为ConfigurableApplicationContext
                ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
                //获取BeanFactory
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();
                //动态注册bean.
                defaultListableBeanFactory.registerSingleton(esClass.getName(), repository);
            }
            return repository;
        }
    }
}
