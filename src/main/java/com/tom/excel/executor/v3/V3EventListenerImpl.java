package com.tom.excel.executor.v3;

import com.tom.excel.domain.EventMessage;
import com.tom.excel.executor.observer.EventFactory;
import com.tom.excel.executor.v3.V3EventListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

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

    /**
     *
     */
    private SSTRecord sstRecord;

    /**
     * 存储每行的解析内容
     */
    private Map<Integer, String> contentMap = new HashMap<>(36);

    /**
     * 每行的内容
     */
    private String content;

    /**
     * 列号
     */
    private int column;

    private int sheetNumber;

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
        // 判断Record的类型
        switch (record.getSid()) {
            case BOFRecord.sid:
                BOFRecord bofRecord = (BOFRecord) record;
                // 判断是否是worksheet
                if (bofRecord.getType() == BOFRecord.TYPE_WORKSHEET) {
                    System.out.println();
                }
                break;
            case RowRecord.sid:
                RowRecord rowRecord = (RowRecord) record;
                // 当前行信息
                rowRecord.getLastCol();
                rowRecord.getFirstCol();
                break;
            case NumberRecord.sid:
                NumberRecord numberRecord = (NumberRecord) record;
                // 判断是否是时间类型
                content = String.valueOf(numberRecord.getValue());
                column = numberRecord.getColumn();
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
            messageNotify(contentMap);
            reloadProperty();
            return;
        }
        // 单元格值判空
        if (StringUtils.isEmpty(content)) {
            return;
        }
        contentMap.put(column, content);
    }

    /**
     * 发送消息
     *
     * @param contentMap
     */
    private void messageNotify(Map<Integer, String> contentMap) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setRowContentMap(contentMap);
        eventFactory.notify(eventMessage);
    }

    /**
     * 清空属性
     */
    private void reloadProperty() {
        // 列值清空
        column = 0;
        // 单元格内容清空
        content = StringUtils.EMPTY;
        // 行内容清空
        contentMap.clear();
    }
}
