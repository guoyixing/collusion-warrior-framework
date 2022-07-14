package io.github.guoyixing.collusion.core.scanner.registrar;

import io.github.guoyixing.collusion.core.annotation.composition.EsCompositionScan;
import io.github.guoyixing.collusion.core.scanner.CompositionScanner;
import io.github.guoyixing.collusion.error.EsCompositionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 在扫描阶段生成Es组合对象及其对应的Repository
 *
 * @author 敲代码的旺财
 * @date 2022/7/14 11:07
 */
public class CompositionScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    /**
     * 注册bean定义，在这里进行扫描
     *
     * @param importingClassMetadata 通过注解注入的的那个注解的元数据
     * @param registry               bean的注册器
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes esCompositionScan = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EsCompositionScan.class.getName()));
        if (esCompositionScan == null) {
            //没有添加扫描注解，当做没有启动这个功能
            return;
        }
        List<String> basePackages = getBasePackages(esCompositionScan);

        if (basePackages.size() == 0) {
            //如果没有设置扫描路径,就是扫描注解所在类同级的类，和下级所有的包
            String annotationPackage = ((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName();
            basePackages.add(annotationPackage);
        }

        //创建扫描器
        CompositionScanner compositionScanner = new CompositionScanner(registry, false);

        if (resourceLoader != null) {
            compositionScanner.setResourceLoader(resourceLoader);
        }

        //扫描
        Set<BeanDefinitionHolder> beanDefinitionHolders = compositionScanner.doScan(basePackages.toArray(new String[0]));

        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> clazz;
            try {
                clazz = Class.forName(beanClassName);
            } catch (ClassNotFoundException e) {
                throw new EsCompositionException("扫描ES组合类失败，无法获取到"+beanClassName,e);
            }

            Class<?> superclass = clazz.getSuperclass();
            if (superclass == null){
                throw new EsCompositionException("创建ES组合类失败，组合对象必须继承一个基础类");
            }

            //TODO 还需要校验是不是泛型，有没有添加EsCompositionField，需要判断加了这个注解的是集合还是普通类型
        }

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    /**
     * 获取扫描的路径
     *
     * @param esCompositionScan 扫描注解
     * @return 扫描注解上的路径
     */
    private List<String> getBasePackages(AnnotationAttributes esCompositionScan) {
        String[] esCompositionScanBasePackages = esCompositionScan.getStringArray("basePackages");
        String[] esCompositionScanValues = esCompositionScan.getStringArray("value");

        List<String> basePackages = new ArrayList<>(esCompositionScanValues.length + esCompositionScanBasePackages.length);
        if (esCompositionScanBasePackages.length > 0) {
            basePackages.addAll(Arrays.asList(esCompositionScanBasePackages));
        }
        if (esCompositionScanValues.length > 0) {
            basePackages.addAll(Arrays.asList(esCompositionScanValues));
        }
        return basePackages;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
