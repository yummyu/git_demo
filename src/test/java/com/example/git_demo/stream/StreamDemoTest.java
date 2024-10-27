package com.example.git_demo.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class StreamDemoTest {

    static List<UserInfo> data;

    @BeforeAll
    public static void init() {
        data = UserInfo.getData();
    }

    @Test
    public void testFilter() {
        //筛选出年龄大于30的用户
        List<UserInfo> list = data.stream().filter(userInfo -> userInfo.getAge() > 30).toList();
        list.forEach(user -> log.info("userFilter:{}", user));
    }

    @Test
    public void testSort() {
        //按年龄排序，默认从小到大
        List<UserInfo> list = data.stream().sorted(Comparator.comparingInt(UserInfo::getAge)).toList();
        list.forEach(user -> log.info("userSort:{}", user));


    }

    @Test
    public void testDistinct() {
        //去除手机号重复的数据 -- 宋江-高俅 手机号重复
        long startTime = System.currentTimeMillis();
        ArrayList<UserInfo> collect1 = data.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(
                                () -> new TreeSet<>(
                                        Comparator.comparing(UserInfo::getPhone)
                                )
                        )
                        , ArrayList::new)
        );
        collect1.forEach(user -> log.info("distinct1:{}", user));
        log.info("1 用时 ：{}",(System.currentTimeMillis() - startTime));
        log.info("=================");

        startTime = System.currentTimeMillis();
        Map<String, UserInfo> uniqueUsersMap = new LinkedHashMap<>();
        data.stream()
                .filter(Objects::nonNull)
                .forEach(user -> {
                    String phone = user.getPhone();
                    if (phone != null && !uniqueUsersMap.containsKey(phone)) {
                        uniqueUsersMap.put(phone, user);
                    }
                });

        List<UserInfo> collect2 = new ArrayList<>(uniqueUsersMap.values());
        collect2.forEach(user -> log.info("distinct2:{}", user));
        log.info("2 用时 ：{}",(System.currentTimeMillis() - startTime));
        log.info("=================");

        startTime = System.currentTimeMillis();
        Map<String, UserInfo> map2 = data.stream()
                .collect(LinkedHashMap::new, (m, o) -> m.put(o.getPhone(), o), LinkedHashMap::putAll);
        List<UserInfo> list5 = new ArrayList<>(map2.values());
        list5.forEach(user -> log.info("distinct3:{}", user));

        log.info("3用时 ：{}",(System.currentTimeMillis() - startTime));
    }


    @Test
    public void testMap() {
        //将user信息转map，以手机号为key, (u1, u2) -> u1 如果手机号重复，就返回u1
        Map<String, UserInfo> collect = data.stream().collect(Collectors.toMap(UserInfo::getPhone, u -> u, (u1, u2) -> u1));
        collect.forEach((k, v) -> log.info("map {}<==>{}", k, v));

    }

}