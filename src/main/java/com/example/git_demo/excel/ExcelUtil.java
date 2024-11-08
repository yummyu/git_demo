package com.example.git_demo.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.example.git_demo.excel.entity.ComplexTemplate;
import com.example.git_demo.excel.entity.FixedHead;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class ExcelUtil {


    /**
     * 获取表头-动态表头 -- 不需要实体对象
     * 参数为有序 LinkedHashMap
     * 前端参数结构 {'name':'姓名','age': '年龄'}
     */
    public static List<List<String>> getExcelHead(LinkedHashMap<String, String> headMap){
        List<List<String>> excelHead = new ArrayList<>();
        //value.split(",") 拆分多级表头,相同名称的表头会被合并
        headMap.forEach((key, value) -> excelHead.add(Lists.newArrayList(value.split(","))));
        return excelHead;
    }


    /**
     * 排序的表头
     */
    public static List<String> getSortHead(LinkedHashMap<String, String> headMap){
        List<String> sortHead = new ArrayList<>();
        headMap.forEach((key, value) -> sortHead.add(key));
        return sortHead;
    }


    /**
     * 将原始数据转成Excel表格数据
     */
    public static <T> List<List<Object>> getDataList(List<T> list,List<String> sortHead){
        List<Map<String, Object>> dataList = list.stream().map(ExcelUtil::convertToMap).toList();
        List<List<Object>> excelRows = new ArrayList<>();
        for (Map<String, Object> dataMap : dataList) {
            List<Object> rows = new ArrayList<>();
            sortHead.forEach(v -> {
                if (dataMap.containsKey(v)) {
                    Object data = dataMap.get(v);
                    rows.add(data);
                }
            });
            excelRows.add(rows);
        }
        return excelRows;

    }


    /**
     * 将对象转换为Map结构
     */
    private static <T> Map<String, Object> convertToMap(T t) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 允许访问私有字段
            try {
                map.put(field.getName(), field.get(t));
            } catch (IllegalAccessException e) {
                log.error("convertToMap\n", e);
            }
        }
        return map;
    }

    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileNameEncode = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ".xlsx");
    }


    //生成一个excel模板
    public static File generateExcelTemplate()  {
        String filePath = "D:\\tmp\\excelTemplate.xlsx";
        EasyExcel.write(filePath,FixedHead.class).sheet("demo").doWrite(new ArrayList<>());
        return new File(filePath);
    }

    public static File generateComplexTemplate()  {
        String filePath = "D:\\tmp\\ComplexTemplate.xlsx";
        List<List<Object>> list = ListUtils.newArrayList();
        
        List<Object> data = ListUtils.newArrayList();
        data.add("{.name}");
        data.add("{.age}");
        data.add("{.address}");
        data.add("{.phone}");
        data.add("{.status}");
        data.add("{.localDateTime}");
        list.add(data);
        
        EasyExcel.write(filePath, ComplexTemplate.class).sheet("demo").doWrite(list);
        return new File(filePath);
    }

}
