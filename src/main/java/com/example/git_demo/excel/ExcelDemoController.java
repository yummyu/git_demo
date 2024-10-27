package com.example.git_demo.excel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller(value = "/demo/excel")
public class ExcelDemoController {

    @GetMapping(value = "/test")
    public void test(){
        log.info("GIT_DEMO.md,官网示例");
    }

}
