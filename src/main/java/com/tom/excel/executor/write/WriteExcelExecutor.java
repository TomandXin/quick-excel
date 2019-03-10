package com.tom.excel.executor.write;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ExcelSheet;
import com.tom.excel.domain.SheetMeta;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 往Excel中写内容的执行器
 *
 * @author tomxin
 * @date 2019-01-21
 * @since v1.0.0
 */
public class WriteExcelExecutor implements WriteExcelBaseExecutor {

    /**
     * Write Content To Excel
     *
     * @param excelContext
     * @return
     */
    @Override
    public boolean write(WriteExcelContext excelContext) {
        SheetMeta sheetMeta = excelContext.getSheetMeta();
        // 创建第一行信息
        createHeadRow(excelContext, sheetMeta);
        try {
            Sheet sheet = sheetMeta.getSheet();
            // 获取对应的属性值
            Map<Integer, String> fieldMap = excelContext.getFieldMap();
            int rowNum = sheetMeta.getCurrentRow();
            // 循环将属性赋值
            for (Object model : excelContext.getModels()) {
                Row currentRow = sheet.createRow(rowNum);
                for (Integer index : fieldMap.keySet()) {
                    Cell cell = currentRow.createCell(index);
                    String fieldName = fieldMap.getOrDefault(index, StringUtils.EMPTY);
                    // 属性名判空
                    if (StringUtils.isEmpty(fieldName)) {
                        cell.setCellValue(StringUtils.EMPTY);
                    }
                    String cellValue = BeanUtils.getProperty(model, fieldName);
                    cell.setCellValue(cellValue);
                }
                ++rowNum;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 创建标题行,标题行Style
     *
     * @param excelContext
     * @param sheetMeta
     */
    private void createHeadRow(WriteExcelContext excelContext, SheetMeta sheetMeta) {
        // 如果不需要标题行，则直接返回
        if (!sheetMeta.isNeedHeader() || sheetMeta.getCurrentRow() > 0) {
            return;
        }
        // 自增
        sheetMeta.setCurrentRow(sheetMeta.getCurrentRow() + 1);
        Row row = sheetMeta.getSheet().createRow(0);
        Map<Integer, String> columnMap = excelContext.getColumnMap();
        for (Integer index : columnMap.keySet()) {
            Cell cell = row.createCell(index);
            cell.setCellValue(columnMap.getOrDefault(index, StringUtils.EMPTY));
        }
    }
}