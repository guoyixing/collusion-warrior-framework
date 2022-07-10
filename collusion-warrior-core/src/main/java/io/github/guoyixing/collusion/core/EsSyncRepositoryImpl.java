package io.github.guoyixing.collusion.core;

import io.github.guoyixing.collusion.core.handler.RepositoryHandler;
import io.github.guoyixing.collusion.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 敲代码的旺财
 * @date 2022/7/8 9:32
 */
public class EsSyncRepositoryImpl<T, ID> implements EsSyncRepository<T, ID> {

    private final RepositoryHandler repositoryHandler;

    private JpaRepository<T, ID> jpaRepository;

    public EsSyncRepositoryImpl(RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    @Override
    public <S extends T> S save(S entity) {
        //通过javassist生成带有发布功能的对象
        EsSyncRepository.setThreadLocal(entity, OperationType.SAVE);

        if (this.jpaRepository == null) {
            this.jpaRepository = (JpaRepository<T, ID>) repositoryHandler.getJpaRepository(entity.getClass());
        }
        return jpaRepository.save(entity);
    }

    @Override
    public void delete(T entity) {
        //通过javassist生成带有发布功能的对象
        EsSyncRepository.setThreadLocal(entity, OperationType.DELETE);

        if (this.jpaRepository == null) {
            this.jpaRepository = (JpaRepository<T, ID>) repositoryHandler.getJpaRepository(entity.getClass());
        }
        jpaRepository.delete(entity);
    }
}
