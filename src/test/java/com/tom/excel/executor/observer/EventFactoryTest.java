package com.tom.excel.executor.observer;

import com.tom.excel.domain.ContentMeta;
import com.tom.excel.domain.EventMessage;
import com.tom.excel.enums.XSSFDataTypeEnum;
import com.tom.excel.executor.event.EventFactory;
import com.tom.excel.executor.event.MessageReceiver;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
        EventFactory eventFactory = EventFactory.getInstance();
        try {
            eventFactory.register(null);
        } catch (Exception e) {
            registerNull = true;
        }
        Assert.assertEquals(true, registerNull);

        // 正常插入判断List中是否存在该MessageReceiver
        eventFactory.register(new MessageReceiver() {
            @Override
            public void invoke(EventMessage eventMessage) {
                System.out.println("EventMessage One " + eventMessage);
            }
        });

        eventFactory.register(new MessageReceiver() {
            @Override
            public void invoke(EventMessage eventMessage) {
                System.out.println("EventMessage Two " + eventMessage);
            }
        });

        EventMessage eventMessage = new EventMessage();
        Map<Integer, ContentMeta> rowContentMap = new HashMap<>();
        ContentMeta contentMeta = new ContentMeta();
        contentMeta.setContent("测试");
        contentMeta.setXssfDataTypeEnum(XSSFDataTypeEnum.SST_STRING);
        rowContentMap.put(1, contentMeta);
        eventMessage.setRowContentMap(rowContentMap);
        eventMessage.setRowNumber(1);

        eventFactory.notify(eventMessage);
    }

    @Test
    public void testNotify() {
    }
}