package io.github.guoyixing.collusion.core.scanner;

import io.github.guoyixing.collusion.core.annotation.composition.EsComposition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * Es组合对象扫描器
 *
 * @author 敲代码的旺财
 * @date 2022/7/14 10:01
 */
public class CompositionScanner extends ClassPathBeanDefinitionScanner {

    public CompositionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        addIncludeFilter(new AnnotationTypeFilter(EsComposition.class));
        return super.doScan(basePackages);
    }
}
