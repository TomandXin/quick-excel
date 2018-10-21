package com.tom.excel.context;


import com.tom.excel.annotations.ExcelWriteProperty;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.enums.ExcelTypeEnum;
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
     * Model List
     */
    private List<? extends BaseModel> models;

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

    /**
     * construct method
     *
     * @param outputStream
     * @param modelClazz
     * @param excelTypeEnum
     */
    public WriteExcelContext(OutputStream outputStream, ExcelTypeEnum excelTypeEnum, Class<?> modelClazz) {
        this.excelOutputStream = outputStream;
        this.models = models;
        this.excelTypeEnum = excelTypeEnum;
        this.modelClazz = modelClazz;

        initWriteExcelContext();
    }

    private void initWriteExcelContext() {

        initExcelSheet();

        initFieldMap();


    }

    private void initExcelSheet() {

        if (ExcelTypeEnum.isXls(excelTypeEnum)) {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet();
            return;
        }

        if (ExcelTypeEnum.isXlsx(excelTypeEnum)) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet();
        }
    }

    private void initFieldMap() {
        // is assignable from BaseModel.class
        if (!modelClazz.isAssignableFrom(BaseModel.class)) {
            throw new RuntimeException();
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

    public OutputStream getExcelOutputStream() {
        return this.excelOutputStream;
    }

    public ExcelTypeEnum getExcelTypeEnum() {
        return this.excelTypeEnum;
    }

    public List<? extends BaseModel> getModels() {
        return this.models;
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
}
