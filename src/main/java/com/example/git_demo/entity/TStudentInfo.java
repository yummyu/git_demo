package com.example.git_demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TStudentInfo {

    private int id;
    private String userName;
    private String userNo;
    private String classId;

}
