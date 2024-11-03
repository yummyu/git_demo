package com.example.git_demo.excel;

import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson2.JSON;
import com.example.git_demo.excel.service.ExcelDemoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void download(@RequestParam(value = "heads") String heads, HttpServletResponse response) throws IOException {
        log.info("开始文件下载");
        try {
//            excelDemoService.downloadDynamicNoEntity(heads, response);
            excelDemoService.downloadDynamicWithEntity(heads, response);
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
