package com.qbin.crawlers.excelparse.service.impl;

import com.qbin.crawlers.common.util.ExcelPaser;
import com.qbin.crawlers.common.util.Json;
import com.qbin.crawlers.excelparse.model.TKZSGoods;
import com.qbin.crawlers.excelparse.repository.TKZSGoodsRepository;
import com.qbin.crawlers.excelparse.service.TKZSGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    /**
      * 功能描述：Excel解析
      * @author qiaobin
      * @date 2016/10/15  14:02
      * @param
      */
    public String importGoodsExcel(InputStream fis) {
        long beginTime = System.currentTimeMillis();
        try {
            List<Map<String, String>> list = ExcelPaser.readExcelContent(fis);
            List<TKZSGoods> goods = Json.toObjectList(Json.toJsonString(list), TKZSGoods.class);
            if (goods.size() > 0) {
                tkzsGoodsRepository.save(goods);
            } else {
                return "文件无数据";
            }
            long cost = System.currentTimeMillis() - beginTime;
            return String.format("文件解析成功，共计 %s 条数据，耗时 %s 秒.", list.size(), cost/1000);
        } catch (FileNotFoundException e) {
            return "文件不存在";
        } catch (IOException e) {
            return "文件字段映射失败";
        }
    }
}
