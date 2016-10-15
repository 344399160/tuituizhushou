package com.qbin.crawlers.excelparse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 描述：淘客助手实体
 * author qiaobin   2016/10/15 11:19.
 */
@Data
@Entity
@Table(name = "`TKZSGOODS`")
@AllArgsConstructor
@NoArgsConstructor
public class TKZSGoods {
    @Id
    private String goodsId;

    @Column(name="`GOODSNAME`")
    private String goodsName;

    @Column(name="`GOODSPICTURE`")
    private String goodsPicture;

    @Lob
    @Column(name="`GOODSURL`")
    private String goodsUrl;

    @Column(name="`STORENAME`")
    private String storeName;

    @Column(name="`PRICE`")
    private String price;

    @Column(name="`SALESVOLUMN`")
    private String salesVolumn;

    @Column(name="`INCOMERATIO`")
    private String incomeRatio;

    @Column(name="`COMMISSION`")
    private String commission;

    @Column(name="`WANGWANG`")
    private String wangwang;

    @Lob
    @Column(name="`TBKURL`")
    private String tbkUrl;

    @Column(name="`COUPONCOUNT`")
    private String couponCount;

    @Column(name="`LEFTCOUPON`")
    private String leftCoupon;

    @Column(name="`COUPONPRICE`")
    private String couponPrice;

    @Column(name="`COUPONBEGINTIME`")
    private String couponBeginTime;

    @Column(name="`COUPONENDTIME`")
    private String couponEndTime;

    @Lob
    @Column(name="`COUPONURL`")
    private String couponUrl;

}
