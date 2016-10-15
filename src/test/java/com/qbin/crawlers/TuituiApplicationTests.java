package com.qbin.crawlers;

import com.qbin.crawlers.common.util.ExcelPaser;
import com.qbin.crawlers.common.util.Json;
import com.qbin.crawlers.common.util.PicDownloader;
import com.qbin.crawlers.excelparse.model.TKZSGoods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TuituiApplicationTests {

	@Test
	public void contextLoads() {
		String url = "https://ss2.baidu.com/73Z1bjeh1BF3odCf/it/u=3890329721,966988116&fm=202";
		PicDownloader.download(url, "a.jpg", "E:/picture");
	}

	@Test
	public void tee() {
		try {
			ExcelPaser excelReader = new ExcelPaser();
			long beginTime = System.currentTimeMillis();
			// 对读取Excel表格内容测试
			FileInputStream is = new FileInputStream("F:\\11.xls");
			List<Map<String, String>> list = excelReader.readExcelContent(is);
			System.out.println(list.size());
			try {
				List<TKZSGoods> goodses = Json.toObjectList(Json.toJsonString(list), TKZSGoods.class);
				System.out.println(goodses);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long cost = System.currentTimeMillis() - beginTime;
			String timeFormat = String.format(". cost : %s ms", cost);

		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
	}



}
