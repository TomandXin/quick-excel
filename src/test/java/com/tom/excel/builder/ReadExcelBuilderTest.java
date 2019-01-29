package com.tom.excel.builder;

import com.tom.excel.common.ThreadPoolFactory;
import com.tom.excel.task.ReadExcelTask;

public class ReadExcelBuilderTest {

    public void testBuilder() {
        // 多个任务同时读取文件内容测试
        ReadExcelTask readExcelTask = new ReadExcelTask(1);
        // ReadExcelTask readExcelTask1 = new ReadExcelTask(2);

        ThreadPoolFactory.execute(readExcelTask);
        // ThreadPoolFactory.execute(readExcelTask1);

        ThreadPoolFactory.shutdown();
    }

    public static void main(String[] args) {
        ReadExcelBuilderTest readExcelBuilderTest = new ReadExcelBuilderTest();
        readExcelBuilderTest.testBuilder();
    }
}
