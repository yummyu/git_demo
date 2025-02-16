package com.example.git_demo.poiexcel.listeners;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import com.example.git_demo.poiexcel.entity.PicUser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class PicListener extends AnalysisEventListener<PicUser> {

    @Getter
    private List<PicUser> list = new ArrayList<>();
    @Override
    public void invoke(PicUser data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("final");
    }

}
