package io.github.guoyixing.collusion.core.generator;

import io.github.guoyixing.collusion.error.EsSyncException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.SignatureAttribute;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.Repository;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 生成ESRepository的接口
 *
 * @author 敲代码的旺财
 * @date 8/7/2022 下午11:29
 */
public class EsRepositoryGenerator {

    /**
     * 生成ESRepository的接口
     *
     * @param esClass
     * @return
     */
    public static Class<? extends Repository> generate(Class<?> esClass) {
        //生成的类的类名
        String className = esClass.getCanonicalName() + "_EsRepository";
        Class<?> idClass = null;

        for (Field field : esClass.getDeclaredFields()) {
            //判断是不是主键
            if (field.getAnnotation(Id.class) != null
                    || field.getAnnotation(javax.persistence.Id.class) != null) {
                idClass = field.getType();
                break;
            }
        }

        if (idClass == null) {
            throw new EsSyncException("无法获取到" + esClass.getName() + "类的主键");
        }

        ClassPool pool = ClassPool.getDefault();
        CtClass elasticsearchRepositoryCtClass;
        try {
            elasticsearchRepositoryCtClass = pool.get(ElasticsearchRepository.class.getName());
        } catch (NotFoundException e) {
            throw new EsSyncException("生成" + esClass.getName() + "的ESRepository的接口失败，无法获取到" + ElasticsearchRepository.class.getName(), e);
        }

        CtClass ctClass = pool.makeInterface(className, elasticsearchRepositoryCtClass);

        SignatureAttribute.ClassSignature cs = new SignatureAttribute.ClassSignature(null, null,
                new SignatureAttribute.ClassType[]{
                        new SignatureAttribute.ClassType(ElasticsearchRepository.class.getName(),
                                new SignatureAttribute.TypeArgument[]{
                                        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(esClass.getCanonicalName())),
                                        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(idClass.getCanonicalName()))
                                }
                        )});

        ctClass.setGenericSignature(cs.encode());

        try {
            ctClass.writeFile("G:\\");
            return (Class<? extends Repository>) ctClass.toClass();
        } catch (CannotCompileException e) {
            throw new RuntimeException("生成EsRepository失败", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
