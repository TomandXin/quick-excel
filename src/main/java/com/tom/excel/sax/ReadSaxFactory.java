package com.tom.excel.sax;

import com.tom.excel.context.ReadExcelContext;
import com.tom.excel.exceptions.ExcelExceptionFactory;
import com.tom.excel.executor.read.InstanceSubject;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;

/**
 * read sax factory
 *
 * @author tomxin
 * @date 2018-10-28
 * @since v1.0.0
 */
public class ReadSaxFactory {


    /**
     * get sheet parser
     *
     * @param sharedStringsTable
     * @return
     */
    public static XMLReader fetchSheetParser(SharedStringsTable sharedStringsTable, ReadExcelContext readExcelContext) {
        try {
            InstanceSubject instanceSubject = new InstanceSubject();
            ReadExcelSaxHandler handler = new ReadExcelSaxHandler(sharedStringsTable, instanceSubject);
            XMLReader xmlReader = SAXHelper.newXMLReader();
            xmlReader.setContentHandler(handler);
            return xmlReader;
        } catch (SAXException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            throw ExcelExceptionFactory.wrapException(e.getMessage(), e);
        }
    }
}
