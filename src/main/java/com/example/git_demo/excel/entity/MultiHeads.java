package com.example.git_demo.excel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MultiHeads {

    @ExcelProperty("班级")
    @NotBlank(message = "班级不能为空")
    private String className;
    @ExcelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @ExcelProperty("学科")
    @NotBlank(message = "学科不能为空")
    private String subject;
    @ExcelProperty("成绩")
    @NotBlank(message = "成绩不能为空")
    private String score;

}
