package com.tom.excel.executor.write;

import com.tom.excel.context.WriteExcelContext;

import java.io.OutputStream;

/**
 * write excel content
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public interface WriteExcelBaseExecutor {

    /**
     * write content to excel
     *
     * @return
     */
    boolean write(WriteExcelContext excelContext);
}
