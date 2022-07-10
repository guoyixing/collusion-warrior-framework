package io.github.guoyixing.collusion.core;

import io.github.guoyixing.collusion.enums.OperationType;
import io.github.guoyixing.collusion.error.EsSyncException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 重写保存和删除方法，让时间监听器可以获取到是保存还是删除
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 9:28
 */
public interface EsSyncRepository<T, ID> {
    InheritableThreadLocal<Map<Object, OperationType>> esSyncRepositoryThreadLocal = new InheritableThreadLocal<>();

    Object sync = new Object();

    <S extends T> S save(S entity);

    void delete(T entity);

    static void setThreadLocal(Object entity, OperationType type) {

        if (esSyncRepositoryThreadLocal.get() == null) {
            synchronized (sync) {
                if (esSyncRepositoryThreadLocal.get() == null) {
                    esSyncRepositoryThreadLocal.set(new ConcurrentHashMap<>());
                }
            }
        }
        esSyncRepositoryThreadLocal.get().put(entity, type);
    }

    static OperationType getThreadLocal(Object entity) {
        Map<Object, OperationType> map = esSyncRepositoryThreadLocal.get();
        if (map != null) {
            return map.get(entity);
        }
        throw new EsSyncException("Es同步失败，无法获取到线程变量，无法判断操作类型");
    }

    static OperationType delThreadLocal(Object entity) {
        Map<Object, OperationType> map = esSyncRepositoryThreadLocal.get();
        if (map != null) {
            return map.remove(entity);
        }
        throw new EsSyncException("Es同步失败，无法获取到线程变量，无法判断操作类型");
    }

}
