package com.example.git_demo.mdc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mdc")
public class MdcDemoController {

    /**
     * MDC 设置 traceId 链路调用追踪
     * 注册拦截器 LogInterceptor
     * logging.pattern.console 设置 %X{traceId}
     */
    @GetMapping("/test/{name}")
    public String test(@PathVariable String name) {
        log.info("name:{}",name);
        return "test";
    }

}
