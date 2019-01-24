package com.tom.excel.builder;

import com.tom.excel.common.ThreadPoolFactory;
import com.tom.excel.task.ReadExcelTask;
import org.junit.Test;

public class ReadExcelBuilderTest {

    public void testBuilder() {
        ReadExcelTask readExcelTask = new ReadExcelTask(1);
        ReadExcelTask readExcelTask1 = new ReadExcelTask(2);

        ThreadPoolFactory.execute(readExcelTask);
        ThreadPoolFactory.execute(readExcelTask1);
    }

    public static void main(String[] args) {
        ReadExcelBuilderTest readExcelBuilderTest = new ReadExcelBuilderTest();
        readExcelBuilderTest.testBuilder();
    }
}
