package com.example.git_demo.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DynamicUser {


    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private int age;
    @ExcelProperty("地址")
    private String address;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("状态")
    private String status;
    @ExcelProperty("时间")
    private LocalDateTime localDateTime;

}
