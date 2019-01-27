package com.tom.excel.builder;

import com.tom.excel.context.ReadExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.executor.read.ExcelEventListener;

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

    private ReadExcelContext readExcelContext;

    public ReadExcelBuilder init() {
        readExcelContext = new ReadExcelContext(inputStream, modelClazz, excelTypeEnum);
        return this;
    }

    public void read(ExcelEventListener excelEventListener) {
        readExcelContext.read(excelEventListener);
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
