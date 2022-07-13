package io.github.guoyixing.collusion.core.annotation;

/**
 * 标记es的组合对象
 *
 * @author 敲代码的旺财
 * @date 2022/7/12 15:28
 */
public @interface EsComposition {

    /**
     * 设置基类，作为底层模板的类，会将指定的类，与加上此注解的类相融合，形成ES组合对象
     */
    Class<?> basicClass();

}
