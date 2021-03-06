package io.github.guoyixing.collusion.core.annotation.composition;

import java.lang.annotation.*;

/**
 * ES组合字段标记关联的对象
 *
 * @author 敲代码的旺财
 * @date 2022/7/13 14:39
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EsCompositionField {
}
