package com.example.git_demo.excel.entity;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Data
public class MergeEntity {

    @ExcelProperty("区域")
    private String address;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("手机号")
    private String phone;


    public static List<MergeEntity> getData(String fileName) {
        try {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);

            if (!file.exists() || file.length() == 0) {
                throw new IOException("File does not exist or is empty: " + file.getAbsolutePath());
            }

            try (Stream<String> lines = Files.lines(file.toPath())) {
                return lines.filter(line -> line != null && !line.trim().isEmpty())
                        .map(line -> {
                            String[] str = line.split(",");
                            return new MergeEntity(str[0], str[1], str[2]);
                        })
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        } catch (IOException e) {
            log.error("Error reading file:, ", e);
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            log.error("Error parsing file: ", e);
            return new ArrayList<>();
        }
    }

}
