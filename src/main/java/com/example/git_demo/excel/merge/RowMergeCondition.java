package com.example.git_demo.excel.merge;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class RowMergeCondition implements MergeCondition{
    @Override
    public boolean isMerge(Row preRow, Row curRow) {

        if (preRow == null || curRow == null) {
            return false;
        }

        return isSame(preRow,curRow);
    }


    private boolean isSame(Row preRow, Row curRow) {
        Cell preCell = preRow.getCell(0);
        Cell curCell = curRow.getCell(0);

        return preCell.getStringCellValue().equals(curCell.getStringCellValue());
    }


}
