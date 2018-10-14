package com.tom.excel.strategy;

/**
 * Base Parse Strategy Class
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public class BaseParseStrategy implements Strategy {

    /**
     * base parse method,do nothing parse
     *
     * @param content
     * @return
     */
    @Override
    public Object parse(Object content) {
        return content;
    }
}
