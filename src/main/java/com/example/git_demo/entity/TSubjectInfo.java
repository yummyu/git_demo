package com.example.git_demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TSubjectInfo {

    private int id;
    private String subjectName;
    private String subjectScore;
    private int studentId;

}
