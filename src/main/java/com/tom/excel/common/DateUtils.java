package com.tom.excel.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义时间格式工具类
 *
 * @author tomxin
 * @date 2019-01-24
 * @since v1.0.0
 */
public class DateUtils {

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd hh:mm:ss";

    /**
     * 时间解析格式
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT);

    /**
     * 将时间类型转换为字符串类型
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return dateFormat.format(date);
    }
}
