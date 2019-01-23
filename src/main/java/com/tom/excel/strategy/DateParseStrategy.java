package com.tom.excel.strategy;

import com.tom.excel.common.DateUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式单元格 解析策略
 *
 * @author tomxin
 * @date 2019-01-24
 * @since v1.0.0
 */
public class DateParseStrategy implements Strategy {

    /**
     * 事件格式解析策略
     *
     * @param content
     * @return
     */
    @Override
    public Object parse(Object content) {
        // 将double类型转换为Date类型
        Date date = DateUtil.getJavaDate((Double) ConvertUtils.convert(content, Double.class));
        // 转换为字符串
        return DateUtils.format(date);
    }
}
