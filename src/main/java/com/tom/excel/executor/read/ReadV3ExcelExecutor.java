package com.tom.excel.executor.read;

import com.tom.excel.context.ReadExcelContext;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.InputStream;

/**
 * 读取03版Excel文件内容
 *
 * @author tomxin
 * @date 2019-01-15
 * @since v1.0.0
 */
public class ReadV3ExcelExecutor implements ReadExcelBaseExecutor {

    /**
     * 解析Excel内容
     *
     * @param readExcelContext
     */
    @Override
    public void parse(ReadExcelContext readExcelContext) {
        try {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(readExcelContext.getInputStream());
            InputStream workBookInputStream = poifsFileSystem.createDocumentInputStream("Workbook");
            // 注册监听器
            MissingRecordAwareHSSFListener missingRecordAwareHSSFListener = new MissingRecordAwareHSSFListener();
            HSSFRequest hssfRequest = new HSSFRequest();
            hssfRequest.addListenerForAllRecords(missingRecordAwareHSSFListener);
            HSSFEventFactory eventFactory = new HSSFEventFactory();
            eventFactory.processEvents(hssfRequest, workBookInputStream);
        } catch (Exception e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }
}
