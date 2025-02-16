package com.example.git_demo.excel.listeners;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表头校验
 */
@Slf4j
public class HeadCheckListener<T> extends AnalysisEventListener<T> {

    private final Class<T> clazz;
    private final List<String> expectedHeaders = new ArrayList<>();
    private boolean isFileEmpty = true;

    public HeadCheckListener(Class<T> clazz) {
        this.clazz = clazz;
        // 初始化预期表头
        initExpectedHeaders();
    }

    /**
     * 验证表头
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        List<String> actualHeaders = new ArrayList<>(headMap.values());
        if (!actualHeaders.equals(expectedHeaders)) {
            log.info("表头验证失败！");
            log.info("预期表头: {}", expectedHeaders);
            log.info("实际表头: {}" ,actualHeaders);
        } else {
            log.info("表头验证成功！");
        }
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        isFileEmpty = false;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 所有数据解析完成后执行
        if (isFileEmpty) {
            log.info("Excel 文件为空，无法验证表头！");
        }
    }

    /**
     * 初始化预期表头
     */
    private void initExpectedHeaders() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                // 获取注解中的表头名称
                String[] headers = excelProperty.value();
                if (headers.length > 0) {
                    expectedHeaders.add(headers[0]); // 取第一个值作为表头
                }
            }
        }
    }

}