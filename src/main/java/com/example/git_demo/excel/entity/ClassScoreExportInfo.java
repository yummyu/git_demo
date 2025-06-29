package com.example.git_demo.excel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ClassScoreExportInfo {

    @ExcelProperty("班级")
    private String className;

}
