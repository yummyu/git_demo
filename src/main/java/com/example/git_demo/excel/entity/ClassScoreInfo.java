package com.example.git_demo.excel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClassScoreInfo {


    private String className;


    private List<Student> students;

}
