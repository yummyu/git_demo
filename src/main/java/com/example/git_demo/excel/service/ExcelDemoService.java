package com.example.git_demo.excel.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.git_demo.excel.ExcelUtil;
import com.example.git_demo.excel.entity.DynamicUser;
import com.example.git_demo.stream.UserInfo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
public class ExcelDemoService {


    /**
     * 无实体动态表头
     *
     * @param heads    前端传参的表头
     * @param response
     * @throws IOException
     */
    public void downloadDynamicNoEntity(String heads, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileName = URLEncoder.encode("测试批量导出", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        //测试表头
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("phone", "电话");
        linkedHashMap.put("address", "地址");
//        linkedHashMap.put("name","姓名");
        linkedHashMap.put("age", "年龄");
        linkedHashMap.put("localDateTime", "时间");

        List<List<String>> excelHead = ExcelUtil.getExcelHead(linkedHashMap);

        List<String> sortHead = ExcelUtil.getSortHead(linkedHashMap);

        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream).head(excelHead).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();

        //分页查询--
        List<UserInfo> userInfos = UserInfo.getData("MaxUserInfo.txt");
        List<List<UserInfo>> partition = ListUtils.partition(userInfos, 1000);

        for (List<UserInfo> list : partition) {
            List<List<Object>> excelRows = ExcelUtil.getDataList(list, sortHead);
            excelWriter.write(excelRows, writeSheet);
        }
        //finish的时候会自动关闭OutputStream
        excelWriter.finish();

    }

    /**
     * 有实体的动态表头
     *
     * @param heads
     * @param response
     * @throws IOException
     */
    public void downloadDynamicWithEntity(String heads, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileName = URLEncoder.encode("测试批量导出", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<UserInfo> userInfos = UserInfo.getData("MaxUserInfo.txt");
        List<List<UserInfo>> partition = ListUtils.partition(userInfos, 1000);
        //导出的列
        Set<String> includeColumnFieldNames = new HashSet<>();
        includeColumnFieldNames.add("address");
        includeColumnFieldNames.add("name");
        includeColumnFieldNames.add("age");

        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), DynamicUser.class).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板")
                    .includeColumnFiledNames(includeColumnFieldNames)//导出的列
                    .orderByIncludeColumn(true)//按顺序导出
                    .build();
            partition.forEach(p -> {
                excelWriter.write(p, writeSheet);
            });

        }


    }


}
