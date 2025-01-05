package com.example.git_demo.excel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.HeadFontStyle;
import cn.idev.excel.annotation.write.style.HeadStyle;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Font;

import java.time.LocalDateTime;

import static org.apache.poi.sl.usermodel.PresetColor.Yellow;

@Getter
@Setter
@ColumnWidth(value = 20)
@HeadStyle(fillForegroundColor = 13)
@HeadFontStyle(fontName="宋体")
public class FixedHead {

    /*@ColumnWith(列宽)
    @ColumnWidth(全局列宽)
    @ExcelProperty(字段配置)
    @HeadFontStyle(头样式)
    @HeadRowHeight(标题高度)
    @ContentFontStyle(内容字体样式)
    @ContentRowHeight(内容高度)*/

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
