package com.tom.excel.executor.read;

import com.tom.excel.context.ReadExcelContext;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.sax.ReadSaxFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * read excel executor
 *
 * @author tomxin
 * @date 2018-10-28
 * @since v1.0.0
 */
public class ReadExcelExecutor implements ReadExcelBaseExecutor {

    /**
     * 解析实体类
     *
     * @param readExcelContext
     * @return
     */
    @Override
    public void parse(ReadExcelContext readExcelContext) {
        try {
            OPCPackage opcPackage = OPCPackage.open(readExcelContext.getInputStream());
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            //
            SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();
            XMLReader xmlReader = ReadSaxFactory.fetchSheetParser(sharedStringsTable, readExcelContext);

            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()){
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                xmlReader.parse(sheetSource);
            }
        } catch (InvalidFormatException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (IOException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (OpenXML4JException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (SAXException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }
}
