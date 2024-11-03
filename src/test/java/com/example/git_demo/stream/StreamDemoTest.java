package com.example.git_demo.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Slf4j
public class StreamDemoTest {

    static List<UserInfo> data;

    @BeforeAll
    public static void init() {
        data = UserInfo.getData("UserInfo.txt");
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
                collectingAndThen(
                        toCollection(
                                () -> new TreeSet<>(
                                        comparing(UserInfo::getPhone)
                                )
                        )
                        , ArrayList::new)
        );//排除后出现的数据
        collect1.forEach(user -> log.info("distinct1:{}", user));
        log.info("1 用时 ：{}", (System.currentTimeMillis() - startTime));
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
                });//排除后出现的数据

        List<UserInfo> collect2 = new ArrayList<>(uniqueUsersMap.values());
        collect2.forEach(user -> log.info("distinct2:{}", user));
        log.info("2 用时 ：{}", (System.currentTimeMillis() - startTime));
        log.info("=================");

        startTime = System.currentTimeMillis();
        Map<String, UserInfo> map2 = data.stream()
                .collect(LinkedHashMap::new, (m, o) -> m.put(o.getPhone(), o), LinkedHashMap::putAll);
        List<UserInfo> list5 = new ArrayList<>(map2.values());
        list5.forEach(user -> log.info("distinct3:{}", user));//无序

        log.info("3用时 ：{}", (System.currentTimeMillis() - startTime));
    }


    @Test
    public void testMap() {
        //将user信息转map，以手机号为key, (u1, u2) -> u1 如果手机号重复，就返回u1
        Map<String, UserInfo> collect = data.stream().collect(Collectors.toMap(UserInfo::getPhone, u -> u, (u1, u2) -> u1));
        collect.forEach((k, v) -> log.info("map {}<==>{}", k, v));
    }

    @Test
    public void testFilterAndPeek() {
        //筛选出年龄大于35的用户，并置为退休状态
        List<UserInfo> list = data.stream()
                .filter(userInfo -> userInfo.getAge() > 35)
                .peek(userInfo -> userInfo.setStatus("退休"))
                .toList();
        list.forEach(user -> log.info("userFilter:{}", user));
    }

    @Test
    public void testMatch() {
        //匹配存在年龄大于35的用户
        boolean anyMatch = data.stream().anyMatch(userInfo -> userInfo.getAge() > 35);
        log.info("anyMatch:{}", anyMatch);

        //匹配所有年龄大于35的用户
        boolean allMatch = data.stream().allMatch(userInfo -> userInfo.getAge() > 35);
        log.info("allMatch:{}", allMatch);

        //匹配不存在年龄大于35的用户
        boolean noneMatch = data.stream().noneMatch(userInfo -> userInfo.getAge() > 35);
        log.info("noneMatch:{}", noneMatch);
    }

    @Test
    public void testMinMax() {
        //获取年龄最小和最大的用户
        Optional<UserInfo> min = data.stream().min(Comparator.comparingInt(UserInfo::getAge));
        log.info("min:{}", min.get());
        Optional<UserInfo> max = data.stream().max(Comparator.comparingInt(UserInfo::getAge));
        log.info("max:{}", max.get());
        //统计年龄大于35的用户数量
        long count = data.stream().filter(userInfo -> userInfo.getAge() > 35).count();
        log.info("count:{}", count);
    }


    @Test
    public void testGroupByAndSort() {
        //按地址分组，按年龄大小排序
        Map<String, List<UserInfo>> collect = data.stream()
                .collect(Collectors
                        .groupingBy(UserInfo::getAddress, collectingAndThen(Collectors.toList(),
                                list -> list.stream().sorted(Comparator.comparingInt(UserInfo::getAge))
                                        .collect(Collectors.toList()))));
        collect.forEach((k, v) -> log.info("groupBy:{}==>{}", k, v));
    }

    @Test
    public void testPartition() {
        //将用户分为年龄大于35和小于35的两组
        Map<Boolean, List<UserInfo>> collect = data.stream().collect(Collectors.partitioningBy(userInfo -> userInfo.getAge() > 35));
        collect.forEach((k, v) -> log.info("partition:{}==>{}", k, v));
    }


    @Test
    public void testReduce() {
        //reduce 方法用于对流中的元素进行归约操作，将多个元素合并为一个结果
        Integer sum = data.stream().map(UserInfo::getAge).reduce(0, Integer::sum);
        log.info("sum:{}", sum);
    }

    @Test
    public void testDistinctAndRemoveDuplicate() {
        //根据地址，手机号去重
        List<UserInfo> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        Iterator<UserInfo> iterator = data.iterator();
        while (iterator.hasNext()) {
            UserInfo next = iterator.next();
            String address = next.getAddress();
            String phone = next.getPhone();
            if (set.contains(address + "_" + phone)) {
                list.add(next);
                iterator.remove();
            } else {
                set.add(address + "_" + phone);
            }
        }
        list.forEach(user -> log.info("distinct:{}", user));
        data.forEach(user -> log.info("distinctAndLimit:{}", user));


    }


}