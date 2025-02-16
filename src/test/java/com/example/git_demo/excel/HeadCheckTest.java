package com.example.git_demo.excel;

import cn.idev.excel.FastExcel;
import com.example.git_demo.excel.entity.HeadCheck;
import com.example.git_demo.excel.listeners.HeadCheckListener;
import org.junit.jupiter.api.Test;

/**
 * excel 表头验证问题
 */
public class HeadCheckTest {

    @Test
    public void testHeadCheck() {
        HeadCheckListener<HeadCheck> headCheckListener = new HeadCheckListener<>(HeadCheck.class);
        FastExcel.read("C:\\Users\\Administrator\\Desktop\\1.xlsx", HeadCheck.class,headCheckListener).sheet(1).doRead();

    }
}
