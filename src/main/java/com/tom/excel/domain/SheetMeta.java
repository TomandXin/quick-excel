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

    /**
     * 是否需要标题行
     */
    private boolean needHeader;

    /**
     * 自定义构造函数
     *
     * @param sheet
     * @param currentRow
     * @param sheetName
     * @param needHeader
     */
    public SheetMeta(Sheet sheet, Integer currentRow, String sheetName, boolean needHeader) {
        this.sheet = sheet;
        this.currentRow = currentRow;
        this.sheetName = sheetName;
        this.needHeader = needHeader;
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

    public boolean isNeedHeader() {
        return needHeader;
    }

    public void setNeedHeader(boolean needHeader) {
        this.needHeader = needHeader;
    }
}
