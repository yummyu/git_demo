package com.example.git_demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchClassInfo {

    private int id;
    private String className;

    List<BatchStudentInfo> infoList;
}
