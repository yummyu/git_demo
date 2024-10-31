package com.example.git_demo.excel;

import com.alibaba.excel.util.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 动态表头
 */
public class DynamicHead {

//    static String head = "姓名,年龄,地址,手机号,状态";
    static String head = "姓名,年龄,地址";

    public static List<List<String>> getHead() {
        String[] split = head.split(",");
        return Arrays.stream(split).map(Arrays::asList).toList();
    }

    public List<List<Object>> getDataList(List<Object> dataVoList) {
        List<List<Object>> dataLists = new ArrayList<>();

        //建立行列表
        for (int i = 1; i <= dataVoList.size(); i++) {
            if (dataLists.size() < i) {
                List<Object> objectList = ListUtils.newArrayList();
                dataLists.add(objectList);
            }
        }
        return dataLists;
    }


}
