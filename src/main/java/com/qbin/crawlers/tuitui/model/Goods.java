package com.qbin.crawlers.tuitui.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 描述：商品表
 * author qiaobin   2016/10/10 18:23.
 */
@Data
@Entity
@Table(name = "`GOODS`")
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    /**
     *  主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goodsId;
    /**
     *  标题
     */
    @Column(name="`TITLE`")
    private String title;
    /**
     *  图片
     */
    @Column(name="`PICTURE`")
    private String picture;
    /**
     *  商品描述
     */
    @Column(name="`DESCRIBE`")
    private String describe;
    /**
     *  券后价
     */
    @Column(name="`DISCOUNTEDPRICE`")
    private String discountedPrice;
    /**
     *  在售价
     */
    @Column(name="`PRICE`")
    private String price;
    /**
     *  优惠券金额
     */
    @Column(name="`YOUHUIQUAN`")
    private String coupon;
    /**
     *  优惠券说明
     */
    @Column(name="`COUPONREMARK`")
    private String couponRemark;
    /**
     *  佣金
     */
    @Column(name="`COMMISSION`")
    private String commission;
    /**
     *  文案
     */
    @Column(name="`WENAN`")
    private String wenan;
    /**
     *  PC领取优惠券入口
     */
    @Column(name="`PCCOUPONHREF`")
    private String pcCouponHref;
    /**
     *  手机领取优惠券入口
     */
    @Column(name="`PHONECOUPONHREF`")
    private String phoneCouponHref;
    /**
     *  商品链接
     */
    @Column(name="`GOODSHREF`")
    private String goodsHref;

    /**
     *  商品类别
     */
    @Column(name="`GOODSTYPE`")
    private int goodsType;

    /*
     *  创建时间
     * */
    @Column(name = "`CREATETIME`",nullable = false, updatable = false, insertable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Date createTime;

}
