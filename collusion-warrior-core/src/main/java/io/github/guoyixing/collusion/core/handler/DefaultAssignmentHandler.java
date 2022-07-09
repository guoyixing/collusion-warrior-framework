package io.github.guoyixing.collusion.core.handler;

import io.github.guoyixing.collusion.error.EsSyncException;
import org.springframework.beans.BeanUtils;

/**
 * 默认的赋值处理方法
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 12:30
 */
public class DefaultAssignmentHandler<R> implements AssignmentHandler<Object, R> {

    /**
     * 处理赋值方式
     *
     * @param source db的对象
     * @return Es的对象
     */
    @Override
    public R assignment(Object source,Class<R> rClass) {
        try {
            R result = rClass.newInstance();
            BeanUtils.copyProperties(source,result);
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new EsSyncException(source.getClass().getName()+"对应的Es对象创建失败", e);
        }
    }

}
