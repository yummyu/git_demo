package com.example.git_demo.excel.merge;

import org.apache.poi.ss.usermodel.Row;

public interface MergeCondition {

    boolean isMerge(Row preRow, Row curRow);
}
