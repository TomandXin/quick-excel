package com.tom.excel.builder;

import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;

import java.io.InputStream;

/**
 * Read Excel Builder
 *
 * @author tomxin
 * @date 2018-10-23
 * @since v1.0.0
 */
public class ReadExcelBuilder {

    private InputStream inputStream;

    private ExcelTypeEnum excelTypeEnum;

    private Class<? extends BaseModel> modelClazz;

    public void init() {

    }

    public void read() {

    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setExcelTypeEnum(ExcelTypeEnum excelTypeEnum) {
        this.excelTypeEnum = excelTypeEnum;
    }

    public void setModelClazz(Class<? extends BaseModel> modelClazz) {
        this.modelClazz = modelClazz;
    }
}
