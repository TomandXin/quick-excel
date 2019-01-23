package com.tom.excel.context;


import com.tom.excel.annotations.ExcelWriteProperty;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ExcelSheet;
import com.tom.excel.domain.SheetMeta;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.executor.write.WriteExcelBaseExecutor;
import com.tom.excel.executor.write.WriteExcelExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
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
     * save model's field and field index
     */
    private Map<Integer, String> fieldMap = new HashMap<>(32);

    /**
     * save relation of column index and column name map
     */
    private Map<Integer, String> columnMap = new HashMap<>(32);

    /**
     * workbook
     */
    private Workbook workbook;

    /**
     * Excel处理器
     */
    private WriteExcelBaseExecutor writeExcelBaseExecutor;

    /**
     * 带写入的实体类List
     */
    private List<? extends BaseModel> models;

    /**
     * Sheet元数据
     */
    private SheetMeta sheetMeta;

    /**
     * Sheet Map (sheetName,Sheet)
     */
    private static final Map<String, SheetMeta> SHEET_META_MAP = new HashMap<>(8);

    private static final String DEFAULT_NAME = "defaultName";

    /**
     * construct method
     *
     * @param outputStream
     * @param excelTypeEnum
     */
    public WriteExcelContext(OutputStream outputStream, ExcelTypeEnum excelTypeEnum) {
        this.excelOutputStream = outputStream;
        this.excelTypeEnum = excelTypeEnum;
        // 初始化Excel上下文
        initWriteExcelContext();
    }

    private void initWriteExcelContext() {
        // 初始化Excel Sheet
        initExcel();
    }

    private void initExcel() {
        // 判断待生成的Excel文件版本
        if (ExcelTypeEnum.isXls(excelTypeEnum)) {
            workbook = new HSSFWorkbook();
        } else if (ExcelTypeEnum.isXlsx(excelTypeEnum)) {
            workbook = new SXSSFWorkbook(256);
        }
        // 初始化Excel文件执行器
        writeExcelBaseExecutor = new WriteExcelExecutor();
    }

    /**
     * 创建Sheet,创建多个Sheet
     *
     * @param excelSheet
     */
    private void createSheet(ExcelSheet excelSheet) {
        // 获取Sheet名称
        String sheetName = StringUtils.isEmpty(excelSheet.getSheetName()) ? DEFAULT_NAME : excelSheet.getSheetName();
        SheetMeta sheetMeta = SHEET_META_MAP.get(sheetName);
        // 判空
        if (null != sheetMeta && null != sheetMeta.getSheet()) {
            this.sheetMeta = sheetMeta;
            return;
        }
        Sheet sheet;
        if (StringUtils.isEmpty(excelSheet.getSheetName())) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(excelSheet.getSheetName());
        }
        sheetMeta = new SheetMeta(sheet, 0, sheetName, excelSheet.isNeedHeader());
        SHEET_META_MAP.put(sheetName, sheetMeta);
        this.sheetMeta = sheetMeta;
    }

    /**
     * 初始化属性信息
     *
     * @param excelSheet
     */
    private void initFieldMap(ExcelSheet excelSheet) {
        Class<? extends BaseModel> modelClazz = excelSheet.getModelClass();
        // 判断该类是否继承了BaseModel
        if (!BaseModel.class.isAssignableFrom(modelClazz)) {
            throw ExcelExceptionFactory.wrapException(modelClazz.getName() + "need extends " + BaseModel.class.getName(), new RuntimeException());
        }
        for (Field field : modelClazz.getDeclaredFields()) {
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
     * @param excelSheet
     * @param models
     */
    public void write(List<? extends BaseModel> models, ExcelSheet excelSheet) {
        this.models = models;
        // 创建Sheet
        createSheet(excelSheet);
        // 初始化Field Map
        initFieldMap(excelSheet);

        writeExcelBaseExecutor.write(this);
    }

    /**
     * 将Workbook的内容写入到OutputStream中
     */
    public void flush() {
        try {
            workbook.write(excelOutputStream);
        } catch (IOException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }

    public Map<Integer, String> getFieldMap() {
        return this.fieldMap;
    }

    public Map<Integer, String> getColumnMap() {
        return this.columnMap;
    }

    public List<? extends BaseModel> getModels() {
        return this.models;
    }

    public SheetMeta getSheetMeta() {
        return this.sheetMeta;
    }
}
