package com.shuyoutech.crawler.processor;

import com.shuyoutech.api.enums.GameGoodsTypeEnum;
import com.shuyoutech.api.enums.GamePlatformEnum;
import com.shuyoutech.common.core.constant.StringConstants;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.NumberUtils;
import com.shuyoutech.common.core.util.SmUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.redis.util.RedisUtils;
import com.shuyoutech.crawler.domain.entity.CrawlerGameEntity;
import com.shuyoutech.crawler.domain.entity.CrawlerRecordEntity;
import com.shuyoutech.crawler.util.WebmagicUtils;
import com.shuyoutech.game.domain.entity.GameEntity;
import com.shuyoutech.game.domain.entity.GameGoodsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author YangChao
 * @date 2025-07-07 12:21
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameGoodsYxbSelenium {

    @Value("${shuyoutech.chrome.driver}")
    private String driverPath;

    public static final String REDIS_KEY_GAME_GOODS_UNIT_PRICE = "GameGoods_UnitPrice:";

    private static String genSm3Id(String platformId, String gameId, String bookId) {
        return SmUtils.sm3(platformId + "-" + gameId + "-" + bookId);
    }

    private static void updateGameGoodsUnitPrice(GameGoodsEntity goods) {
        String summaryKey;
        String gameArea = goods.getGameArea();
        String gameServer = goods.getGameServer();
        String gameRace = goods.getGameRace();
        if (StringUtils.isNotBlank(gameRace)) {
            summaryKey = gameArea + "-" + gameServer + "-" + gameRace;
        } else {
            summaryKey = gameArea + "-" + gameServer;
        }
        RedisUtils.hashPut(REDIS_KEY_GAME_GOODS_UNIT_PRICE + goods.getGameId(), summaryKey + "-" + goods.getPlatformName(), goods);
    }

    private void updateCrawlerRecord(String recordId, int crawlerCount) {
        if (StringUtils.isBlank(recordId)) {
            return;
        }
        CrawlerRecordEntity record = new CrawlerRecordEntity();
        record.setId(recordId);
        record.setCreateTime(new Date());
        record.setStatus("1");
        record.setCrawlerCount(crawlerCount);
        MongoUtils.patch(record);
    }

    public void dynamicParseGmmNewYxb(Request request) {
        try {
            String url = request.getUrl();
            System.setProperty("webdriver.chrome.driver", driverPath);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
            chromeOptions.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
            chromeOptions.setExperimentalOption("useAutomationExtension", false);
            WebDriver driver = new ChromeDriver(chromeOptions);
            driver.get(url);
            TimeUnit.SECONDS.sleep(10);
            String text = driver.getPageSource();
            driver.quit();
            if (StringUtils.isBlank(text)) {
                log.info("dynamicParseGmmNewYxb ========================== text is null");
                return;
            }
            // log.info("dynamicParseGmmNewYxb ========================== text:{}", text);
            Html html = Html.create(text);
            List<Selectable> nodes = html.xpath("//ul[@id='goods-list-ajax']/li").nodes();
            String recordId = request.getExtra("recordId");
            if (CollectionUtils.isEmpty(nodes)) {
                updateCrawlerRecord(recordId, 0);
                return;
            }
            GameEntity game = request.getExtra("game");
            CrawlerGameEntity crawler = request.getExtra("crawler");
            String gameArea = crawler.getGameArea();
            String gameServer = crawler.getGameServer();
            String gameRace = crawler.getGameRace();
            String gameId = game.getId();
            String id = request.getExtra("id");
            List<GameGoodsEntity> insertList = CollectionUtils.newArrayList();
            GameGoodsEntity goods;
            for (Selectable node : nodes) {
                goods = new GameGoodsEntity();
                goods.setStatus("1");
                goods.setPlatformId(GamePlatformEnum.PLATFORM_GMM.getValue());
                goods.setPlatformName(GamePlatformEnum.PLATFORM_GMM.getLabel());
                goods.setGoodsType(GameGoodsTypeEnum.GOODS_TYPE_YXB.getLabel());
                goods.setGameId(gameId);
                goods.setGameName(game.getGameName());
                goods.setGameArea(gameArea);
                goods.setGameServer(gameServer);
                goods.setGameRace(gameRace);
                goods.setBookId(node.xpath("//li[@class='media']/@data-bookid").toString());
                goods.setId(genSm3Id(GamePlatformEnum.PLATFORM_GMM.getValue(), gameId, goods.getBookId()));
                goods.setTitle(WebmagicUtils.getTextTrim(node.xpath("//h4[@class='media-heading']")).replaceAll("担保", "").replaceAll("寄售", ""));
                goods.setSellType(WebmagicUtils.getTextTrim(node.xpath("//h4[@class='media-heading']")).contains("担保") ? "担保" : "寄售");
                goods.setGoodsUrl("https://www.gmmsj.com" + node.xpath("//div[@class='media-body row']/a/@href").toString());
                goods.setPrice(Double.parseDouble(WebmagicUtils.getTextTrim(node.xpath("//i[@data-bind='text:price']"))));
                String textPriceStr = WebmagicUtils.getTextTrim(node.xpath("//p[@data-bind='text:extra_hint']"));
                goods.setPriceHint(textPriceStr + ";" + WebmagicUtils.getTextTrim(node.xpath("//p[@data-bind='text:extra_hint2']")));
                if (StringUtils.isNotBlank(textPriceStr) && textPriceStr.contains("1元=")) {
                    goods.setUnitPrice(Double.parseDouble(textPriceStr.replaceAll("1元=", "").replaceAll("万", "").replaceAll("基纳", "")));
                }
                goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
                goods.setGoodsStock(Integer.parseInt(WebmagicUtils.getTextTrim(node.xpath("//p[@data-bind='html:avail_qty']"))));
                insertList.add(goods);
                //log.info("dynamicParseGmmNewYxb ============== goods:{}", JSON.toJSONString(goods));
            }
            if (StringUtils.isNotBlank(recordId)) {
                updateCrawlerRecord(recordId, insertList.size());
            } else if (StringUtils.isNotBlank(id)) {
                //log.info("dynamicParseGmmNewYxb ============== crawlerId:{}, count:{}", crawler.getId(), insertList.size());
                RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_GMM.getValue() + StringConstants.HYPHEN + id, insertList);
            }
            if (CollectionUtils.isNotEmpty(insertList)) {
                updateGameGoodsUnitPrice(insertList.getFirst());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
