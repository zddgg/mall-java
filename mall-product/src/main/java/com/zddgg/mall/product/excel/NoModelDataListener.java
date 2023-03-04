package com.zddgg.mall.product.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.zddgg.mall.product.entity.RegionCity;
import com.zddgg.mall.product.entity.RegionDistrict;
import com.zddgg.mall.product.entity.RegionProvince;
import com.zddgg.mall.product.service.RegionCityService;
import com.zddgg.mall.product.service.RegionDistrictService;
import com.zddgg.mall.product.service.RegionProvinceService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 2000;
    private final RegionProvinceService provinceService;
    private final RegionCityService cityService;
    private final RegionDistrictService districtService;
    private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public NoModelDataListener(RegionProvinceService provinceService,
                               RegionCityService cityService,
                               RegionDistrictService districtService) {
        this.provinceService = provinceService;
        this.cityService = cityService;
        this.districtService = districtService;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (cachedDataList != null && cachedDataList.size() != 0) {
            saveData();
        }
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        cachedDataList.forEach(map -> {
            String code = map.get(0);
            String name = map.get(1);
            System.out.println(map + "              ===");
            if (code.endsWith("0000")) {
                RegionProvince province = new RegionProvince();
                province.setProvinceCode(code);
                province.setProvinceName(name);
                provinceService.save(province);
            } else if (code.endsWith("00")) {
                RegionCity city = new RegionCity();
                city.setCityCode(code);
                city.setCityName(name);
                cityService.save(city);
            } else {
                RegionDistrict district = new RegionDistrict();
                district.setDistrictCode(code);
                district.setDistrictName(name);
                districtService.save(district);
            }
        });
        log.info("存储数据库成功！");
    }
}
