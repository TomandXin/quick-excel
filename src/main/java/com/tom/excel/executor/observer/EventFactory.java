package com.tom.excel.executor.observer;

import com.tom.excel.annotations.EventReceiver;
import com.tom.excel.domain.EventMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

public class EventFactory {

    private static BlockingQueue<EventMessage> eventQueue = new LinkedBlockingQueue<>(256);

    private static List<MessageReceiver> messageReceiverList = new ArrayList<>(32);

    /**
     * 观察者通过该接口注册事件
     *
     * @param messageReceiver
     */
    public static void register(MessageReceiver messageReceiver) {
        // 判空
        if (null == messageReceiver) {
            throw new NullPointerException("observer is null");
        }
        // 将MessageReceiver添加到列表中
        messageReceiverList.add(messageReceiver);
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
        while ((message = eventQueue.poll()) != null) {
            for (MessageReceiver messageReceiver : messageReceiverList) {
                messageReceiver.invoke(message);
            }
        }
    }
}
