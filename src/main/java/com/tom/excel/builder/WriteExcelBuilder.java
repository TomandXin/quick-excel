package com.tom.excel.builder;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ExcelSheet;
import com.tom.excel.enums.ExcelTypeEnum;

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

    /**
     * init method
     */
    public void init() {
        writeExcelContext = new WriteExcelContext(outputStream, excelTypeEnum);
    }

    /**
     * 将数据写入Excel文件中
     *
     * @param models
     * @param excelSheet
     */
    public void write(List<? extends BaseModel> models, ExcelSheet excelSheet) {
        writeExcelContext.write(models, excelSheet);
    }

    /**
     * 将Workbook的内容写入到OutputStream中
     */
    public void flush() {
        writeExcelContext.flush();
    }


    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setExcelTypeEnum(ExcelTypeEnum excelTypeEnum) {
        this.excelTypeEnum = excelTypeEnum;
    }
}
