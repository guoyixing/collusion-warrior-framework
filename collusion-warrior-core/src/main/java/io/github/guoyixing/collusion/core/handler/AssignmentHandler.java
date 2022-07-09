package io.github.guoyixing.collusion.core.handler;

/**
 * 赋值处理器
 * 用于处理db对象到Es对象的转换
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 12:26
 */
public interface AssignmentHandler<T, R> {

    /**
     * 处理赋值方式
     *
     * @param t db的对象
     * @return Es的对象
     */
    R assignment(T t,Class<R> rClass);

}
