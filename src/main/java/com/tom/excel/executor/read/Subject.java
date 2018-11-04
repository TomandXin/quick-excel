package com.tom.excel.executor.read;

/**
 * 观察者模式-subject
 *
 * @author tomxin
 * @date 2018-11-04
 * @since v1.0.0
 */
public interface Subject {

    /**
     * 注册excelObserver
     *
     * @param excelObserver
     */
    void register(ExcelObserver excelObserver);

    /**
     * 移除excelObserver
     *
     * @param excelObserver
     */
    void remove(ExcelObserver excelObserver);

    /**
     * 通知Observer
     */
    void notifyObserver(String[] rowContents);
}
