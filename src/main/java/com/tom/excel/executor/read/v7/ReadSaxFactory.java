package com.tom.excel.executor.read.v7;

import com.tom.excel.context.ReadExcelContext;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * read v7 factory
 *
 * @author tomxin
 * @date 2018-10-28
 * @since v1.0.0
 */
public class ReadSaxFactory {


    /**
     * get sheet parser
     *
     * @param xssfReader
     * @param readExcelContext
     * @return
     */
    public static XMLReader fetchSheetParser(XSSFReader xssfReader, ReadExcelContext readExcelContext) {
        try {
            // ShareStringsTable
            ReadExcelSaxHandler handler = new ReadExcelSaxHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable(), readExcelContext.getEventFactory());
            XMLReader xmlReader = SAXHelper.newXMLReader();
            xmlReader.setContentHandler(handler);
            return xmlReader;
        } catch (SAXException | IOException | InvalidFormatException | ParserConfigurationException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }
}
