package com.tom.excel.executor.write;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.domain.ExcelSheet;
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

public class WriteExcelExecutor implements WriteExcelBaseExecutor {
    
    /**
     * Write Content To Excel
     *
     * @param excelContext
     * @return
     */
    @Override
    public boolean write(WriteExcelContext excelContext) {
        ExcelSheet excelSheet = excelContext.getExcelSheet();
        // 创建第一行信息
        createHeadRow(excelContext, excelSheet);
        try {
            Sheet sheet = excelSheet.getSheet();
            // 获取对应的属性值
            Map<Integer, String> fieldMap = excelContext.getFieldMap();
            int rowNum = 0;
            if (excelSheet.isNeedHeader()) {
                rowNum = 1;
            }
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
        } catch (IllegalAccessException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 创建标题行,标题行Style TODO
     *
     * @param excelContext
     * @param excelSheet
     */
    private void createHeadRow(WriteExcelContext excelContext, ExcelSheet excelSheet) {
        // 如果不需要标题行，则直接返回
        if (!excelSheet.isNeedHeader()) {
            return;
        }
        Row row = excelSheet.getSheet().createRow(0);
        Map<Integer, String> columnMap = excelContext.getColumnMap();
        for (Integer index : columnMap.keySet()) {
            Cell cell = row.createCell(index);
            cell.setCellValue(columnMap.getOrDefault(index, StringUtils.EMPTY));
        }
    }
}