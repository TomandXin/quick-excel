package com.tom.excel.domain;

import com.tom.excel.enums.XSSFDataTypeEnum;

/**
 * 内容元数据
 *
 * @author tomxin
 * @date 2019-03-10
 * @since v1.0.0
 */
public class ContentMeta {

    /**
     * 内容
     */
    private String content;

    /**
     * 字符串类型
     */
    private XSSFDataTypeEnum xssfDataTypeEnum;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public XSSFDataTypeEnum getXssfDataTypeEnum() {
        return xssfDataTypeEnum;
    }

    public void setXssfDataTypeEnum(XSSFDataTypeEnum xssfDataTypeEnum) {
        this.xssfDataTypeEnum = xssfDataTypeEnum;
    }

    @Override
    public String toString() {
        return "ContentMeta{" +
                "content='" + content + '\'' +
                ", xssfDataTypeEnum=" + xssfDataTypeEnum +
                '}';
    }
}
