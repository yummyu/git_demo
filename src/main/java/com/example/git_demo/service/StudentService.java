package com.example.git_demo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.metadata.WriteSheet;
import com.example.git_demo.entity.*;
import com.example.git_demo.excel.merge.DebuggableMergeStrategy;
import com.example.git_demo.excel.merge.MergeRowWriteHandler;
import com.example.git_demo.excel.merge.RowMergeCondition;
import com.example.git_demo.mapper.TClassMapper;
import com.example.git_demo.utils.RandomNameGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {

    @Resource
    private TClassMapper tClassMapper;

    @Transactional
    public void saveStudentAndSubjetInfo() {
        String[] subjectName = {"语文", "数学", "英语", "物理", "化学", "生物", "历史", "地理"};

        List<TStudentInfo> studentInfos = new ArrayList<>();
        List<TSubjectInfo> subjectInfos = new ArrayList<>();
        TStudentInfo studentInfo;
        TSubjectInfo subjectInfo;

        List<String> names = RandomNameGenerator.generateRandomNames(450).stream().distinct().collect(Collectors.toList());

        int i = 1;
        String prefix = "2025";
        for (String name : names) {
            int id = i % 8 == 0 ? 8 : i % 8;

            String userNo = prefix + id + StrUtil.fillBefore(i + "", '0', 4);
            studentInfo = TStudentInfo.builder().id(i).classId(id + "").userName(name).userNo(userNo).build();

            studentInfos.add(studentInfo);

            for (String s : subjectName) {
                subjectInfo = TSubjectInfo.builder().studentId(i).subjectName(s).subjectScore(String.valueOf(RandomUtil.randomInt(60, 100))).build();
                subjectInfos.add(subjectInfo);
            }

            i++;
        }

        int i1 = tClassMapper.saveBatchStudentInfo(studentInfos);

        int i2 = tClassMapper.saveBatchSubjectInfo(subjectInfos);

        log.info("save:{}->{]", i1, i2);
    }


    public void getExportStudentInfo() {

        List<BatchClassInfo> infoList = tClassMapper.getExportStudentInfo();

        List<ExportClassInfo> classInfos = new ArrayList<>();

        List<ExportStudentInfo> exportStudentInfos = new ArrayList<>();

        infoList.forEach(batchClassInfo -> {

            ExportClassInfo exportClassInfo = new ExportClassInfo();
            BeanUtil.copyProperties(batchClassInfo, exportClassInfo);
            classInfos.add(exportClassInfo);

            List<BatchStudentInfo> infoList1 = batchClassInfo.getInfoList();
            infoList1.forEach(batchStudentInfo -> {
                ExportStudentInfo exportStudentInfo = new ExportStudentInfo();
                BeanUtil.copyProperties(batchStudentInfo, exportStudentInfo);
                exportStudentInfo.setClassId(String.valueOf(batchClassInfo.getId()));
                exportStudentInfos.add(exportStudentInfo);
            });

        });

        Integer[] mergeColumn = {0,1,2};

        ExcelWriter excelWriter = EasyExcel.write("C:\\Users\\Administrator\\Desktop\\BatchStudentInfo.xlsx").build() ;

        WriteSheet writeSheet1 = EasyExcel.writerSheet(0, "班级信息").head(ExportClassInfo.class).build();
        WriteSheet writeSheet2 = EasyExcel.writerSheet(1, "学生信息").registerWriteHandler(new MergeRowWriteHandler(1, new RowMergeCondition(mergeColumn),mergeColumn)).head(ExportStudentInfo.class).build();

        StopWatch batchStudentInfo = StopWatch.create("BatchStudentInfo");
        batchStudentInfo.start();
        excelWriter.write(classInfos,writeSheet1);
        excelWriter.write(exportStudentInfos,writeSheet2);

        batchStudentInfo.stop();

        log.info("BatchStudentInfo:{}",batchStudentInfo.shortSummary(TimeUnit.SECONDS));

        excelWriter.finish();

    }

}
