package com.example.git_demo.excel;

import cn.idev.excel.EasyExcel;
import com.example.git_demo.excel.entity.MergeEntity;
import com.example.git_demo.excel.listeners.MergeCellWriteHandler;
import com.example.git_demo.stream.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
class ExcelMergeTest {

    static List<MergeEntity> data;
    static String path = "C:\\Users\\Administrator\\Desktop\\";
    @BeforeAll
    public static void init() {
        data = MergeEntity.getData("MergeInfo.txt");
        path = path + "mergeCell.xlsx";
    }

    @Test
    public void testMergeCell() {
        EasyExcel.write(path, MergeEntity.class)
                .registerWriteHandler(new MergeCellWriteHandler(0, new int[]{0, 1}))
                .sheet("sheet1")
                .doWrite(data);

    }




}