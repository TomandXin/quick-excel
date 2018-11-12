package com.tom.excel.executor.observer;

import com.tom.excel.annotations.EventReceiver;
import com.tom.excel.executor.observer.model.EventMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class EventFactory {

    private static Set<Class<?>> registerList = new HashSet<>();

    private static BlockingQueue<EventMessage> eventQueue = new LinkedBlockingQueue<>();

    /**
     * 观察者通过该接口注册事件
     *
     * @param clazz
     */
    public static void register(Class<?> clazz) {
        registerList.add(clazz);
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
                for (Class<?> clazz : registerList) {
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(EventReceiver.class)) {
                            method.invoke(clazz.newInstance(), message);
                        }
                    }
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
