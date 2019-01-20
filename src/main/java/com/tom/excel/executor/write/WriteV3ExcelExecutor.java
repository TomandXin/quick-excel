package com.tom.excel.executor.write;

import com.tom.excel.context.WriteExcelContext;

/**
 * 03版Excel文件写入执行器
 *
 * @author tomxin
 * @date 2019-01-20
 * @since v1.0.0
 */
public class WriteV3ExcelExecutor implements WriteExcelBaseExecutor {

    public WriteV3ExcelExecutor() {

    }

    @Override
    public boolean write(WriteExcelContext excelContext) {
        return false;
    }

    @Override
    public boolean postProcess(WriteExcelContext excelContext) {
        return false;
    }
}
