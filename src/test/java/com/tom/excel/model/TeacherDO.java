package com.tom.excel.model;

import com.tom.excel.annotations.ExcelWriteProperty;
import com.tom.excel.domain.BaseModel;

/**
 * 自定义实体类
 *
 * @author tomxin
 * @date 2019-01-29
 * @since v1.0.0
 */
public class TeacherDO extends BaseModel {

    /**
     * 姓名
     */
    @ExcelWriteProperty(columnName = "姓名", columnIndex = 0)
    private String name;

    /**
     * 课程
     */
    @ExcelWriteProperty(columnName = "课程", columnIndex = 1)
    private String course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
