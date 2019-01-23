package com.tom.excel.executor.v7;

import com.tom.excel.executor.observer.EventFactory;
import com.tom.excel.domain.EventMessage;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read Excel Handler for SAX TODO 魔法文本更改
 *
 * @author tomxin
 * @date 2018-10-23
 * @since v1.0.0
 */
public class ReadExcelSaxHandler extends DefaultHandler {

    /**
     *
     */
    private String lastContents;

    /**
     * shared string table
     */
    private SharedStringsTable sharedStringsTable;

    /**
     *
     */
    private boolean nextIsString;

    /**
     * row's col content
     */
    private Map<Integer, String> rowContentMap = new HashMap<>(36);

    /**
     * current column number
     */
    private int curCol;

    /**
     * current column row
     */
    private int curRow;

    /**
     * column pattern
     */
    private static Pattern COL_PATTERN = Pattern.compile("[0-9]");

    /**
     * row pattern
     */
    private static Pattern ROW_PATTERN = Pattern.compile("[^0-9]");


    /**
     * destruct method
     *
     * @param sharedStringsTable
     */
    public ReadExcelSaxHandler(SharedStringsTable sharedStringsTable) {
        this.sharedStringsTable = sharedStringsTable;
    }

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
        if (qName.contains("c")) {
            // 获取当前列的索引位置
            String index = attributes.getValue("r");
            if (StringUtils.isNotEmpty(index)) {
                Matcher rowMatcher = ROW_PATTERN.matcher(index);
                // 当前行
                curRow = Integer.parseInt(rowMatcher.replaceAll(StringUtils.EMPTY));
                // 当前列
                Matcher colMatcher = COL_PATTERN.matcher(index);
                char[] cols = colMatcher.replaceAll(StringUtils.EMPTY).toCharArray();
                int col = 0;
                for (int i = 0; i < cols.length; i++) {
                    col += (cols[i] - '@') * Math.pow(26, cols.length - i - 1);
                }
                curCol = col - 1;
            }
            // 类型
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
            lastContents = StringUtils.EMPTY;
        }
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
        if (nextIsString) {
            int idx = (Integer) ConvertUtils.convert(lastContents, Integer.class);
            XSSFRichTextString richText = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
            lastContents = richText.toString();
            nextIsString = false;
        }
        // 存储坐标
        if (qName.equals("v")) {
            rowContentMap.put(curCol, lastContents);
        }
        // 标志一行已经解析结束
        if (qName.equals("row")) {
            if (curRow == 0) {
                // 判断是标题栏

            } else {
                // 发送通知
                publishContent();
                // 重新初始化rowContentMap
                rowContentMap.clear();
            }

        }
    }

    /**
     * 发送内容到注册中心
     */
    private void publishContent() {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setRowContentMap(rowContentMap);
        EventFactory.notify(eventMessage);
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
        lastContents += new String(chars, start, length);
    }
}
