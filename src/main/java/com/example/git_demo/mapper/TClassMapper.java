package com.example.git_demo.mapper;

import com.example.git_demo.entity.BatchClassInfo;
import com.example.git_demo.entity.TClassInfo;
import com.example.git_demo.entity.TStudentInfo;
import com.example.git_demo.entity.TSubjectInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TClassMapper {

    int saveClassInfo(TClassInfo classInfo);

    int saveBatchStudentInfo(@Param("list") List<TStudentInfo> list);

    int saveBatchSubjectInfo(@Param("list") List<TSubjectInfo> list);

    List<BatchClassInfo> getExportStudentInfo();

}
