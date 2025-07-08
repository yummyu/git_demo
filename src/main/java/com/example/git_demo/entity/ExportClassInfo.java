package com.example.git_demo.entity;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportClassInfo {

    @ExcelProperty("班级ID")
    private int id;

    @ExcelProperty("班级")
    private String className;

}
