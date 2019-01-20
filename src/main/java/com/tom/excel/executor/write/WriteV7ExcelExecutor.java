package com.tom.excel.executor.write;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class WriteV7ExcelExecutor implements WriteExcelBaseExecutor {


    /**
     * Write Excel Executor Construct
     */
    public WriteV7ExcelExecutor() {

    }

    /**
     * Write Content To Excel
     *
     * @param excelContext
     * @return
     */
    @Override
    public boolean write(WriteExcelContext excelContext) {
        Sheet sheet = excelContext.getSheet();
        // 创建第一行信息
        createHeadRow(excelContext, sheet);
        try {
            // 获取对应的属性值
            Map<Integer, String> fieldMap = excelContext.getFieldMap();
            int rowNum = 1;
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
        Workbook workbook = excelContext.getWorkbook();
        try {
            workbook.write(excelContext.getExcelOutputStream());
        } catch (IOException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 创建头文件
     *
     * @param excelContext
     * @param sheet
     */
    private void createHeadRow(WriteExcelContext excelContext, Sheet sheet) {
        Row row = sheet.createRow(0);
        Map<Integer, String> columnMap = excelContext.getColumnMap();
        for (Integer index : columnMap.keySet()) {
            Cell cell = row.createCell(index);
            cell.setCellValue(columnMap.getOrDefault(index, StringUtils.EMPTY));
        }
    }

    /**
     * post process excel
     *
     * @return
     */
    @Override
    public boolean postProcess(WriteExcelContext excelContext) {
        Workbook workbook = excelContext.getWorkbook();
        try {
            workbook.write(excelContext.getExcelOutputStream());
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return true;
    }
}