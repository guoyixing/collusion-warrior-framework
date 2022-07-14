package io.github.guoyixing.collusion.core.annotation.composition;

import io.github.guoyixing.collusion.core.scanner.registrar.CompositionScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * es组合对象的扫描启动类
 *
 * @author 敲代码的旺财
 * @date 2022/7/14 10:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CompositionScannerRegistrar.class)
public @interface EsCompositionScan {

    String[] value() default {};

    String[] basePackages() default {};

}
