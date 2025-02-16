package com.example.git_demo.poiexcel.listeners;


import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ReadConverterContext;
import cn.idev.excel.enums.CellDataTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class PicConverter implements Converter<String> {


    public Class<?> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadConverterContext<?> context) {
        String stringValue = context.getReadCellData().getStringValue();
        if (stringValue.startsWith("=DISPIMG")){
            return stringValue.split("\"")[1];
        }
       return stringValue;
    }

}
