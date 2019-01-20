package com.tom.excel.executor.observer;

import com.tom.excel.domain.EventMessage;

/**
 * 事件消息接收者
 *
 * @author tomxin
 * @date 2019-01-20
 * @since v1.0.0
 */
public interface MessageReceiver {

    /**
     * 接收事件消息
     *
     * @param eventMessage
     */
    void invoke(EventMessage eventMessage);
}
