package com.example.git_demo.excel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class StudentExport {

    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("学科")
    private String subject;
    @ExcelProperty("成绩")
    private String score;


}