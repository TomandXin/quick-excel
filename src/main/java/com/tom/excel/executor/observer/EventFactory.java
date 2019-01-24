package com.tom.excel.executor.observer;

import com.tom.excel.domain.EventMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 事件工厂类
 *
 * @author tomxin
 * @date 2019-01-24
 * @since v1.0.0
 */
public class EventFactory {

    /**
     * 消息队列
     */
    private static BlockingQueue<EventMessage> eventQueue = new LinkedBlockingQueue<>(256);

    /**
     * 消息接收者列表
     */
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
     * 通知事件 异步阻塞队列 TODO 优化消息分发方式
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
