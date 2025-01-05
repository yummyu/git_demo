package com.example.git_demo.excel.listeners;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.enums.CellExtraTypeEnum;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.metadata.CellExtra;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class MergeReadListener extends AnalysisEventListener<Map<Integer, Object>> {
    private List<Map<Integer, Object>> dataList = new ArrayList<>();

    private List<CellExtra> extraList = new ArrayList<>();


    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        Integer approximateTotalRowNumber = context.readSheetHolder().getApproximateTotalRowNumber();
        if (approximateTotalRowNumber == null) {
            log.warn("approximateTotalRowNumber is null, skipping.");
            return;
        }else {
            log.info("approximateTotalRowNumber:{}", approximateTotalRowNumber);
        }
        log.info("Processing data: {}", data);
        if (data != null) {
            dataList.add(data);
        } else {
            log.warn("Encountered null data, skipping.");
        }
//        String validate = ValidatorUtil.validate(data);
//        log.info("validate:{}", validate);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("All data parsed, starting to fill data list by merge.");
        fillDataListByMerge(dataList, extraList, 1);
        log.info("Data list filled by merge.");

        dataList.forEach(data -> log.info("Processed data: {}", data));
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        if (extra.getType() == CellExtraTypeEnum.MERGE) {
            extraList.add(extra);
        }
    }

    //copy https://blog.csdn.net/weixin_45186601/article/details/136684653
    private static void fillDataListByMerge(List<Map<Integer, Object>> dataList, List<CellExtra> extraList, Integer headRow) {
        for (CellExtra cellExtra : extraList) {
            Integer rowIndex = cellExtra.getRowIndex();
            if (rowIndex < headRow) {
                continue;
            }
            int dataListIndex = rowIndex - headRow;
            Integer dataMapKey = cellExtra.getColumnIndex();
            Map<Integer, Object> dataMap = dataList.get(dataListIndex);
            int firstRowIndex = cellExtra.getFirstRowIndex() - headRow;
            int lastRowIndex = cellExtra.getLastRowIndex() - headRow;
            Integer firstColumnIndex = cellExtra.getFirstColumnIndex();
            Integer lastColumnIndex = cellExtra.getLastColumnIndex();
            Object value = dataMap.get(dataMapKey);
            log.debug("Handling cell merge at row index: {}, column index: {}", rowIndex, dataMapKey);

            handleHorizontalMerge(dataMap, firstColumnIndex, lastColumnIndex, value);
            handleVerticalMerge(dataList, firstRowIndex, lastRowIndex, firstColumnIndex, lastColumnIndex, value);
        }
    }

    private static void handleHorizontalMerge(Map<Integer, Object> dataMap, Integer firstColumnIndex, Integer lastColumnIndex, Object value) {
        for (int i = firstColumnIndex + 1; i < lastColumnIndex + 1; i++) {
            dataMap.put(i, value);
            log.debug("Horizontal merge: setting column index {} to value {}", i, value);
        }
    }

    private static void handleVerticalMerge(List<Map<Integer, Object>> dataList, int firstRowIndex, int lastRowIndex, Integer firstColumnIndex, Integer lastColumnIndex, Object value) {
        for (int i = firstRowIndex + 1; i < lastRowIndex + 1; i++) {
            Map<Integer, Object> integerObjectMap = dataList.get(i);
            integerObjectMap.put(firstColumnIndex, value);
            log.debug("Vertical merge: setting row index {} column index {} to value {}", i, firstColumnIndex, value);
            if (!firstColumnIndex.equals(lastColumnIndex)) {
                for (int j = firstColumnIndex + 1; j < lastColumnIndex + 1; j++) {
                    integerObjectMap.put(j, value);
                    log.debug("Vertical merge: setting row index {} column index {} to value {}", i, j, value);
                }
            }
        }
    }

    /*private void insertDataToDatabase(List<Map<Integer, Object>> dataList) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DataItemMapper mapper = session.getMapper(DataItemMapper.class);
            List<DataItem> dataItems = new ArrayList<>();
            for (Map<Integer, Object> data : dataList) {
                DataItem item = new DataItem();
                item.setColumn1((String) data.get(0)); // 假设第一列是column1
                item.setColumn2((String) data.get(1)); // 假设第二列是column2
                item.setColumn3((String) data.get(2)); // 假设第三列是column3
                dataItems.add(item);
            }
            mapper.insertDataItems(dataItems);
            session.commit();
        } catch (Exception e) {
            log.error("Error inserting data into database", e);
        }
    }*/
}
