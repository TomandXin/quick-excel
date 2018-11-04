package com.tom.excel.executor.read;

/**
 * 观察者的抽象类
 *
 * @author tomxin
 * @date 2018-11-04
 * @since v1.0.0
 */
public interface ExcelObserver {

    /**
     * 事例化实体类方法
     *
     * @param rowContents
     */
    void instance(String[] rowContents);
}
