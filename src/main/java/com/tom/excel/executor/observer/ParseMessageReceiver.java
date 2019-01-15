package com.tom.excel.executor.observer;

import com.tom.excel.annotations.EventReceiver;
import com.tom.excel.domain.ClassMeta;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.domain.EventMessage;
import com.tom.excel.executor.read.ExcelEventListener;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SAX模式下读取Excel文件内容的观察者
 *
 * @author tomxin
 * @date 2018-11-04
 * @since v1.0.0
 */
public class ParseMessageReceiver {

    private ClassMeta classMeta;

    private ExcelEventListener excelEventListener;

    public ParseMessageReceiver(ClassMeta classMeta) {
        this.classMeta = classMeta;
        // 事件监听注册
        EventFactory.register(this);
    }

    /**
     * 根据从Excel文件中读取的每行的内容事例化实体类
     *
     * @param eventMessage
     */
    @EventReceiver
    public void instance(EventMessage eventMessage) {
        // 解析出的Excel内容
        Map<Integer, String> rowContentMap = eventMessage.getRowContentMap();
        try {
            for (Integer index : rowContentMap.keySet()) {
                String content = rowContentMap.get(index);
                // 判空
                if (StringUtils.isEmpty(content)) {
                    continue;
                }
                String fieldName = classMeta.getFieldNameMap().get(index);
                // 判空
                if (StringUtils.isEmpty(fieldName)) {
                    continue;
                }
                Method strategyMethod = classMeta.getStrategyMethodMap().get(fieldName);
                Class<?> strategyClazz = classMeta.getStrategyClassMap().get(fieldName);
                // 如果没有对应的解析策略，则直接将content赋值该对应的Filed属性
                if (null == strategyClazz || null == strategyMethod) {
                    BeanUtils.setProperty(classMeta.getTarget(), fieldName, content);
                    continue;
                }
                Object valueResult = strategyMethod.invoke(strategyClazz.newInstance(), content);
                // 属性赋值
                BeanUtils.setProperty(classMeta.getTarget(), fieldName, valueResult);
            }
        } catch (InstantiationException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }

    public void setExcelEventListener(ExcelEventListener excelEventListener) {
        this.excelEventListener = excelEventListener;
    }
}