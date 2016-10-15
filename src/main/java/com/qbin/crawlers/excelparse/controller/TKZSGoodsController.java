package com.qbin.crawlers.excelparse.controller;

import com.qbin.crawlers.common.util.ApplicationUtil;
import com.qbin.crawlers.excelparse.service.TKZSGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 描述：淘客助手文件解析
 * author qiaobin   2016/10/15 12:00.
 */
@RestController
@RequestMapping("tkzs")

public class TKZSGoodsController {
    @Autowired
    private TKZSGoodsService tkzsGoodsService;

    /**
     *   Excel文件上传
     * */
    @RequestMapping(value = "/excelparser", method = RequestMethod.POST)
    public String uploadPicture(@RequestParam("file") MultipartFile file){
        try {
            return tkzsGoodsService.importGoodsExcel(file.getInputStream());
        } catch (Exception e) {
            return String.format("图片上传失败：%s", e.getMessage());
        }
    }
    /**
     *   Excel文件上传
     * */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String aa(){
        try {
           return "aa";
        } catch (Exception e) {
            return String.format("图片上传失败：%s", e.getMessage());
        }
    }
}
