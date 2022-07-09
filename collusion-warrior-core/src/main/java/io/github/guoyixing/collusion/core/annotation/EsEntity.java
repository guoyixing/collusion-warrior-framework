package io.github.guoyixing.collusion.core.annotation;


import io.github.guoyixing.collusion.core.handler.DefaultAssignmentHandler;

import java.lang.annotation.*;

/**
 * 用来在jpa的对象上标注出关联的Es对象
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 12:21
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EsEntity {

    /**
     * 数据库实体关联的es对象
     */
    Class<?> value();

    Class<?> assignmentHandler() default DefaultAssignmentHandler.class;

}
