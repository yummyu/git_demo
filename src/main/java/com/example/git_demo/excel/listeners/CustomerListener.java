package com.example.git_demo.excel.listeners;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.enums.CellExtraTypeEnum;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.metadata.CellExtra;
import com.alibaba.fastjson2.JSON;
import com.example.git_demo.excel.entity.MultiHeads;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class CustomerListener extends AnalysisEventListener<MultiHeads> {

    private final List<MultiHeads> list = new ArrayList<>();
    private final List<CellExtra> extraMergeInfoList = new ArrayList<>();
    @Override
    public void invoke(MultiHeads data, AnalysisContext context) {
        log.info("读取到:{}", JSON.toJSONString(data));
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("final");
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        if (extra.getType() == CellExtraTypeEnum.MERGE) {
            extraMergeInfoList.add(extra);
        }
    }
}
