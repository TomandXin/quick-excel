package com.tom.excel.task;

import com.tom.excel.builder.ExcelBuilder;
import com.tom.excel.builder.ReadExcelBuilder;
import com.tom.excel.enums.ExcelTypeEnum;
import com.tom.excel.executor.read.ExcelEventListener;
import com.tom.excel.model.StudentDO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 并发读取文件测试
 *
 * @author tomxin
 * @date 2019-01-24
 * @since v1.0.0
 */
public class ReadExcelTask implements Runnable {

    private int number;

    public ReadExcelTask(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        // 读取Excel文件的InputStream
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/Users/sandyli/sanjin/file/test.xlsx");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        ReadExcelBuilder readExcelBuilder = ExcelBuilder.of(ReadExcelBuilder::new)
                .with(ReadExcelBuilder::setExcelTypeEnum, ExcelTypeEnum.XLSX)
                .with(ReadExcelBuilder::setModelClazz, StudentDO.class)
                .with(ReadExcelBuilder::setInputStream, inputStream)
                .build()
                .init();
        // 读取文件内容
        readExcelBuilder.read(new ExcelEventListener() {
            @Override
            public void postProcess(Object result) {
                System.out.println("postProcess " + number + " result " + result);
            }
        });
    }
}
