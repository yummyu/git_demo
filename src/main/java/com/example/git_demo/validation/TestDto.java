package com.example.git_demo.validation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestDto {

    @NotNull
    private String name;

    @EnumValidator(enumValue = {"上海", "北京"},message = "值类型不在[上海, 北京]中")
    private String province;


}
