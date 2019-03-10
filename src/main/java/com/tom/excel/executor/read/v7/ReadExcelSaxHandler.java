package com.tom.excel.executor.read.v7;

import com.tom.excel.common.StringUtil;
import com.tom.excel.domain.ContentMeta;
import com.tom.excel.domain.EventMessage;
import com.tom.excel.enums.XSSFDataTypeEnum;
import com.tom.excel.executor.event.EventFactory;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Read Excel Handler for SAX
 *
 * @author tomxin
 * @date 2018-10-23
 * @since v1.0.0
 */
public class ReadExcelSaxHandler extends DefaultHandler {


    private String lastContents;

    /**
     * shared string table
     */
    private SharedStringsTable sharedStringsTable;

    /**
     * row's col content
     */
    private Map<Integer, ContentMeta> rowContentMap = new HashMap<>(36);

    /**
     * current column number
     */
    private int curCol;

    /**
     * current column row
     */
    private int curRow;

    /**
     * Set when an Inline String "is" is seen
     */
    private boolean isIsOpen;


    private EventFactory eventFactory;

    /**
     * 判断字符类型
     */
    private XSSFDataTypeEnum nextDataType;

    /**
     * 数字格式
     */
    private DataFormatter dataFormatter;

    private StylesTable stylesTable;

    private int formatIndex;

    private String formatString;


    /**
     * destruct method
     *
     * @param sharedStringsTable
     */
    public ReadExcelSaxHandler(SharedStringsTable sharedStringsTable, StylesTable stylesTable, EventFactory eventFactory) {
        this.sharedStringsTable = sharedStringsTable;
        this.eventFactory = eventFactory;
        this.stylesTable = stylesTable;
        dataFormatter = new DataFormatter();
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
        if (StringUtil.LOCAL_NAME_IS.equals(localName)) {
            // Inline string outer tag
            isIsOpen = true;
        }
        // c => cell
        if (qName.contains(StringUtil.QNAME_C)) {
            // 获取当前列的索引位置
            String index = attributes.getValue(StringUtil.QNAME_R);
            if (StringUtils.isNotEmpty(index)) {
                CellAddress cellAddress = new CellAddress(index);
                curRow = cellAddress.getRow();
                curCol = cellAddress.getColumn();
            }
            // 类型
            String cellType = attributes.getValue(StringUtil.QNAME_T);
            String cellStyleStr = attributes.getValue(StringUtil.QNAME_S);

            if ("b".equals(cellType)) {
                nextDataType = XSSFDataTypeEnum.BOOLEAN;
            } else if ("e".equals(cellType)) {
                nextDataType = XSSFDataTypeEnum.ERROR;
            } else if ("inlineStr".equals(cellType)) {
                nextDataType = XSSFDataTypeEnum.INLINE_STRING;
            } else if ("s".equals(cellType)) {
                nextDataType = XSSFDataTypeEnum.SST_STRING;
            } else if ("str".equals(cellType)) {
                nextDataType = XSSFDataTypeEnum.FORMULA;
            } else {
                XSSFCellStyle style = null;
                if (null != stylesTable) {
                    if (null != cellStyleStr) {
                        int styleIndex = Integer.parseInt(cellStyleStr);
                        style = stylesTable.getStyleAt(styleIndex);
                    } else if (stylesTable.getNumCellStyles() > 0) {
                        style = stylesTable.getStyleAt(0);
                    }
                }
                if (null != style) {
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (null != formatString) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                }
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
        String thisStr = null;
        if (isTextTag(localName)) {
            switch (nextDataType) {
                case BOOLEAN:
                    char first = lastContents.charAt(0);
                    thisStr = first == '0' ? "FALSE" : "TRUE";
                    break;
                case ERROR:
                    thisStr = "ERROR:" + lastContents;
                    break;
                case FORMULA:
                    String fv = lastContents;
                    if (null != this.formatString) {
                        try {
                            double d = Double.parseDouble(fv);
                            thisStr = dataFormatter.formatRawCellContents(d, formatIndex, formatString);
                        } catch (NumberFormatException e) {
                            thisStr = fv;
                        }
                    } else {
                        thisStr = fv;
                    }
                    break;
                case INLINE_STRING:
                    XSSFRichTextString rtsi = new XSSFRichTextString(lastContents.toString());
                    thisStr = rtsi.toString();
                    break;
                case SST_STRING:
                    int idx = (Integer) ConvertUtils.convert(lastContents, Integer.class);
                    XSSFRichTextString richText = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                    thisStr = richText.toString();
                    break;
                case NUMBER:
                    if (lastContents.length() > 0 && null != formatString) {
                        thisStr = dataFormatter.formatRawCellContents(Double.parseDouble(lastContents), this.formatIndex, this.formatString);
                    } else {
                        thisStr = lastContents;
                    }
                    break;
                default:
                    break;
            }
            ContentMeta contentMeta = new ContentMeta();
            contentMeta.setContent(thisStr);
            contentMeta.setXssfDataTypeEnum(nextDataType);
            rowContentMap.put(curCol, contentMeta);
        } else if (StringUtil.LOCAL_NAME_IS.equals(localName)) {
            isIsOpen = false;
        }

        if (StringUtil.QNAME_ROW.equals(qName)) {
            // TODO 标志一行已经解析结束 判断是否需要标题行
            if (curRow == 0) {
                // 判断是标题栏

            } else {
                // 发送通知
                messageNotify();
                // 重新初始化rowContentMap
                rowContentMap.clear();
            }

        }
    }

    /**
     * 判断单元格内容是否是文本
     *
     * @param name
     * @return
     */
    private boolean isTextTag(String name) {
        if ("v".equals(name)) {
            return true;
        }
        if ("inlineStr".equals(name)) {
            return true;
        }
        if ("t".equals(name) && isIsOpen) {
            // Inline string <is><t>...</t></is>
            return true;
        }
        return false;
    }

    /**
     * 发送内容到注册中心
     */
    private void messageNotify() {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setRowContentMap(rowContentMap);
        eventMessage.setRowNumber(curRow + 1);
        eventFactory.notify(eventMessage);
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
