package com.tom.excel.executor.observer;

import com.tom.excel.domain.ClassMeta;
import com.tom.excel.domain.EventMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 事件工厂类单元测试
 *
 * @author tomxin
 * @date 2019-01-22
 * @since v1.0.0
 */
public class EventFactoryTest {

    @Test
    public void testRegister() {
        // 判断是否抛异常
        boolean registerNull = false;
        try {
            EventFactory.register(null);
        } catch (Exception e) {
            registerNull = true;
        }
        Assert.assertEquals(true, registerNull);

        // 正常插入判断List中是否存在该MessageReceiver
        EventFactory.register(new MessageReceiver() {
            @Override
            public void invoke(EventMessage eventMessage) {
                System.out.println("EventMessage One " + eventMessage);
            }
        });

        EventFactory.register(new MessageReceiver() {
            @Override
            public void invoke(EventMessage eventMessage) {
                System.out.println("EventMessage Two " + eventMessage);
            }
        });

        EventMessage eventMessage = new EventMessage();
        Map<Integer, String> rowContentMap = new HashMap<>();
        rowContentMap.put(1, "test");
        eventMessage.setRowContentMap(rowContentMap);

        EventFactory.notify(eventMessage);
    }

    @Test
    public void testNotify() {
    }
}