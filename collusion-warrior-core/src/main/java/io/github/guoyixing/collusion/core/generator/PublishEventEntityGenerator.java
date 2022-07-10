//package io.github.guoyixing.collusion.core.generator;
//
//import javassist.*;
//import javassist.bytecode.AnnotationsAttribute;
//import javassist.bytecode.ClassFile;
//import javassist.bytecode.ConstPool;
//import javassist.bytecode.annotation.Annotation;
//import org.springframework.data.domain.AfterDomainEventPublication;
//import org.springframework.data.domain.DomainEvents;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 用来给dbEntity添加上事件发布的能力
// *
// * @author 敲代码的旺财
// * @date 9/7/2022 下午8:28
// */
//public class PublishEventEntityGenerator {
//
//    private final static Object sync = new Object();
//    private static Map<Class<?>, Class<?>> clazz = new ConcurrentHashMap<>();
//
//    /**
//     * 根据db对象生成带有事件发布能力的db对象
//     *
//     * @param dbClass db对象的类型
//     * @return 带有事件发布能力的db对象
//     */
//    public static Class<?> generate(Class<?> dbClass) {
//        if (clazz.get(dbClass) != null) {
//            return clazz.get(dbClass);
//        }
//        synchronized (sync) {
//            if (clazz.get(dbClass) != null) {
//                return clazz.get(dbClass);
//            }
//
//            //生成的类的类名
//            ClassPool classPool = ClassPool.getDefault();
//            try {
//                CtClass ctClass = classPool.get(dbClass.getName());
//                ClassFile classFile = ctClass.getClassFile();
//                ConstPool constPool = classFile.getConstPool();
//
//
//                //判断DomainEvents的方法是否存在，不存在就添加
//                boolean notHasDomainEvents = true;
//                boolean notHasAfterDomainEventPublication = true;
//                for (CtMethod method : ctClass.getMethods()) {
//                    if (method.hasAnnotation(DomainEvents.class)) {
//                        notHasDomainEvents = false;
//                    }
//                    if (method.hasAnnotation(AfterDomainEventPublication.class)) {
//                        notHasAfterDomainEventPublication = false;
//                    }
//                    if (!notHasDomainEvents && !notHasAfterDomainEventPublication) {
//                        break;
//                    }
//                }
//                if (notHasDomainEvents) {
//                    addDomainEvents(ctClass, constPool);
//                }
//
//                if (notHasDomainEvents && notHasAfterDomainEventPublication) {
//                    addAfterDomainEventPublication(ctClass, constPool);
//                }
//
//                CtClass zClass = classPool.getCtClass(dbClass.getCanonicalName());
//                zClass.setSuperclass(ctClass);
//
//                Loader classLoader = new Loader(classPool);
//                // 生成新的字节码
//                Class<?> newClass = classLoader.loadClass(zClass.getName());
//
//                clazz.put(dbClass, newClass);
//                return newClass;
//            } catch (CannotCompileException e) {
//                throw new RuntimeException("生成Es对象类型失败", e);
//            } catch (NotFoundException e) {
//                throw new RuntimeException("无法查询到" + dbClass.getName() + "类", e);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    /**
//     * 添加带有DomainEvents注解的方法
//     *
//     * @param ctClass   类池
//     * @param constPool 常量池
//     */
//    private static void addDomainEvents(CtClass ctClass, ConstPool constPool) throws CannotCompileException {
//        CtMethod ctMethod = CtMethod.make(
//                "public Collection<Object> domainEvents(){\n" +
//                        "List<Object> events= new ArrayList<>();\n" +
//                        "events.add(this);\n" +
//                        "return events;\n" +
//                        "}"
//                , ctClass);
//
//        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
//        Annotation domainEventsAnnotation = new Annotation(DomainEvents.class.getCanonicalName(), constPool);
//        annotationsAttribute.addAnnotation(domainEventsAnnotation);
//        ctMethod.getMethodInfo().addAttribute(annotationsAttribute);
//        ctClass.addMethod(ctMethod);
//    }
//
//    /**
//     * 添加带有AfterDomainEventPublication注解的方法
//     *
//     * @param ctClass   类池
//     * @param constPool 常量池
//     */
//    private static void addAfterDomainEventPublication(CtClass ctClass, ConstPool constPool) throws CannotCompileException {
//        CtMethod ctMethod = CtMethod.make(
//                "public void callbackMethod() {\n" +
//                        "domainEvents().clear();\n" +
//                        "}"
//                , ctClass);
//
//        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
//        Annotation AfterDomainEventPublicationAnnotation = new Annotation(AfterDomainEventPublication.class.getCanonicalName(), constPool);
//        annotationsAttribute.addAnnotation(AfterDomainEventPublicationAnnotation);
//        ctMethod.getMethodInfo().addAttribute(annotationsAttribute);
//        ctClass.addMethod(ctMethod);
//    }
//
//}
