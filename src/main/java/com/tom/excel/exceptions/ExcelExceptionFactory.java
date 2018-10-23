package com.tom.excel.exceptions;

/**
 * Exception Factory
 *
 * @author tomxin
 * @date 2018-10-23
 * @since v1.0.0
 */
public class ExcelExceptionFactory {

    /**
     * default construct method
     *
     */
    public ExcelExceptionFactory() {

    }

    /**
     * wrap exception
     *
     * @param message
     * @param exception
     * @return
     */
    public static RuntimeException wrapException(String message, Exception exception) {
        return new ExcelException(message, exception);
    }
}
