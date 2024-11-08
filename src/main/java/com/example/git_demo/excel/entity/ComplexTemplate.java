package com.example.git_demo.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//复杂模板头生成 -- 简单的生成
@Getter
@Setter
@ColumnWidth(value = 20)
@HeadStyle(fillForegroundColor = 13)
@HeadFontStyle(fontName="宋体")
public class ComplexTemplate {


    @ExcelProperty({"统计","","姓名"})
    private String name;
    @ExcelProperty({"统计","","年龄"})
    private int age;
    @ExcelProperty({"统计","","地址"})
    private String address;
    @ExcelProperty({"统计","","手机号"})
    private String phone;
    @ExcelProperty({"统计","时间：{date}","状态"})
    private String status;
    @ExcelProperty({"统计","时间：{date}","生成时间"})
    private LocalDateTime localDateTime;

}
