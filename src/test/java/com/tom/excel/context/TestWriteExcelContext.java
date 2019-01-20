package com.tom.excel.context;

import com.tom.excel.builder.ExcelBuilder;
import com.tom.excel.builder.WriteExcelBuilder;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.model.StudentDO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestWriteExcelContext {

    WriteExcelBuilder writeExcelBuilder;

    public void testWrite() throws Exception {
        File file = new File("/Users/sandyli/sanjin/file/test.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        // excel builder
        WriteExcelBuilder writeExcelBuilder = ExcelBuilder.of(WriteExcelBuilder::new)
                .with(WriteExcelBuilder::setExcelTypeEnum, ExcelTypeEnum.XLSX)
                .with(WriteExcelBuilder::setModelClazz, StudentDO.class)
                .with(WriteExcelBuilder::setOutputStream, fileOutputStream)
                .build();
        writeExcelBuilder.init();
        List<StudentDO> studentDOS = new ArrayList<>();
        StudentDO studentDO = new StudentDO();
        studentDO.setAge(16);
        studentDO.setName("tomxin");
        studentDO.setGrade("一");
        studentDOS.add(studentDO);
        
        writeExcelBuilder.write(studentDOS);

    }

    public static void main(String[] args) throws Exception {
        TestWriteExcelContext context = new TestWriteExcelContext();
        context.testWrite();
    }
}
