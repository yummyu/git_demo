package com.example.git_demo.entity;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportStudentInfo {

    @ExcelIgnore
    private String id;

    @ExcelProperty("班级ID")
    private String classId;

    @ExcelProperty("姓名")
    private String userName;

    @ExcelProperty("学号")
    private String userNo;

    @ExcelProperty("学科")
    private String subjectName;

    @ExcelProperty("成绩")
    private String subjectScore;


}
