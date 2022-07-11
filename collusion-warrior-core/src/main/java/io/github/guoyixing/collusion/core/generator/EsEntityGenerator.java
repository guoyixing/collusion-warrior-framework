package io.github.guoyixing.collusion.core.generator;

import io.github.guoyixing.collusion.utils.NameConversionUtils;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当没有指定Es对象的时候，动态生成es对象
 *
 * @author 敲代码的旺财
 * @date 2022/7/8 13:54
 */
public class EsEntityGenerator {

    private final static Object sync = new Object();
    private static Map<Class<?>, Class<?>> clazz = new ConcurrentHashMap<>();

    /**
     * 根据db对象生成es对象
     *
     * @param dbClass db对象的类型
     * @return es对象的类型
     */
    public static Class<?> generate(Class<?> dbClass) {

        if (clazz.get(dbClass) != null) {
            return clazz.get(dbClass);
        }
        synchronized (sync) {
            if (clazz.get(dbClass) != null) {
                return clazz.get(dbClass);
            }
            //生成的类的类名
            String className = dbClass.getCanonicalName() + "_EsDto";

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(className);

            ClassFile classFile = ctClass.getClassFile();
            ConstPool constPool = classFile.getConstPool();

            //类添加注解
            addAnnotation(dbClass, classFile, constPool);

            List<CtField> ctFields = new ArrayList<>();
            //根据db对象属性生成的Es对象属性
            for (Field field : getDbAllField(dbClass)) {
                field.setAccessible(true);
                try {
                    //添加属性
                    ctFields.add(addCtField(pool, ctClass, constPool, field));
                } catch (CannotCompileException | NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                for (CtField ctField : ctFields) {
                    //生成对应的getter/setter方法
                    String methodName = ctField.getName().substring(0, 1).toUpperCase(Locale.ROOT) + ctField.getName().substring(1);
                    ctClass.addMethod(CtNewMethod.setter("set" + methodName, ctField));
                    ctClass.addMethod(CtNewMethod.getter("get" + methodName, ctField));
                }
                Class<?> esClass = ctClass.toClass();
                clazz.put(dbClass, esClass);
                return esClass;
            } catch (CannotCompileException e) {
                throw new RuntimeException("生成Es对象类型失败", e);
            }
        }
    }

    /**
     * 类添加注解
     *
     * @param dbClass   db对象的类型
     * @param classFile 生成的Es对象文件
     * @param constPool 常量池
     */
    private static void addAnnotation(Class<?> dbClass, ClassFile classFile, ConstPool constPool) {
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation documentAnnotation = new Annotation(org.springframework.data.elasticsearch.annotations.Document.class.getCanonicalName(), constPool);

        //添加注解里面的属性
        documentAnnotation.addMemberValue("indexName", new StringMemberValue(NameConversionUtils.camelCase2UnderScoreCase(dbClass.getSimpleName()), constPool));
        annotationsAttribute.addAnnotation(documentAnnotation);
        classFile.addAttribute(annotationsAttribute);
    }

    /**
     * 生成Es对象的属性
     *
     * @param pool      类池
     * @param ctClass   生成的类对象
     * @param constPool 常量池
     * @param field     db对象的属性
     */
    private static CtField addCtField(ClassPool pool, CtClass ctClass, ConstPool constPool, Field field) throws CannotCompileException, NotFoundException {
        String name = field.getName();

        CtField ctField = new CtField(pool.get(field.getType().getCanonicalName()), name, ctClass);
        ctField.setModifiers(Modifier.PRIVATE);

        //在属性上添加注解
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation fieldAnnotation = new Annotation(org.springframework.data.elasticsearch.annotations.Field.class.getCanonicalName(), constPool);

        //添加注解里面的属性
        StringMemberValue stringMemberValue = new StringMemberValue(NameConversionUtils.camelCase2UnderScoreCase(name), constPool);
        fieldAnnotation.addMemberValue("name", stringMemberValue);
        fieldAnnotation.addMemberValue("value", stringMemberValue);
        annotationsAttribute.addAnnotation(fieldAnnotation);

        if (field.getAnnotation(Id.class) != null
                || field.getAnnotation(javax.persistence.Id.class) != null) {
            Annotation idAnnotation = new Annotation(Id.class.getCanonicalName(), constPool);
            annotationsAttribute.addAnnotation(idAnnotation);
        }

        ctField.getFieldInfo().addAttribute(annotationsAttribute);
        ctClass.addField(ctField);
        return ctField;
    }

    /**
     * 获取db对象所有的属性，包括父类中的属性
     *
     * @param dbClass db对象的类型
     * @return db对象中所有的属性
     */
    private static List<Field> getDbAllField(Class<?> dbClass) {
        //获取db对象所有的属性
        List<Field> allField = new ArrayList<>();
        Class<?> useGetFieldClass = dbClass;
        while (useGetFieldClass != null) {
            Field[] fields = useGetFieldClass.getDeclaredFields();
            allField.addAll(Arrays.asList(fields));
            useGetFieldClass = useGetFieldClass.getSuperclass();
        }
        return allField;
    }

}
