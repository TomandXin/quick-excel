package com.tom.excel.model;

import com.tom.excel.annotations.ExcelWriteProperty;
import com.tom.excel.domain.BaseModel;

public class StudentDO extends BaseModel {

    @ExcelWriteProperty(columnIndex = 0, columnName = "年龄")
    private Integer age;

    @ExcelWriteProperty(columnIndex = 1, columnName = "姓名")
    private String name;

    @ExcelWriteProperty(columnIndex = 2, columnName = "年级")
    private String grade;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
