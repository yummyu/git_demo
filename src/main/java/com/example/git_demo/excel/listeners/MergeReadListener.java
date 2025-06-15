package com.example.git_demo.excel.listeners;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.enums.CellExtraTypeEnum;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.metadata.CellExtra;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class MergeReadListener extends AnalysisEventListener<Map<Integer, Object>> {
    private final List<Map<Integer, Object>> dataList = new ArrayList<>();

    private final List<CellExtra> extraList = new ArrayList<>();

    //处理合并单元格中空行问题 -> 实际行索引 - 实际数据索引
    private final Map<Integer, Integer> physicalRow = new HashMap<>();

    private final int headRow;

    int currentRow = 1;

    public MergeReadListener(int headRow){
        this.headRow = headRow;
    }


    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        Integer approximateTotalRowNumber = context.readSheetHolder().getApproximateTotalRowNumber();
        if (approximateTotalRowNumber == null) {
            log.warn("approximateTotalRowNumber is null, skipping.");
            return;
        } else {
            log.info("approximateTotalRowNumber:{}", approximateTotalRowNumber);
        }

        Integer rowIndex = context.readSheetHolder().getRowIndex();
        log.info("当前数据行:{}", rowIndex);

        log.info("Processing data: {}", data);
        if (data != null) {
            dataList.add(data);
        } else {
            log.warn("Encountered null data, skipping.");
        }

        physicalRow.put(rowIndex, currentRow++);

//        String validate = ValidatorUtil.validate(data);
//        log.info("validate:{}", validate);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("All data parsed, starting to fill data list by merge.");
        fillDataListByMerge(dataList, extraList);
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
    private  void fillDataListByMerge(List<Map<Integer, Object>> dataList, List<CellExtra> extraList) {
        for (CellExtra cellExtra : extraList) {
//            Integer rowIndex = cellExtra.getRowIndex();
            Integer rowIndex = physicalRow.get(cellExtra.getRowIndex());
            if (rowIndex < headRow) {
                continue;
            }
            log.info("actualRow: {}", rowIndex);
            int dataListIndex = rowIndex - headRow;
            Integer dataMapKey = cellExtra.getColumnIndex();
            Map<Integer, Object> dataMap = dataList.get(dataListIndex);
            int firstRowIndex = physicalRow.get(cellExtra.getFirstRowIndex()) - headRow;
            int lastRowIndex =  physicalRow.get(cellExtra.getLastRowIndex()) - headRow;
            Integer firstColumnIndex = cellExtra.getFirstColumnIndex();
            Integer lastColumnIndex = cellExtra.getLastColumnIndex();
            Object value = dataMap.get(dataMapKey);
            log.info("Handling cell merge at row index: {}, column index: {}", rowIndex, dataMapKey);

            handleHorizontalMerge(dataMap, firstColumnIndex, lastColumnIndex, value);
            handleVerticalMerge(dataList, firstRowIndex, lastRowIndex, firstColumnIndex, lastColumnIndex, value);
        }
    }

    private void handleHorizontalMerge(Map<Integer, Object> dataMap, Integer firstColumnIndex, Integer lastColumnIndex, Object value) {
        for (int i = firstColumnIndex + 1; i < lastColumnIndex + 1; i++) {
            dataMap.put(i, value);
            log.debug("Horizontal merge: setting column index {} to value {}", i, value);
        }
    }

    private void handleVerticalMerge(List<Map<Integer, Object>> dataList, int firstRowIndex, int lastRowIndex, Integer firstColumnIndex, Integer lastColumnIndex, Object value) {
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
