package com.tom.excel.builder;

import com.tom.excel.domain.ExcelSheet;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.model.TeacherDO;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 写Excel的Builder
 *
 * @author tomxin
 * @date 2019-01-28
 * @since v1.0.0
 */
public class WriteExcelBuilderTest {

    @Test
    public void writeTest() throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/sandyli/sanjin/file/testNew.xlsx");
        WriteExcelBuilder writeExcelBuilder = ExcelBuilder.of(WriteExcelBuilder::new)
                .with(WriteExcelBuilder::setExcelTypeEnum, ExcelTypeEnum.XLSX)
                .with(WriteExcelBuilder::setOutputStream, fileOutputStream)
                .build()
                .init();
        ExcelSheet oneSheet = new ExcelSheet();
        oneSheet.setSheetName("one");
        oneSheet.setNeedHeader(true);
        oneSheet.setModelClass(TeacherDO.class);
        oneSheet.setIndex(0);

        List<TeacherDO> oneTeacherList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setName("one " + i);
            teacherDO.setCourse("course " + i);
            oneTeacherList.add(teacherDO);
        }
        writeExcelBuilder.write(oneTeacherList, oneSheet);

        ExcelSheet twoSheet = new ExcelSheet();
        twoSheet.setSheetName("three");
        twoSheet.setNeedHeader(true);
        twoSheet.setModelClass(TeacherDO.class);
        twoSheet.setIndex(0);

        List<TeacherDO> threeTeacherList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setName("three " + i);
            teacherDO.setCourse("course " + i);
            threeTeacherList.add(teacherDO);
        }

        writeExcelBuilder.write(threeTeacherList, twoSheet);

        ExcelSheet threeSheet = new ExcelSheet();
        threeSheet.setSheetName("two");
        threeSheet.setNeedHeader(true);
        threeSheet.setModelClass(TeacherDO.class);
        threeSheet.setIndex(0);

        List<TeacherDO> twoTeacherList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setName("two " + i);
            teacherDO.setCourse("course " + i);
            twoTeacherList.add(teacherDO);
        }

        writeExcelBuilder.write(twoTeacherList, threeSheet);

        writeExcelBuilder.flush();
    }
}
