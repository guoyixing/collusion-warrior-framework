package io.github.guoyixing.collusion.core;

import io.github.guoyixing.collusion.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 敲代码的旺财
 * @date 2022/7/8 9:32
 */
public class EsSyncRepositoryImpl<T, ID> implements EsSyncRepository<T, ID> {

    private JpaRepository<T, ID> jpaRepository;

    public EsSyncRepositoryImpl(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public <S extends T> S save(S entity) {
        //TODO entity要通过javassist增加事件发布的能力
        //通过javassist生成带有发布功能的对象
        EsSyncRepository.setThreadLocal(entity, OperationType.SAVE);
        return jpaRepository.save(entity);
    }

    @Override
    public void delete(T entity) {
        EsSyncRepository.setThreadLocal(entity, OperationType.DELETE);
        jpaRepository.delete(entity);
    }
}
