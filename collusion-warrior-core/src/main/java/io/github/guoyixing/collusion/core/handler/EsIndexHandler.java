package io.github.guoyixing.collusion.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * es的索引管理器
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 13:54
 */
@Slf4j
@Component
public class EsIndexHandler implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static List<Class<?>> existsIndex = new CopyOnWriteArrayList<>();

    /**
     * 判断索引是否存在，如果不存在则新增
     *
     * @param esClass es对象的类型
     * @return es索引的操作对象
     */
    public IndexOperations existsAndCreate(Class<?> esClass) {
        ElasticsearchOperations elasticsearchOperations = applicationContext.getBean(ElasticsearchOperations.class);
        IndexOperations indexOperations = elasticsearchOperations.indexOps(esClass);
        if (existsIndex.contains(esClass)) {
            return indexOperations;
        }
        //TODO 这里要冲配置里面读取
        Settings settings = indexOperations.createSettings(esClass);
        Document mapping = indexOperations.createMapping(esClass);
        if (!indexOperations.exists() && indexOperations.create(settings,mapping)) {
            existsIndex.add(esClass);
        }
        return indexOperations;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
