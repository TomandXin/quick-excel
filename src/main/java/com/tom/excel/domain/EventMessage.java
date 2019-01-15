package com.tom.excel.domain;

import java.util.Map;

/**
 * 传递事件内容的消息体
 *
 * @author tomxin
 * @date 2018-11-13
 * @since v1.0.0
 */
public class EventMessage {

    /**
     * Excel中每行的内容
     */
    private Map<Integer, String> rowContentMap;

    public Map<Integer, String> getRowContentMap() {
        return rowContentMap;
    }

    public void setRowContentMap(Map<Integer, String> rowContentMap) {
        this.rowContentMap = rowContentMap;
    }
}
