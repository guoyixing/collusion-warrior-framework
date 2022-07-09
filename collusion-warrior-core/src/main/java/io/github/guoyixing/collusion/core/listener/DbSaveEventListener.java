package io.github.guoyixing.collusion.core.listener;

import io.github.guoyixing.collusion.core.EsSyncRepository;
import io.github.guoyixing.collusion.core.annotation.EsEntity;
import io.github.guoyixing.collusion.core.generator.EsEntityGenerator;
import io.github.guoyixing.collusion.core.handler.AssignmentHandler;
import io.github.guoyixing.collusion.core.handler.DefaultAssignmentHandler;
import io.github.guoyixing.collusion.core.handler.EsIndexHandler;
import io.github.guoyixing.collusion.core.handler.EsRepositoryHandler;
import io.github.guoyixing.collusion.enums.OperationType;
import io.github.guoyixing.collusion.error.EsSyncException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 数据库调用save方法后的事件监听器
 * insert和update都会调用save
 *
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:07
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class DbSaveEventListener {

    private final EsRepositoryHandler esRepositoryHandler;

    private final EsIndexHandler esIndexHandler;

    public DbSaveEventListener(EsRepositoryHandler esRepositoryHandler, EsIndexHandler esIndexHandler) {
        this.esRepositoryHandler = esRepositoryHandler;
        this.esIndexHandler = esIndexHandler;
    }

    @Async
    @TransactionalEventListener
    public void handlePersonSavedEvent(Object obj) {
        Class<?> clazz = obj.getClass();
        EsEntity annotation = clazz.getAnnotation(EsEntity.class);

        //es对象的类型
        Class<?> esClass;
        //转换器类型
        Class<?> assignmentHandlerClazz;
        if (annotation == null) {
            //自动生成es的类型
            esClass = EsEntityGenerator.generate(clazz);
            assignmentHandlerClazz = DefaultAssignmentHandler.class;
        } else {
            //获取需要转换的ES类型
            esClass = annotation.value();
            assignmentHandlerClazz = annotation.assignmentHandler();
        }

        //判断这个对象索引是否存在如果不存在就插入
        esIndexHandler.existsAndCreate(esClass);


        OperationType type = EsSyncRepository.getThreadLocal(obj);
        if (type.equals(OperationType.SAVE)) {
            try {
                AssignmentHandler assignmentHandler = (AssignmentHandler) assignmentHandlerClazz.newInstance();
                Object assignmentValue = assignmentHandler.assignment(obj, esClass);

                //获取esRepository,如果不存在就生成一个
                ElasticsearchRepository elasticsearchRepository = esRepositoryHandler.getElasticsearchRepository(assignmentValue.getClass());
                elasticsearchRepository.save(assignmentValue);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new EsSyncException(assignmentHandlerClazz.getName() + "赋值器初始化失败", e);
            }

        }
        EsSyncRepository.delThreadLocal(obj);
        log.debug("数据库操作类型{}", type);
        log.debug("数据库操作数据{}", obj);

    }
}
