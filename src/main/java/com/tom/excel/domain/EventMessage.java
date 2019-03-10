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
    private Map<Integer, ContentMeta> rowContentMap;

    /**
     * 行号
     */
    private Integer rowNumber;

    public Map<Integer, ContentMeta> getRowContentMap() {
        return rowContentMap;
    }

    public void setRowContentMap(Map<Integer, ContentMeta> rowContentMap) {
        this.rowContentMap = rowContentMap;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "rowContentMap=" + rowContentMap +
                ", rowNumber=" + rowNumber +
                '}';
    }
}
