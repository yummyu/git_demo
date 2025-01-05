package com.example.git_demo.excel.listeners;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.write.handler.CellWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * copy <a href="https://www.cnblogs.com/wj123bk/p/18442280">...</a>
 */
public class MergeCellWriteHandler implements CellWriteHandler {

    // 要合并的列索引数组
    private final int[] mergeColumnIndex;
    // 合并开始的行索引
    private final int mergeRowIndex;

    /**
     * 构造函数
     *
     * @param mergeRowIndex     合并开始的行索引
     * @param mergeColumnIndex  要合并的列索引数组
     */
    public MergeCellWriteHandler(int mergeRowIndex, int[] mergeColumnIndex) {
        this.mergeRowIndex = mergeRowIndex;
        this.mergeColumnIndex = mergeColumnIndex;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        // 单元格创建前的处理（这里不需要处理）
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 单元格创建后的处理（这里不需要处理）
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 当前行索引
        int curRowIndex = cell.getRowIndex();
        // 当前列索引
        int curColIndex = cell.getColumnIndex();

        // 如果当前行大于合并开始行且当前列在需要合并的列中
        if (curRowIndex > mergeRowIndex && isMergeColumn(curColIndex)) {
            // 进行合并操作
            mergeWithPrevRow(writeSheetHolder, cell, curRowIndex, curColIndex);
        }
    }

    /**
     * 检查当前列是否在需要合并的列中
     *
     * @param curColIndex 当前列索引
     * @return 如果是需要合并的列返回true，否则返回false
     */
    private boolean isMergeColumn(int curColIndex) {
        for (int columnIndex : mergeColumnIndex) {
            if (curColIndex == columnIndex) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前单元格向上合并
     *
     * @param writeSheetHolder 当前工作表持有者
     * @param cell             当前单元格
     * @param curRowIndex      当前行索引
     * @param curColIndex      当前列索引
     */
    private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {
        // 获取当前单元格的数据
        Object curData = getCellData(cell);
        // 获取前一个单元格的数据
        Cell preCell = cell.getSheet().getRow(curRowIndex - 1).getCell(curColIndex);
        Object preData = getCellData(preCell);

        // 判断当前单元格和前一个单元格的数据以及主键是否相同
        if (curData.equals(preData) && isSamePrimaryKey(cell, curRowIndex)) {
            // 获取工作表
            Sheet sheet = writeSheetHolder.getSheet();
            // 合并单元格
            mergeCells(sheet, curRowIndex, curColIndex);
        }
    }

    /**
     * 获取单元格的数据
     *
     * @param cell 单元格
     * @return 单元格数据
     */
    private Object getCellData(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();
    }

    /**
     * 判断当前单元格和前一个单元格的主键是否相同
     *
     * @param cell         当前单元格
     * @param curRowIndex  当前行索引
     * @return 如果主键相同返回true，否则返回false
     */
    private boolean isSamePrimaryKey(Cell cell, int curRowIndex) {
        String currentPrimaryKey = cell.getRow().getCell(0).getStringCellValue();
        String previousPrimaryKey = cell.getSheet().getRow(curRowIndex - 1).getCell(0).getStringCellValue();
        return currentPrimaryKey.equals(previousPrimaryKey);
    }

    /**
     * 合并单元格
     *
     * @param sheet        工作表
     * @param curRowIndex  当前行索引
     * @param curColIndex  当前列索引
     */
    private void mergeCells(Sheet sheet, int curRowIndex, int curColIndex) {
        // 获取已合并的区域
        List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
        boolean isMerged = false;

        // 检查前一个单元格是否已经被合并
        for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
            CellRangeAddress cellRangeAddr = mergeRegions.get(i);
            if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
                sheet.removeMergedRegion(i);
                cellRangeAddr.setLastRow(curRowIndex);
                sheet.addMergedRegion(cellRangeAddr);
                isMerged = true;
            }
        }

        // 如果前一个单元格未被合并，则新增合并区域
        if (!isMerged) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }
}