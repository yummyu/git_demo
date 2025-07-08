package com.example.git_demo.excel.merge;

import cn.idev.excel.write.handler.RowWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * 处理单元格导出合并，数据已经排过序
 */
public class MergeRowWriteHandler implements RowWriteHandler {

    //合并条件
    private RowMergeCondition mergeCondition;
    //数据起始行
    private int headRowIndex;
    //需要合并的数据列
    private Integer[] lastMergeColumn;

    private Row firstRowData;//合并行的第一条数据，解决合并单元格所有合并列都有值的问题

    public MergeRowWriteHandler(int headRowIndex, RowMergeCondition mergeCondition) {
        this.headRowIndex = headRowIndex;
        this.mergeCondition = mergeCondition;
    }

    public MergeRowWriteHandler(int headRowIndex, RowMergeCondition mergeCondition, Integer[] lastMergeColumn) {
        this.headRowIndex = headRowIndex;
        this.mergeCondition = mergeCondition;
        this.lastMergeColumn = lastMergeColumn;
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row curRow, Integer relativeRowIndex, Boolean isHead) {
        Sheet sheet = writeSheetHolder.getSheet();

        // 当前行索引
        int curRowIndex = curRow.getRowNum();

        if (curRowIndex <= headRowIndex) {
            if (curRowIndex == headRowIndex) {
                firstRowData = curRow;
            }
            return;
        }

        boolean needMerge = mergeCondition.isMerge(firstRowData, curRow);

        if (needMerge) {
            this.clearCellData(curRow);
            for (Integer columnIndex : lastMergeColumn) {
                // 获取已合并的区域
                List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
                boolean isMerged = false;

                // 检查前一个单元格是否已经被合并
                for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                    CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                    if (cellRangeAddr.isInRange(curRowIndex - 1, columnIndex)) {
                        sheet.removeMergedRegion(i);
                        cellRangeAddr.setLastRow(curRowIndex);
                        sheet.addMergedRegion(cellRangeAddr);
                        isMerged = true;
                    }
                }

                // 如果前一个单元格未被合并，则新增合并区域
                if (!isMerged) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, columnIndex, columnIndex);
                    sheet.addMergedRegion(cellRangeAddress);
                }
            }
        } else {
            firstRowData = curRow; // 重置合并行数据
        }

    }

    private void clearCellData(Row row) {
        for (Integer columnIndex : lastMergeColumn) {
            row.getCell(columnIndex).setBlank();
        }
    }

}
