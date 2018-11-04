package com.tom.excel.executor.read;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * 观察者模式中具体的目标
 *
 * @author tomxin
 * @date 2018-11-04
 * @since v1.0.0
 */
public class InstanceSubject implements Subject{

    List<ExcelObserver> observers;

    public InstanceSubject() {
        observers = new ArrayList<>();
    }

    /**
     * 注册观察者
     *
     * @param observer
     */
    @Override
    public void register(ExcelObserver observer) {
        this.observers.add(observer);
    }

    /**
     * 移除观察者
     *
     * @param observer
     */
    @Override
    public void remove(ExcelObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * 通知观察者
     */
    @Override
    public void notifyObserver(String[] rowContents) {
        for(ExcelObserver observer : observers) {
            observer.instance(rowContents);
        }
    }
}
