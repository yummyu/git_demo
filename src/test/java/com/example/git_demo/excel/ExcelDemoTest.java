package com.example.git_demo.excel;

import com.alibaba.excel.EasyExcel;
import com.example.git_demo.stream.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
class ExcelDemoTest {

    static List<UserInfo> data;

    @BeforeAll
    public static void init() {
//        data = UserInfo.getData("UserInfo.txt");
        data = UserInfo.getData("MaxUserInfo.txt");
    }

    @Test
    public void testDynamicHead() {
        List<List<String>> head = DynamicHead.getHead();
        log.info("head:{}", head);

        String fileName = "D:\\tmp\\" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName)
                // 这里放入动态头
                .head(head).sheet("模板")
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(data);
    }

    @Test
    public void testDynamicHead2() throws IOException {

    }



}