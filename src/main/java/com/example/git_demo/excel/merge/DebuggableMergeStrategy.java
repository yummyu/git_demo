package com.example.git_demo.excel.merge;


import cn.idev.excel.write.handler.RowWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteTableHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

@Slf4j
public class DebuggableMergeStrategy implements RowWriteHandler {
    private final int[] mergeColumns;
    private final int startRow;
    private final Map<Integer, Integer> mergeStartRowMap = new HashMap<>();
    private final Map<Integer, Object> previousValueMap = new HashMap<>();
    
    public DebuggableMergeStrategy(int[] mergeColumns, int startRow) {
        this.mergeColumns = mergeColumns;
        this.startRow = startRow;
        log.info("初始化合并策略: 列={}, 起始行={}", Arrays.toString(mergeColumns), startRow);
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder,
                                WriteTableHolder writeTableHolder,
                                Row row,
                                Integer relativeRowIndex,
                                Boolean isHead) {
        if (isHead) {
            log.info("跳过标题行: {}", row.getRowNum());
            return;
        }
        
        Sheet sheet = writeSheetHolder.getSheet();
        int currentRowIndex = row.getRowNum();
        
        log.info("\n处理行: " + currentRowIndex);
        
        // 初始化第一行数据
        if (currentRowIndex == startRow) {
            for (int colIndex : mergeColumns) {
                Cell cell = row.getCell(colIndex);
                Object value = (cell != null) ? getCellValue(cell) : null;
                previousValueMap.put(colIndex, value);
                mergeStartRowMap.put(colIndex, startRow);
                log.info("初始化列[{}]: 值={}", colIndex, value);
            }
            return;
        }
        
        for (int colIndex : mergeColumns) {
            Cell cell = row.getCell(colIndex);
            Object currentValue = (cell != null) ? getCellValue(cell) : null;
            Object prevValue = previousValueMap.get(colIndex);
            
            System.out.printf("列[%d]: 当前值='%s', 前值='%s'%n", 
                             colIndex, currentValue, prevValue);
            
            if (!valuesEqual(currentValue, prevValue)) {
                int startRowIndex = mergeStartRowMap.get(colIndex);
                
                // 需要合并的行数大于1
                if (currentRowIndex - startRowIndex > 1) {
                    CellRangeAddress region = new CellRangeAddress(
                        startRowIndex, 
                        currentRowIndex - 1, 
                        colIndex, 
                        colIndex
                    );

                    log.info("创建合并区域: {}", region.formatAsString());
                    
                    if (!isRegionMerged(sheet, region)) {
                        sheet.addMergedRegion(region);
                        log.info("✅ 添加合并区域: {}", region.formatAsString());
                    } else {
                        log.info("⛔ 区域已存在: {}", region.formatAsString());
                    }
                }
                
                // 更新合并起始行
                mergeStartRowMap.put(colIndex, currentRowIndex);
                log.info("更新列[{}]起始行: {}", colIndex, currentRowIndex);
            }
            
            // 更新上一个值
            previousValueMap.put(colIndex, currentValue);
        }
        
        // 处理最后一行
        if (currentRowIndex == sheet.getLastRowNum()) {
            log.info("处理最后一行...");
            for (int colIndex : mergeColumns) {
                int startRowIndex = mergeStartRowMap.get(colIndex);
                if (currentRowIndex - startRowIndex >= 1) {
                    CellRangeAddress region = new CellRangeAddress(
                        startRowIndex, 
                        currentRowIndex, 
                        colIndex, 
                        colIndex
                    );

                    log.info("创建最后一行合并区域: {}", region.formatAsString());
                    
                    if (!isRegionMerged(sheet, region)) {
                        sheet.addMergedRegion(region);
                        log.info("✅ 添加最后合并区域: {}", region.formatAsString());
                    }
                }
            }
        }
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: 
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BOOLEAN: return cell.getBooleanCellValue();
            case FORMULA: return cell.getCellFormula();
            default: return null;
        }
    }
    
    private boolean valuesEqual(Object val1, Object val2) {
        if (val1 == null && val2 == null) return true;
        if (val1 == null || val2 == null) return false;
        
        // 处理数值类型
        if (val1 instanceof Number && val2 instanceof Number) {
            return ((Number) val1).doubleValue() == ((Number) val2).doubleValue();
        }
        
        // 处理字符串
        if (val1 instanceof String && val2 instanceof String) {
            return ((String) val1).trim().equalsIgnoreCase(((String) val2).trim());
        }
        
        return val1.equals(val2);
    }
    
    private boolean isRegionMerged(Sheet sheet, CellRangeAddress newRegion) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress existing = sheet.getMergedRegion(i);
            if (existing.getFirstRow() == newRegion.getFirstRow() &&
                existing.getLastRow() == newRegion.getLastRow() &&
                existing.getFirstColumn() == newRegion.getFirstColumn() &&
                existing.getLastColumn() == newRegion.getLastColumn()) {
                return true;
            }
        }
        return false;
    }
}