package com.tom.excel.exceptions;

/**
 * Define Excel Exception
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public class ExcelException extends RuntimeException {

    /**
     * 默认无参数的构造函数
     */
    public ExcelException() {
        super();
    }

    /**
     * 带参数的构造函数
     *
     * @param message
     */
    public ExcelException(String message) {
        super(message);
    }

    /**
     * 带参数的构造函数
     *
     * @param message
     * @param e
     */
    public ExcelException(String message,Throwable e) {
        super(message,e);
    }

    /**
     * 带参数的构造函数
     *
     * @param e
     */
    public ExcelException(Throwable e) {
        super(e);
    }

}
