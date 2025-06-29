package com.example.git_demo.excel.merge;

import cn.idev.excel.write.handler.RowWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class MergeRowWriteHandler implements RowWriteHandler {

    //合并条件
    private RowMergeCondition mergeCondition;
    //数据起始行
    private int headRowIndex;

    public MergeRowWriteHandler(int headRowIndex,RowMergeCondition mergeCondition) {
        this.headRowIndex = headRowIndex;
        this.mergeCondition = mergeCondition;
    }


    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        Sheet sheet = writeSheetHolder.getSheet();

        // 当前行索引
        int curRowIndex = row.getRowNum();

        if (curRowIndex <= headRowIndex) {
            return;
        }

        // 获取上一行
        Row preRow = sheet.getRow(curRowIndex - 1);

        boolean needMerge = mergeCondition.isMerge(preRow, row);

        if (needMerge) {
            // 获取已合并的区域
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            boolean isMerged = false;

            // 检查前一个单元格是否已经被合并
            for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                if (cellRangeAddr.isInRange(curRowIndex - 1, 0)) {
                    sheet.removeMergedRegion(i);
                    cellRangeAddr.setLastRow(curRowIndex);
                    sheet.addMergedRegion(cellRangeAddr);
                    isMerged = true;
                }
            }

            // 如果前一个单元格未被合并，则新增合并区域
            if (!isMerged) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex,0,0);
                sheet.addMergedRegion(cellRangeAddress);
            }
        }



    }
}
