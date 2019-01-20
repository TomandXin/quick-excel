package com.tom.excel.context;


import com.tom.excel.annotations.ExcelWriteProperty;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.executor.write.WriteExcelBaseExecutor;
import com.tom.excel.executor.write.WriteV3ExcelExecutor;
import com.tom.excel.executor.write.WriteV7ExcelExecutor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * write excel context
 *
 * @author tomxin
 * @date 2018-10-21
 * @since v1.0.0
 */
public class WriteExcelContext implements ExcelContext {

    /**
     * Excel OutputStream
     */
    private OutputStream excelOutputStream;

    /**
     * Excel Type
     */
    private ExcelTypeEnum excelTypeEnum;

    /**
     * Sheet
     */
    private Sheet sheet;

    /**
     * save model's field and field index
     */
    private Map<Integer, String> fieldMap = new HashMap<>(32);

    /**
     * save relation of column index and column name map
     */
    private Map<Integer, String> columnMap = new HashMap<>(32);

    /**
     * target model
     */
    private Class<?> modelClazz;

    /**
     * workbook
     */
    private Workbook workbook;

    private WriteExcelBaseExecutor writeExcelBaseExecutor;

    private List<? extends BaseModel> models;

    /**
     * construct method
     *
     * @param outputStream
     * @param modelClazz
     * @param excelTypeEnum
     */
    public WriteExcelContext(OutputStream outputStream, ExcelTypeEnum excelTypeEnum, Class<?> modelClazz) {
        this.excelOutputStream = outputStream;
        this.excelTypeEnum = excelTypeEnum;
        this.modelClazz = modelClazz;

        initWriteExcelContext();
    }

    private void initWriteExcelContext() {
        // 初始化Excel Sheet
        initExcelSheet();
        // 初始化Field Map
        initFieldMap();
    }

    private void initExcelSheet() {

        if (ExcelTypeEnum.isXlsx(excelTypeEnum)) {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet();
            writeExcelBaseExecutor = new WriteV7ExcelExecutor();
            return;
        }

        if (ExcelTypeEnum.isXls(excelTypeEnum)) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet();
            writeExcelBaseExecutor = new WriteV3ExcelExecutor();
        }
    }

    private void initFieldMap() {
        // 判断该类是否继承了BaseModel
        if (!BaseModel.class.isAssignableFrom(modelClazz)) {
            throw ExcelExceptionFactory.wrapException(modelClazz.getName() + "need extends " + BaseModel.class.getName(), new RuntimeException());
        }
        for (Field field : modelClazz.getFields()) {
            if (!field.isAnnotationPresent(ExcelWriteProperty.class)) {
                continue;
            }
            ExcelWriteProperty excelWriteProperty = field.getAnnotation(ExcelWriteProperty.class);
            fieldMap.put(excelWriteProperty.columnIndex(), field.getName());
            columnMap.put(excelWriteProperty.columnIndex(), excelWriteProperty.columnName());
        }
    }

    /**
     * 将内容写入Excel
     *
     * @param models
     */
    public void write(List<? extends BaseModel> models) {
        this.models = models;
        writeExcelBaseExecutor.write(this);
    }

    public OutputStream getExcelOutputStream() {
        return this.excelOutputStream;
    }

    public ExcelTypeEnum getExcelTypeEnum() {
        return this.excelTypeEnum;
    }

    public Sheet getSheet() {
        return this.sheet;
    }

    public Map<Integer, String> getFieldMap() {
        return this.fieldMap;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Map<Integer, String> getColumnMap() {
        return this.columnMap;
    }

    public List<? extends BaseModel> getModels() {
        return this.models;
    }
}
