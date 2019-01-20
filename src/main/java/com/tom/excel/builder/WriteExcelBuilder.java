package com.tom.excel.builder;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.executor.write.WriteExcelBaseExecutor;

import java.io.OutputStream;
import java.util.List;

/**
 * Write Excel Builder
 *
 * @author tomxin
 * @date 2018-10-21
 * @since v1.0.0
 */
public class WriteExcelBuilder {

    private OutputStream outputStream;

    private ExcelTypeEnum excelTypeEnum;

    private WriteExcelContext writeExcelContext;

    private Class<?> modelClazz;

    private WriteExcelBaseExecutor writeExcelExecutor;

    /**
     * init method
     */
    public void init() {
        writeExcelContext = new WriteExcelContext(outputStream, excelTypeEnum, modelClazz);
    }

    public void write(List<? extends BaseModel> models) {
        writeExcelContext.write(models);
    }


    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setExcelTypeEnum(ExcelTypeEnum excelTypeEnum) {
        this.excelTypeEnum = excelTypeEnum;
    }

    public void setModelClazz(Class<?> modelClazz) {
        this.modelClazz = modelClazz;
    }
}
