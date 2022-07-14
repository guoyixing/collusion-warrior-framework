package io.github.guoyixing.collusion.core.handler;

import io.github.guoyixing.collusion.core.generator.EsEntityGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * es对象的处理器
 *
 * @author 敲代码的旺财
 * @date 2022/7/14 9:35
 */
public class EsEntityHandler {

    private final static Object sync = new Object();

    /**
     * 原始entity与生成的EsEntity的关系
     */
    private static Map<Class<?>, Class<?>> clazz = new ConcurrentHashMap<>();

    /**
     * 根据原始对象生成es对象
     *
     * @param sourceClazz 原始对象
     * @return es对象
     */
    public static Class<?> getEsEntity(Class<?> sourceClazz) {
        if (clazz.get(sourceClazz) != null) {
            return clazz.get(sourceClazz);
        }
        synchronized (sync) {
            if (clazz.get(sourceClazz) != null) {
                return clazz.get(sourceClazz);
            }
            Class<?> esEntity = EsEntityGenerator.generate(sourceClazz);
            clazz.put(sourceClazz, esEntity);
            return esEntity;
        }
    }

}
