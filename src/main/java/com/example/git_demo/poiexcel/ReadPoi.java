package com.example.git_demo.poiexcel;

import cn.hutool.poi.excel.ExcelPicUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.idev.excel.EasyExcel;
import com.example.git_demo.poiexcel.entity.PicUser;
import com.example.git_demo.poiexcel.listeners.PicListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class ReadPoi {

    private static final Map<String, String> cellImageMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String excelFilePath = "C:\\Users\\Administrator\\Desktop\\新建 XLSX 工作表.xlsx";
        String outputDirectory = "C:\\Users\\Administrator\\Desktop\\";

        PicListener picListener = new PicListener();
        EasyExcel.read(excelFilePath,PicUser.class,picListener).sheet().doRead();
        List<PicUser> list = picListener.getList();
        list.forEach(System.out::println);
        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath)) {

            WPSExcelImportImgExtractor wpsExcelImportImgExtractor = new WPSExcelImportImgExtractor();
            Map<String, String> stringStringMap = wpsExcelImportImgExtractor.extractImages(fileInputStream, outputDirectory);

            stringStringMap.forEach((k, v) -> {
                log.info("图片:{},路径：{}",k, v);
            });

        } catch (Exception e) {
            log.error("读取excel失败", e);
        }

    }




}
