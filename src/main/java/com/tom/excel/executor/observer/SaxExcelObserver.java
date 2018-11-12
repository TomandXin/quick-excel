package com.tom.excel.executor.observer;

import com.tom.excel.annotations.ExcelReadProperty;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SAX模式下读取Excel文件内容的观察者
 *
 * @author tomxin
 * @date 2018-11-04
 * @since v1.0.0
 */
public class SaxExcelObserver{

    private Object targetObject;

    public SaxExcelObserver(Object targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * 根据从Excel文件中读取的每行的内容事例化实体类
     *
     * @param rowContents
     */
    public void instance(String[] rowContents) {
        Class<?> targetClazz = targetObject.getClass();
        // 属性列表
        Field[] fields = targetClazz.getFields();
        try {
            for (Field field : fields) {
                // 判断是否有注解
                if (!field.isAnnotationPresent(ExcelReadProperty.class)) {
                    continue;
                }
                ExcelReadProperty excelReadProperty = field.getAnnotation(ExcelReadProperty.class);
                // 根据需要判断每个属性对应的序列号
                Class<?> strategyClazz = excelReadProperty.parseStrategy();
                Class<?>[] parameters = new Class<?>[]{String.class};
                Method method = strategyClazz.getMethod("parse", parameters);
                Object valueResult = method.invoke(strategyClazz.newInstance(), rowContents[1]);
                // 属性赋值
                BeanUtils.setProperty(targetObject,field.getName(),valueResult);
            }
        } catch (NoSuchMethodException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }
}
