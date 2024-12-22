package com.example.git_demo.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiHeads {

    @ExcelProperty("班级")
    private String className;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("学科")
    private String subject;
    @ExcelProperty("成绩")
    private String score;

}
