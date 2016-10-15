package com.qbin.crawlers.excelparse.repository;

import com.qbin.crawlers.excelparse.model.TKZSGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 描述：淘客助手商品接口
 * author qiaobin   2016/10/15 11:35.
 */
public interface TKZSGoodsRepository extends JpaRepository<TKZSGoods, String>, PagingAndSortingRepository<TKZSGoods, String> {
}
