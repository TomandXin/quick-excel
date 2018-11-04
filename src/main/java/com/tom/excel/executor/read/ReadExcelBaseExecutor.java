package com.tom.excel.executor.read;

import java.io.InputStream;

/**
 * Read Excel Executor
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public interface ReadExcelBaseExecutor {

    /**
     * parse excel content
     *
     * @param inputStream
     * @return
     */
    public void parse(InputStream inputStream);
}
