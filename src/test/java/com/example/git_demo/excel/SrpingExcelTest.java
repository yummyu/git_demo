package com.example.git_demo.excel;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.example.git_demo.entity.TUserInfo;
import com.example.git_demo.mapper.TUserMapper;
import com.example.git_demo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SrpingExcelTest {


    @Test
    public void testExportMergeRow() {
        TUserMapper bean = SpringUtil.getBean(TUserMapper.class);
        TUserInfo userInfoById = bean.getUserInfoById(1);
        log.info("{}", JSON.toJSONString(userInfoById));

    }

    @Test
    public void testBatchInsert() {
        StudentService bean = SpringUtil.getBean(StudentService.class);
        bean.saveStudentAndSubjetInfo();
        log.info("end...");

    }

    @Test
    public void testExportStudentInfo() {
        StudentService bean = SpringUtil.getBean(StudentService.class);
        bean.getExportStudentInfo();
        log.info("end");
    }


}
