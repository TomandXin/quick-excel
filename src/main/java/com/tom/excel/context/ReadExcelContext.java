package com.tom.excel.context;

import com.tom.excel.annotations.ExcelReadProperty;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ClassMeta;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.exceptions.ExcelException;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.executor.observer.EventFactory;
import com.tom.excel.executor.observer.ParseMessageReceiver;
import com.tom.excel.executor.read.ExcelEventListener;
import com.tom.excel.executor.read.ReadExcelBaseExecutor;
import com.tom.excel.executor.read.ReadExcelExecutor;
import com.tom.excel.executor.read.ReadV3ExcelExecutor;
import com.tom.excel.strategy.BaseParseStrategy;
import com.tom.excel.strategy.Strategy;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    private EventFactory eventFactory;

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
        // 初始化EventFactory
        initEventFactory();
        // 初始化执行器
        initExecutor();
        // 初始化类的元数据
        initClassMeta();
        // 初始化监听事件
        initReceiver();
    }

    private void initEventFactory() {
        eventFactory = EventFactory.getInstance();
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
        // filed name
        Map<String, Class<? extends Strategy>> strategyClassMap = new HashMap<>(32);
        // 封装Field信息
        for (Field field : targetClass.getDeclaredFields()) {
            // 判断是否有注解
            ExcelReadProperty excelReadProperty = field.getAnnotation(ExcelReadProperty.class);
            if (null == excelReadProperty) {
                continue;
            }
            // filed index,filed name map
            filedNameMap.putIfAbsent(excelReadProperty.columnIndex(), field.getName());
            // 判断是否继承了BaseParseStrategy解析策略，如果继承了该策略则不做任何处理
            if (!BaseParseStrategy.class.isAssignableFrom(excelReadProperty.parseStrategy())) {
                Class<? extends Strategy> strategyClazz = excelReadProperty.parseStrategy();
                // filed name,strategy class
                strategyClassMap.putIfAbsent(field.getName(), strategyClazz);
            }
        }
        // ClassMeta实体类赋值
        createClassMeta(filedNameMap, strategyClassMap);
    }

    /**
     * 创建ClassMeta实体类
     *
     * @param filedNameMap
     * @param strategyClassMap
     */
    private void createClassMeta(Map<Integer, String> filedNameMap, Map<String, Class<? extends Strategy>> strategyClassMap) {
        classMeta = new ClassMeta();
        // 实体类赋值
        try {
            classMeta.setTarget(targetClass.newInstance());
        } catch (Exception e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
        classMeta.setClazz(targetClass);

        classMeta.setFieldNameMap(filedNameMap);

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

    /**
     * 读取Excel文件内容
     *
     * @param excelEventListener
     */
    public void read(ExcelEventListener excelEventListener) {
        // 设置后置处理器
        parseMessageReceiver.setExcelEventListener(excelEventListener);
        // 开始解析内容
        readExcelBaseExecutor.parse(this);
    }

    /**
     * 初始化消息接收者
     */
    private void initReceiver() {
        parseMessageReceiver = new ParseMessageReceiver(classMeta, eventFactory);
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

    public EventFactory getEventFactory() {
        return this.eventFactory;
    }
}
