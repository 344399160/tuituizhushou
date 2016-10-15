package com.qbin.crawlers.excelparse.service;

import java.io.InputStream;

/**
 * 描述：淘客助手-Excel数据源
 * author qiaobin   2016/10/15 11:38.
 */
public interface TKZSGoodsService {
    /**
     * 功能描述：Excel解析
     *
     * @param
     * @author qiaobin
     * @date 2016/10/15  14:02
     */
    public String importGoodsExcel(InputStream fis);
}
