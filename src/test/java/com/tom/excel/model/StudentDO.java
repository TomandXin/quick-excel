package com.tom.excel.model;

import com.tom.excel.annotations.ExcelReadProperty;
import com.tom.excel.domain.BaseModel;

/**
 * 自定义测试类
 *
 * @author tomxin
 * @date 2019-01-24
 * @since v1.0.0
 */
public class StudentDO extends BaseModel {

    /**
     * 姓名
     */
    @ExcelReadProperty(columnIndex = 1, columnName = "姓名")
    private String name;

    /**
     * 年龄
     */
    @ExcelReadProperty(columnIndex = 0, columnName = "年龄")
    private String age;

    /**
     * 年级
     */
    @ExcelReadProperty(columnIndex = 2, columnName = "年级")
    private String grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "StudentDO{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
