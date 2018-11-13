package com.tom.excel.executor.observer;

import com.tom.excel.annotations.EventReceiver;
import com.tom.excel.executor.observer.model.EventMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

public class EventFactory {

    private static BlockingQueue<EventMessage> eventQueue = new LinkedBlockingQueue<>(256);

    private static Map<Method, Object> methodObjectMap = new ConcurrentHashMap<>(32);

    /**
     * 观察者通过该接口注册事件
     *
     * @param observer
     */
    public static void register(Object observer) {
        // 判空
        if (null == observer) {
            throw new NullPointerException("observer is null");
        }
        for (Method method : observer.getClass().getMethods()) {
            // 判断方法是否包含EventReceiver的注解
            if (!method.isAnnotationPresent(EventReceiver.class)) {
                continue;
            }
            methodObjectMap.putIfAbsent(method, observer);
        }
    }

    /**
     * 通知事件 异步阻塞队列
     *
     * @param eventMessage
     */
    public static void notify(EventMessage eventMessage) {
        // 先放入到阻塞队列中
        eventQueue.add(eventMessage);

        EventMessage message;
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
