package com.tom.excel.executor.read;

/**
 * 事件监听器
 *
 * @author tomxin
 * @date 2019-01-15
 * @since v1.0.0
 */
public interface ExcelEventListener {

    /**
     * 处理结果
     *
     * @param result
     */
    void postProcess(Object result);
}
