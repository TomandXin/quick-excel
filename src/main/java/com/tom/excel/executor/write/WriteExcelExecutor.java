package com.tom.excel.executor.write;

import com.tom.excel.context.WriteExcelContext;
import com.tom.excel.domain.BaseModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
        try {
            Sheet sheet = excelContext.getSheet();
            List<? extends BaseModel> models = excelContext.getModels();
            Map<Integer, String> fieldMap = excelContext.getFieldMap();
            for (int index = 0, rowNum = 1; index < models.size(); ++index) {
                Object model = models.get(index);
                Row row = sheet.createRow(rowNum);
                for (int i = 0; i < 10; ++i) {
                    Cell cell = row.createCell(i);
                    String value = BeanUtils.getProperty(model, fieldMap.get(i));
                    cell.setCellValue(value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        } catch (InvocationTargetException e) {
            throw new RuntimeException();
        }

        return true;
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
        }catch (IOException e) {
            throw new RuntimeException();
        }
        return true;
    }
}