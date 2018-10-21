package com.tom.excel.builder;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.executor.write.WriteExcelExecutor;

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

    private WriteExcelExecutor writeExcelExecutor;

    public void init() {
        writeExcelContext = new WriteExcelContext(outputStream, excelTypeEnum, modelClazz);
    }

    public void write(List<? extends BaseModel> models) {

        writeExcelExecutor = new WriteExcelExecutor(models);

        writeExcelExecutor.write(writeExcelContext);

        writeExcelExecutor.postProcess(writeExcelContext);
    }
}
