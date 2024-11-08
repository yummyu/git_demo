package com.example.git_demo.excel;

import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson2.JSON;
import com.example.git_demo.excel.service.ExcelDemoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(value = "/demo/excel")
public class ExcelDemoController {

    @Resource
    private ExcelDemoService excelDemoService;

    @GetMapping(value = "/test")
    public void test() {
        log.info("GIT_DEMO.md,官网示例");
    }

    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) throws IOException {
        log.info("开始文件下载");
        try {
            //无实体动态表头下载
//          excelDemoService.downloadDynamicNoEntity("heads", response);
            //有实体动态表头下载
//          excelDemoService.downloadDynamicWithEntity("heads", response);
            //固定表头下载
//            excelDemoService.downloadWithFixedHead(response);
            //数据量大的复杂填充，采用模板方式
            excelDemoService.complexFillWithTable(response);


        } catch (IOException e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }

    }


}
