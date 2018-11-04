package com.tom.excel.context;

import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Read Excel Context
 *
 * @author tomxin
 * @date 2018-10-21
 * @since v1.0.0
 */
public class ReadExcelContext implements ExcelContext{

    private InputStream inputStream;

    private Object targetObject;

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public Object getTargetObject() {
        return this.targetObject;
    }
}
