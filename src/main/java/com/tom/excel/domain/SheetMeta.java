package com.tom.excel.domain;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * 保存每个Sheet的信息
 *
 * @author tomxin
 * @date 2019-01-21
 * @since v1.0.0
 */
public class SheetMeta {

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * 当前行数
     */
    private Integer currentRow;

    /**
     * sheet名称
     */
    private String sheetName;


    public SheetMeta(Sheet sheet, Integer currentRow, String sheetName) {
        this.sheet = sheet;
        this.currentRow = currentRow;
        this.sheetName = sheetName;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Integer getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(Integer currentRow) {
        this.currentRow = currentRow;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
