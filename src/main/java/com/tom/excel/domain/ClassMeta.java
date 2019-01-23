package com.tom.excel.domain;

import com.tom.excel.strategy.Strategy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 类的元数据实体类
 *
 * @author tomxin
 * @date 2018-11-13
 * @since v1.0.0
 */
public class ClassMeta {

    /**
     * 实体类
     */
    private Object target;

    /**
     * 类的Class信息
     */
    private Class<?> clazz;

    /**
     * Field Map
     */
    private Map<Integer, String> fieldNameMap;

    /**
     * strategy class map
     */
    private Map<String, Class<? extends Strategy>> strategyClassMap;

    public Map<String, Class<? extends Strategy>> getStrategyClassMap() {
        return strategyClassMap;
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public Map<Integer, String> getFieldNameMap() {
        return this.fieldNameMap;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setFieldNameMap(Map<Integer, String> fieldNameMap) {
        this.fieldNameMap = fieldNameMap;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setStrategyClassMap(Map<String, Class<? extends Strategy>> strategyClassMap) {
        this.strategyClassMap = strategyClassMap;
    }
}
