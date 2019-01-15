package com.tom.excel.context;

import com.tom.excel.annotations.ExcelReadProperty;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ClassMeta;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.exceptions.ExcelException;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.executor.observer.ParseMessageReceiver;
import com.tom.excel.executor.observer.SaxExcelObserver;
import com.tom.excel.executor.read.ExcelEventListener;
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

    private Class<? extends BaseModel> targetClass;

    private ReadExcelBaseExecutor readExcelBaseExecutor;

    private ClassMeta classMeta;

    private ExcelTypeEnum excelTypeEnum;

    private ParseMessageReceiver parseMessageReceiver;

    public ReadExcelContext(InputStream inputStream, Class targetClass, ExcelTypeEnum excelTypeEnum) {
        this.inputStream = inputStream;
        this.targetClass = targetClass;
        this.excelTypeEnum = excelTypeEnum;
        // 调用init方法，初始化上下文
        init();
    }

    /**
     * 初始化方法
     */
    public void init() {
        // 初始化执行器
        initExecutor();
        // 初始化类的元数据
        initClassMeta();
        // 初始化监听事件
        initReceiver();
    }

    /**
     * 初始化类的元数据
     */
    private void initClassMeta() {
        // 判断该类是否继承了BaseModel类
        if (!BaseModel.class.isAssignableFrom(targetClass)) {
            throw ExcelExceptionFactory.wrapException("This Class Not Extends BaseModel", new ExcelException());
        }
        Map<Integer, String> filedNameMap = new HashMap<>(32);
        // filed name,strategy method
        Map<String, Method> strategyMethodMap = new HashMap<>(32);
        // filed name
        Map<String, Class<?>> strategyClassMap = new HashMap<>(32);
        try {
            // 封装Field信息
            for (Field field : targetClass.getFields()) {
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
            createClassMeta(filedNameMap, strategyMethodMap, strategyClassMap);

        } catch (NoSuchMethodException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }

    /**
     * 创建ClassMeta实体类
     *
     * @param filedNameMap
     * @param strategyMethodMap
     * @param strategyClassMap
     */
    private void createClassMeta(Map<Integer, String> filedNameMap, Map<String, Method> strategyMethodMap, Map<String, Class<?>> strategyClassMap) {
        classMeta = new ClassMeta();
        // 实体类赋值
        try {
            classMeta.setTarget(targetClass.newInstance());
        } catch (Exception e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
        classMeta.setClazz(targetClass);

        classMeta.setFieldNameMap(filedNameMap);

        classMeta.setStrategyMethodMap(strategyMethodMap);

        classMeta.setStrategyClassMap(strategyClassMap);
    }

    private void initExecutor() {
        if (ExcelTypeEnum.isXlsx(excelTypeEnum)) {
            readExcelBaseExecutor = new ReadExcelExecutor();
        }
        if (ExcelTypeEnum.isXls(excelTypeEnum)) {
            readExcelBaseExecutor = new ReadV3ExcelExecutor();
        }
    }

    public void read(ExcelEventListener excelEventListener) {
        // 设置后置处理器
        parseMessageReceiver.setExcelEventListener(excelEventListener);
        // 开始解析内容
        readExcelBaseExecutor.parse(this);
    }

    private void initReceiver() {
        parseMessageReceiver = new ParseMessageReceiver(classMeta);
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public Object getTargetClass() {
        return this.targetClass;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }
}
