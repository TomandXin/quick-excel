package com.tom.excel.sax;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Read Excel Handler for SAX
 *
 * @author tomxin
 * @date 2018-10-23
 * @since v1.0.0
 */
public class ReadExcelSaxHandler extends DefaultHandler {

    /**
     * start element
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

    }

    /**
     * end element
     *
     * @param uri
     * @param localName
     * @param qName
     */
    @Override
    public void endElement(String uri, String localName, String qName) {

    }

    /**
     * characters
     *
     * @param chars
     * @param start
     * @param length
     */
    @Override
    public void characters(char[] chars, int start, int length) {

    }
}
