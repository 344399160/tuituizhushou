package com.qbin.crawlers.excelparse.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qbin.crawlers.common.util.ExcelPaser;
import com.qbin.crawlers.common.util.Json;
import com.qbin.crawlers.excelparse.model.TKZSGoods;
import com.qbin.crawlers.excelparse.repository.TKZSGoodsRepository;
import com.qbin.crawlers.excelparse.service.TKZSGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述：淘客助手-Excel数据源
 * author qiaobin   2016/10/15 11:38.
 */
@Service
public class TKZSGoodsServiceImpl implements TKZSGoodsService {

    @Autowired
    private TKZSGoodsRepository tkzsGoodsRepository;

    public String importGoodsExcel(byte contents) {
        long beginTime = System.currentTimeMillis();
        try {
            ExcelPaser excelReader = new ExcelPaser();
            FileInputStream is = new FileInputStream("F:\\11.xls");
            List<Map<String, String>> list = excelReader.readExcelContent(is);
            List<TKZSGoods> goods = Json.toObjectList(Json.toJsonString(list), TKZSGoods.class);
            if (goods.size() > 0) {
                tkzsGoodsRepository.save(goods);
            } else {
                return "文件无数据";
            }
            long cost = System.currentTimeMillis() - beginTime;
            return String.format("文件解析成功，共计 %s 条数据，耗时 %s 秒.", list.size(), cost/60);
        } catch (FileNotFoundException e) {
            return "文件不存在";
        } catch (IOException e) {
            return "文件字段映射失败";
        }
    }
}
