package com.tom.excel.model;

import com.tom.excel.annotations.ExcelReadProperty;
import com.tom.excel.domain.BaseModel;

public class TeacherReadDO extends BaseModel {

    /**
     * 姓名
     */
    @ExcelReadProperty(columnName = "姓名", columnIndex = 0)
    private String name;

    /**
     * 课程
     */
    @ExcelReadProperty(columnName = "课程", columnIndex = 1)
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

    @Override
    public String toString() {
        return "TeacherReadDO{" +
                "name='" + name + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
