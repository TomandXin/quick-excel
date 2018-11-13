package com.tom.excel.context;

import com.tom.excel.annotations.ExcelReadProperty;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ClassMeta;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.exceptions.ExcelException;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.executor.observer.SaxExcelObserver;
import com.tom.excel.executor.read.ReadExcelBaseExecutor;
import com.tom.excel.executor.read.ReadExcelExecutor;
import com.tom.excel.strategy.BaseParseStrategy;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read Excel Context
 *
 * @author tomxin
 * @date 2018-10-21
 * @since v1.0.0
 */
public class ReadExcelContext implements ExcelContext {

    private InputStream inputStream;

    private Object targetObject;

    private ReadExcelBaseExecutor readExcelBaseExecutor;

    private ClassMeta classMeta;

    /**
     * 初始化方法
     */
    public void init() {
        // 初始化执行器
        initExecutor();
        // 初始化类的元数据
        initClassMeta();
        // 初始化监听事件
        initObserver();
    }

    /**
     * 初始化类的元数据
     */
    private void initClassMeta() {
        // 判断该类是否继承了BaseModel类
        Class<?> targetClazz = targetObject.getClass();
        if (!targetClazz.isAssignableFrom(BaseModel.class)) {
            throw ExcelExceptionFactory.wrapException("该实体类没有继承BaseModel类", new ExcelException());
        }
        Map<Integer, String> filedNameMap = new HashMap<>(32);
        // filed name,strategy method
        Map<String, Method> strategyMethodMap = new HashMap<>(32);
        // filed name
        Map<String, Class<?>> strategyClassMap = new HashMap<>(32);
        try {
            // 封装Field信息
            for (Field field : targetClazz.getFields()) {
                // 判断是否有注解
                ExcelReadProperty excelReadProperty = field.getAnnotation(ExcelReadProperty.class);
                if (null == excelReadProperty) {
                    continue;
                }
                // filed index,filed name map
                filedNameMap.putIfAbsent(excelReadProperty.columnIndex(), field.getName());
                // 判断是否继承了BaseParseStrategy解析策略，如果继承了该策略则不做任何处理
                if (!excelReadProperty.parseStrategy().isAssignableFrom(BaseParseStrategy.class)) {
                    Class<?> strategyClazz = excelReadProperty.parseStrategy();
                    Class<?>[] parameters = new Class<?>[]{String.class};
                    Method method = strategyClazz.getMethod("parse", parameters);
                    // filed name,strategy method
                    strategyMethodMap.putIfAbsent(field.getName(), method);
                    // filed name,strategy class
                    strategyClassMap.putIfAbsent(field.getName(), strategyClazz);
                }
            }
            // ClassMeta实体类赋值
            createClassMeta(targetClazz, filedNameMap, strategyMethodMap, strategyClassMap);

        } catch (NoSuchMethodException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }

    /**
     * 创建ClassMeta实体类
     *
     * @param targetClazz
     * @param filedNameMap
     * @param strategyMethodMap
     * @param strategyClassMap
     */
    private void createClassMeta(Class<?> targetClazz, Map<Integer, String> filedNameMap, Map<String, Method> strategyMethodMap, Map<String, Class<?>> strategyClassMap) {
        classMeta = new ClassMeta();
        // 实体类赋值
        classMeta.setTarget(targetObject);

        classMeta.setClazz(targetClazz);

        classMeta.setFieldNameMap(filedNameMap);

        classMeta.setStrategyMethodMap(strategyMethodMap);

        classMeta.setStrategyClassMap(strategyClassMap);
    }

    private void initExecutor() {
        readExcelBaseExecutor = new ReadExcelExecutor();
    }

    public void read() {
        readExcelBaseExecutor.parse(this);
    }

    private void initObserver() {
        SaxExcelObserver saxExcelObserver = new SaxExcelObserver(classMeta);
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public Object getTargetObject() {
        return this.targetObject;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }
}
