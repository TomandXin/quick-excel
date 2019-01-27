package com.tom.excel.executor.event;

import com.tom.excel.domain.EventMessage;

import java.util.ArrayList;
import java.util.List;
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
    private LinkedBlockingQueue<EventMessage> eventQueue;

    /**
     * 消息接收者列表
     */
    private List<MessageReceiver> messageReceiverList;

    /**
     * 获取EventFactory实例
     *
     * @return
     */
    public static EventFactory getInstance() {
        return new EventFactory();
    }

    public EventFactory() {
        this.messageReceiverList = new ArrayList<>(32);
        this.eventQueue = new LinkedBlockingQueue<>(256);
    }

    /**
     * 观察者通过该接口注册事件
     *
     * @param messageReceiver
     */
    public void register(MessageReceiver messageReceiver) {
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
    public void notify(EventMessage eventMessage) {
        // 先放入到阻塞队列中
        eventQueue.add(eventMessage);
        // 调用消息的处理者
        EventMessage message;
        while ((message = eventQueue.poll()) != null) {
            for (MessageReceiver messageReceiver : messageReceiverList) {
                messageReceiver.invoke(message);
            }
        }
    }
}
