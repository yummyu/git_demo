package com.example.git_demo.excel;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.enums.CellExtraTypeEnum;
import com.example.git_demo.excel.entity.MultiHeads;
import com.example.git_demo.excel.listeners.CustomerListener;
import com.example.git_demo.excel.listeners.MergeReadListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelReadTest {

    //多表头测试
    @Test
    public void testReadWithMultiHead() throws FileNotFoundException {
        CustomerListener customerListener = new CustomerListener();
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "multiHeads.xlsx");
        EasyExcel.read(file, MultiHeads.class, customerListener)
                .extraRead(CellExtraTypeEnum.MERGE)
                .sheet().doRead();

    }

    @Test
    public void testReadWithMultiHeadAndCheck() throws FileNotFoundException {
        MergeReadListener mergeReadListener = new MergeReadListener();
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "multiHeads.xlsx");
        EasyExcel.read(file, mergeReadListener).extraRead(CellExtraTypeEnum.MERGE)
                .sheet().doRead();

    }




}
