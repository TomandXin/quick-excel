package com.tom.excel.strategy;

/**
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public interface Strategy<T> {

    /**
     * parse method
     *
     * @param content
     * @return
     */
    Object parse(T content);
}
