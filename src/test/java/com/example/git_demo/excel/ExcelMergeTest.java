package com.example.git_demo.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.metadata.WriteSheet;
import com.example.git_demo.excel.entity.*;
import com.example.git_demo.excel.listeners.MergeCellWriteHandler;
import com.example.git_demo.excel.merge.MergeRowWriteHandler;
import com.example.git_demo.excel.merge.RowMergeCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExcelMergeTest {

    static List<MergeEntity> data;
    static String path = "C:\\Users\\Administrator\\Desktop\\";

    @BeforeAll
    public static void init() {

    }

    @Test
    public void testMergeCell() {
        data = MergeEntity.getData("MergeInfo.txt");
        path = path + "mergeCell.xlsx";
        EasyExcel.write(path, MergeEntity.class)
                .registerWriteHandler(new MergeCellWriteHandler(0, new int[]{0, 1}))
                .sheet("sheet1")
                .doWrite(data);

    }

    /**
     * 测试导出带合并单元格
     */
    @Test
    public void testExportMergeCell() {
        path = path + "ClassScoreInfo.xlsx";
        List<ClassScoreInfo> list = getData();

        List<ClassScoreExportInfo> exportInfoList = BeanUtil.copyToList(list,ClassScoreExportInfo.class);
        List<StudentExport> studentExportList = list.stream().flatMap(v -> v.getStudents().stream()).map(v -> BeanUtil.copyProperties(v,StudentExport.class)).toList() ;


        ExcelWriter build = EasyExcel.write(path).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("班级").build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet("学生").registerWriteHandler(new MergeRowWriteHandler(0, new RowMergeCondition())).build();

        build.write(exportInfoList, writeSheet);
        build.write(studentExportList, writeSheet1);
        build.finish();

    }


    private List<ClassScoreInfo> getData() {
        List<ClassScoreInfo> list = new ArrayList<>();

        List<Student> students;

        ClassScoreInfo classScoreInfo;
        for (int i = 0; i < 3; i++) {

            students = new ArrayList<>();
            students.add(new Student("张三" + i, "语文", "90"));
            students.add(new Student("张三" + i, "数学", "90"));
            students.add(new Student("张三" + i, "英语", "90"));

            classScoreInfo = new ClassScoreInfo("班级" + i, students);

            list.add(classScoreInfo);
        }

        return list;

    }


}