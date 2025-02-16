package com.example.git_demo.excel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeadCheck {

    //账号
    @ExcelProperty("账号")
    private String account;
    //姓名
    @ExcelProperty("姓名")
    private String name;
    //角色
    @ExcelProperty("角色")
    private String role;
    //邮箱
    @ExcelProperty("邮箱")
    private String email;

}
