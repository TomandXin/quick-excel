package com.tom.excel.annotations;

import com.tom.excel.strategy.BaseParseStrategy;
import com.tom.excel.strategy.Strategy;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelReadProperty {

    int columnIndex() default 0;

    String columnName() default StringUtils.EMPTY;

    Class<? extends Strategy> parseStrategy() default BaseParseStrategy.class;
}
