package com.tom.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * write excel annotation, use on model/DO
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelWriteProperty {

    int columnIndex();

    String columnName();
}
