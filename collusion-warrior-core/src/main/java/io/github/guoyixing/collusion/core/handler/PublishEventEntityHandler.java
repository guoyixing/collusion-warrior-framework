//package io.github.guoyixing.collusion.core.handler;
//
//import io.github.guoyixing.collusion.core.generator.PublishEventEntityGenerator;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Component;
//
///**
// * @author 敲代码的旺财
// * @date 9/7/2022 下午9:04
// */
//@Component
//public class PublishEventEntityHandler {
//
//    /**
//     * 获取带有事件发布能力的对象
//     *
//     * @param obj 原本的entity对象
//     * @return 带有时间发布能力的entity
//     */
//    public Object getPublishEventEntity(Object obj) {
//        Class<?> publishEventEntity = PublishEventEntityGenerator.generate(obj.getClass());
//        try {
//            Object publishEventObj = publishEventEntity.newInstance();
//            BeanUtils.copyProperties(obj, publishEventObj);
//            return publishEventObj;
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
