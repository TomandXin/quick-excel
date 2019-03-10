package com.tom.excel.executor.event;

import com.tom.excel.domain.ClassMeta;
import com.tom.excel.domain.ContentMeta;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.domain.EventMessage;
import com.tom.excel.executor.read.ExcelEventListener;
import com.tom.excel.strategy.Strategy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 读取Excel文件内容的观察者
 *
 * @author tomxin
 * @date 2018-11-04
 * @since v1.0.0
 */
public class ParseMessageReceiver implements MessageReceiver {

    /**
     * 类的元数据信息
     */
    private ClassMeta classMeta;

    /**
     * 后置处理
     */
    private ExcelEventListener excelEventListener;

    /**
     * 构造函数
     *
     * @param classMeta
     * @param eventFactory
     */
    public ParseMessageReceiver(ClassMeta classMeta, EventFactory eventFactory) {
        this.classMeta = classMeta;
        // 事件监听注册
        eventFactory.register(this);
    }

    /**
     * 根据从Excel文件中读取的每行的内容事例化实体类
     *
     * @param eventMessage
     */
    public void invoke(EventMessage eventMessage) {
        // 解析出的Excel内容
        Map<Integer, ContentMeta> rowContentMap = eventMessage.getRowContentMap();
        try {
            for (Integer index : rowContentMap.keySet()) {
                ContentMeta contentMeta = rowContentMap.get(index);
                // 判空
                if (null == contentMeta || StringUtils.isBlank(contentMeta.getContent())) {
                    continue;
                }
                String fieldName = classMeta.getFieldNameMap().get(index);
                // 判空
                if (StringUtils.isEmpty(fieldName)) {
                    continue;
                }
                Class<? extends Strategy> strategyClazz = classMeta.getStrategyClassMap().get(fieldName);
                // 如果没有对应的解析策略，则直接将content赋值该对应的Filed属性
                if (null == strategyClazz) {
                    BeanUtils.setProperty(classMeta.getTarget(), fieldName, contentMeta.getContent());
                    continue;
                }
                Object valueResult = strategyClazz.newInstance().parse(contentMeta);
                // 属性赋值
                BeanUtils.setProperty(classMeta.getTarget(), fieldName, valueResult);
            }
            // 后置处理
            excelEventListener.postProcess(classMeta.getTarget());

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }

    public void setExcelEventListener(ExcelEventListener excelEventListener) {
        this.excelEventListener = excelEventListener;
    }
}
