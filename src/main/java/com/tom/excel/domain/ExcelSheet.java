package com.tom.excel.domain;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * 自定义Sheet
 *
 * @author tomxin
 * @date 2019-01-21
 * @since v1.0.0
 */
public class ExcelSheet {

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * sheet编号
     */
    private Integer index;

    /**
     * 带写入的实体类型
     */
    private Class<? extends BaseModel> modelClass;

    /**
     * 是否需要标题行
     */
    private boolean needHeader;


    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Class<? extends BaseModel> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<? extends BaseModel> modelClass) {
        this.modelClass = modelClass;
    }

    public boolean isNeedHeader() {
        return needHeader;
    }

    public void setNeedHeader(boolean needHeader) {
        this.needHeader = needHeader;
    }
}
