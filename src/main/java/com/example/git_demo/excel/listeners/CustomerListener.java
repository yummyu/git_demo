package com.example.git_demo.excel.listeners;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.fastjson2.JSON;
import com.example.git_demo.excel.entity.MultiHeads;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class CustomerListener extends AnalysisEventListener<MultiHeads> {

    private List<MultiHeads> list = new ArrayList<>();
    private List<CellExtra> extraMergeInfoList = new ArrayList<>();
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
