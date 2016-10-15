package com.qbin.crawlers.crawler.processor;

import com.qbin.crawlers.common.globalconst.RuleConst;
import com.qbin.crawlers.common.util.CrawlerUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 描述：淘宝客数据爬取
 * author qiaobin   2016/9/29 17:34.
 */
public class TaoBaoZhuShouProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);
    //计数器
    private static int count = 0;

    private static int type = 0;
    @Override
    public void process(Page page) {
        //首页    （抓商品链接 、分页链接）
        //分页     （抓商品链接）
        //商品细览页  （抓数据）
        if (CrawlerUtil.isMainPage(page)) { //首页处理
            //1.查看商品类别
            type = CrawlerUtil.goodsType(page);
            //2.将商品图片链接加入到待抓取列表中
            page.addTargetRequests(page.getHtml().xpath(RuleConst.GOODSLISTRULE).links().all()); //goodslist
            //3.将分页信息加入到待抓取列表中
            String lastPageUrl = CrawlerUtil.getLastPageUrl(page, RuleConst.LASTPAGEURL);
            int lastPageNo = CrawlerUtil.getLastPageNo(lastPageUrl);
            page.addTargetRequests(CrawlerUtil.getPageUrls(lastPageNo, lastPageUrl));
        } else if (page.getUrl().regex(RuleConst.PAGEREGEX).match()){
            //将商品图片链接加入到待抓取列表中
            page.addTargetRequests(page.getHtml().xpath(RuleConst.GOODSLISTRULE).links().all()); //goodslist
        } else {
            //正文爬取
            count ++;
            String title = CrawlerUtil.getValue(page, RuleConst.TITLE); //标题
            String picture = CrawlerUtil.getValue(page, RuleConst.PICTRURE);
            String describe = CrawlerUtil.getValue(page, RuleConst.DESCRIBE); //商品描述
            String quanhoujia = CrawlerUtil.getValue(page, RuleConst.QUANHOUJIA); //券后价
            String zaishoujia = CrawlerUtil.getValue(page, RuleConst.ZAISHOUJIA); //在售价
            String youhuiquan = CrawlerUtil.getValue(page, RuleConst.YOUHUIQUAN); //优惠券金额
            String youhuiquanbeizhu = CrawlerUtil.getValue(page, RuleConst.YOUHUIQUANBEIZHU); //优惠券说明
            String yongjin = CrawlerUtil.getValue(page, RuleConst.YONGJIN); //佣金
            String wenan = CrawlerUtil.getValue(page, RuleConst.WENAN); //文案
            String pcyouhuihref = CrawlerUtil.getValue(page, RuleConst.PCYOUHUIHREF); //PC领取优惠券入口
            String phoneyouhuihref = CrawlerUtil.getValue(page, RuleConst.PHONEYOUHUIHREF); //手机领取优惠券入口
            String goodshref = CrawlerUtil.getValue(page, RuleConst.GOODSHREF); //商品链接

        }

    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        long beginTime ,endTime;
        System.out.println("========淘客助手爬虫【启动】喽！=========");
        Spider.create(new TaoBaoZhuShouProcessor()).addUrl(RuleConst.NVZHUANG)
                .thread(5).run();
        //
        System.out.println(String.format("共抓取%s条数据", count));
        System.out.println("========淘客助手爬虫【结束】喽！=========");
    }
}
