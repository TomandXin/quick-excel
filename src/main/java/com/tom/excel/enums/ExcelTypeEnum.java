package com.tom.excel.enums;

/**
 * Excel version Enum
 *
 * @author tomxin
 * @date 2018-10-14
 * @since v1.0.0
 */
public enum ExcelTypeEnum {

    /**
     * 03~07 version Excel
     */
    XLS,

    /**
     * 07~ version Excel
     */
    XLSX;

    ExcelTypeEnum() {

    }

    /**
     * judge 03~07 version of excel
     *
     * @param excelTypeEnum
     * @return
     */
    public static boolean isXls(ExcelTypeEnum excelTypeEnum) {
        return XLS.equals(excelTypeEnum);
    }

    /**
     * judge is 07~ version of excel
     *
     * @param excelTypeEnum
     * @return
     */
    public static boolean isXlsx(ExcelTypeEnum excelTypeEnum) {
        return XLSX.equals(excelTypeEnum);
    }
}
