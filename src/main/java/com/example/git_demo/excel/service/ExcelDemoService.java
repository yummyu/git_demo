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
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExcelDemoService {


    /**
     * 无实体动态表头
     *
     * @param heads    前端传参的表头
     */
    public void downloadDynamicNoEntity(String heads, HttpServletResponse response) throws IOException {
        ExcelUtil.setResponseHeader(response, "测试批量导出无实体");

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
     * @param heads 前端传参的表头
     */
    public void downloadDynamicWithEntity(String heads, HttpServletResponse response) throws IOException {

        ExcelUtil.setResponseHeader(response, "测试批量导出有实体");

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
                    .includeColumnFieldNames(includeColumnFieldNames)//导出的列
                    .orderByIncludeColumn(true)//按顺序导出
                    .build();
            partition.forEach(p -> {
                excelWriter.write(p, writeSheet);//分页导出
            });
        }
    }


    /**
     * 使用固定模板导出
     */
    public void downloadWithFixedHead(HttpServletResponse response) throws IOException {
        ExcelUtil.setResponseHeader(response, "测试固定表头模板导出");
        File file = ExcelUtil.generateExcelTemplate();//这里用代码生成，实际直接使用excel模板文件

        List<UserInfo> userInfos = UserInfo.getData("MaxUserInfo.txt");
        List<List<UserInfo>> partition = ListUtils.partition(userInfos, 1000);

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                .withTemplate(file)//模板文件
                .needHead(false)//固定模板导出，不需要再次生成表头
                .build();
        // 这里注意 如果同一个sheet只要创建一次，默认sheet名称是"0" 和
        WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
        partition.forEach(p -> {
            excelWriter.write(p, writeSheet);//分页导出
        });
        excelWriter.finish();
    }

    /**
     * 数据量大的复杂填充 -- copy 官网
     * <p>
     * 这里的解决方案是 确保模板list为最后一行，然后再拼接table.还有03版没救，只能刚正面加内存。
     *
     * @since 2.1.1
     */
    public void complexFillWithTable(HttpServletResponse response) throws IOException {
        ExcelUtil.setResponseHeader(response, "测试复杂表头模板导出");
        File tmpFile = ExcelUtil.generateComplexTemplate();
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // {} 代表普通变量 {.} 代表是list的变量
        // 这里模板 删除了list以后的数据，也就是统计的这一行

        // 方案1
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(tmpFile).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();

            // 直接写入数据
            List<UserInfo> userInfos = UserInfo.getData("UserInfo.txt");
            List<List<UserInfo>> partition = ListUtils.partition(userInfos, 10);
            partition.forEach(p -> {
                excelWriter.fill(p, writeSheet);//分页导出
            });

            // 写入list之前的数据
            Map<String, Object> map = new HashMap<>();
            map.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            excelWriter.fill(map, writeSheet);

            // list 后面还有个统计 想办法手动写入
            // 这里偷懒直接用list 也可以用对象
            List<List<String>> totalListList = new ArrayList<>();
            List<String> totalList = new ArrayList<>();
            totalListList.add(totalList);
            totalList.add(null);
            totalList.add(null);
            totalList.add(null);
            totalList.add(null);
            totalList.add(null);
            // 第四列
            totalList.add("统计:1000");
            // 这里是write 别和fill 搞错了
            excelWriter.write(totalListList, writeSheet);
            // 总体上写法比较复杂 但是也没有想到好的版本 异步的去写入excel 不支持行的删除和移动，也不支持备注这种的写入，所以也排除了可以
            // 新建一个 然后一点点复制过来的方案，最后导致list需要新增行的时候，后面的列的数据没法后移，后续会继续想想解决方案
        }
    }

}
