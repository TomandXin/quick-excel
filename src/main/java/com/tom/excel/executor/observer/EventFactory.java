package com.tom.excel.executor.observer;

import com.tom.excel.annotations.EventReceiver;
import com.tom.excel.executor.observer.model.EventMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

public class EventFactory {

    private static Set<Class<?>> registerList = new HashSet<>(32);

    private static BlockingQueue<EventMessage> eventQueue = new LinkedBlockingQueue<>(256);

    private static Map<Method, Object> methodObjectMap = new ConcurrentHashMap<>(32);

    /**
     * 观察者通过该接口注册事件
     *
     * @param clazz
     */
    public static void register(Class<?> clazz) {
        registerList.add(clazz);
        Object target = null;
        try {
            target = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 判空
        if (null == target) {
            return;
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            // 判断方法是否包含EventReceiver的注解
            if (!method.isAnnotationPresent(EventReceiver.class)) {
                continue;
            }
            methodObjectMap.putIfAbsent(method, target);
        }
    }

    /**
     * 通知事件 异步阻塞队列
     *
     * @param eventMessage
     */
    public static void notify(EventMessage eventMessage) {

        eventQueue.add(eventMessage);

        EventMessage message = new EventMessage();
        try {
            while ((message = eventQueue.poll()) != null) {
                for (Method method : methodObjectMap.keySet()) {
                    method.invoke(methodObjectMap.get(method), message);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
