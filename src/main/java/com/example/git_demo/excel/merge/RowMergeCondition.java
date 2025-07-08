package com.example.git_demo.excel.merge;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class RowMergeCondition implements MergeCondition {

    private Integer[] mergeColumn;

    public RowMergeCondition() {

    }

    public RowMergeCondition(Integer[] mergeColumn) {
        this.mergeColumn = mergeColumn;
    }

    @Override
    public boolean isMerge(Row preRow, Row curRow) {

        if (preRow == null || curRow == null) {
            return false;
        }

        if (mergeColumn == null) {
            return isSame(preRow, curRow);
        }

        return isSameCustomer(preRow, curRow, mergeColumn);
    }


    private boolean isSame(Row preRow, Row curRow) {
        Cell preCell = preRow.getCell(0);
        Cell curCell = curRow.getCell(0);

        return preCell.getStringCellValue().equals(curCell.getStringCellValue());
    }

    private boolean isSameCustomer(Row preRow, Row curRow, Integer[] mergeColumn) {

        boolean merge = true;
        for (Integer s : mergeColumn) {
            Cell preCell = preRow.getCell(s);
            Cell curCell = curRow.getCell(s);

            if (!preCell.getStringCellValue().equals(curCell.getStringCellValue())) {
                merge = false;
                break;
            }

        }


        return merge;
    }


    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


}
