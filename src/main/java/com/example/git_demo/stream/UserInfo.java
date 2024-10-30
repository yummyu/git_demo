package com.example.git_demo.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
@Data
public class UserInfo {

    private String name;
    private int age;
    private String address;
    private String phone;
    private String status;

    public static List<UserInfo> getData() {
        try {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "UserInfo.txt");

            if (!file.exists() || file.length() == 0) {
                throw new IOException("File does not exist or is empty: " + file.getAbsolutePath());
            }

            try (Stream<String> lines = Files.lines(file.toPath())) {
                return lines.filter(line -> line != null && !line.trim().isEmpty())
                        .map(line -> {
                            String[] str = line.split(",");
                            if (str.length < 4) {
                                throw new IllegalArgumentException("Invalid line format: " + line);
                            }
                            return new UserInfo(str[0], Integer.parseInt(str[1]), str[2], str[3],"");
                        })
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}

