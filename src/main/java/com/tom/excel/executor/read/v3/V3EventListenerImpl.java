package com.tom.excel.executor.read.v3;

import com.tom.excel.domain.ContentMeta;
import com.tom.excel.domain.EventMessage;
import com.tom.excel.enums.XSSFDataTypeEnum;
import com.tom.excel.executor.event.EventFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.record.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建Record的监听器
 *
 * @author tomxin
 * @date 2019-01-20
 * @since v1.0.0
 */
public class V3EventListenerImpl implements V3EventListener {

    private SSTRecord sstRecord;

    /**
     * 存储每行的解析内容
     */
    private Map<Integer, ContentMeta> contentMap = new HashMap<>(36);

    /**
     * 每行的内容
     */
    private String content;

    /**
     * 列号
     */
    private int column;

    private int sheetNumber;

    private Integer rowNumber;

    private EventFactory eventFactory;

    /**
     * 构造函数
     *
     * @param eventFactory
     */
    public V3EventListenerImpl(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    /**
     * 处理Record内容
     *
     * @param record
     */
    @Override
    public void processRecord(Record record) {
        XSSFDataTypeEnum xssfDataTypeEnum = XSSFDataTypeEnum.SST_STRING;
        // 判断Record的类型
        switch (record.getSid()) {
            case BOFRecord.sid:
                BOFRecord bofRecord = (BOFRecord) record;
                // 判断是否是worksheet
                if (bofRecord.getType() == BOFRecord.TYPE_WORKSHEET) {
                }
                break;
            case RowRecord.sid:
                RowRecord rowRecord = (RowRecord) record;
                // 当前行信息
                rowNumber = rowRecord.getRowNumber();
                break;
            case NumberRecord.sid:
                NumberRecord numberRecord = (NumberRecord) record;
                // 判断是否是时间类型
                content = String.valueOf(numberRecord.getValue());
                column = numberRecord.getColumn();
                xssfDataTypeEnum = XSSFDataTypeEnum.NUMBER;
                break;
            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;
            case LabelSSTRecord.sid:
                LabelSSTRecord labelSSTRecord = (LabelSSTRecord) record;
                // 获取当前单元格的值
                content = sstRecord.getString(labelSSTRecord.getSSTIndex()).getString();
                // 列号
                column = labelSSTRecord.getColumn();
            default:
                break;
        }
        // 判断该行是否内容为空
        if (record instanceof LastCellOfRowDummyRecord) {
            messageNotify();
            reloadProperty();
            return;
        }
        // 单元格值判空
        if (StringUtils.isEmpty(content)) {
            return;
        }
        ContentMeta contentMeta = new ContentMeta();
        contentMeta.setContent(content);
        contentMeta.setXssfDataTypeEnum(xssfDataTypeEnum);
        contentMap.put(column, contentMeta);
    }

    /**
     * 发送消息
     */
    private void messageNotify() {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setRowContentMap(contentMap);
        eventMessage.setRowNumber(rowNumber);
        eventFactory.notify(eventMessage);
    }

    /**
     * 清空属性
     */
    private void reloadProperty() {
        // 列值清空
        column = 0;
        // 行值清空
        rowNumber = 0;
        // 单元格内容清空
        content = StringUtils.EMPTY;
        // 行内容清空
        contentMap.clear();
    }
}
