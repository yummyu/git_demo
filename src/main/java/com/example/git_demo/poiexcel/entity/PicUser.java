package com.example.git_demo.poiexcel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import com.example.git_demo.poiexcel.listeners.PicConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PicUser {

    @ExcelProperty("账号")
    private String account;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("角色")
    private String role;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty(value = "附件", converter = PicConverter.class)
    private String file;


}
