package com.shuyoutech.crawler.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 12:21
 **/
@Slf4j
public class GameGoodsYxbPageListProcessor implements PageProcessor {

    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(5000);

    public static final String REDIS_KEY_GAME_GOODS_UNIT_PRICE = "GameGoods_UnitPrice:";

    @Override
    public void process(Page page) {
        String platformId = page.getRequest().getExtra("platformId");
        if (GamePlatformEnum.PLATFORM_GMM.getValue().equals(platformId)) {
            dynamicParseGmmNewYxb(page);
        } else if (GamePlatformEnum.PLATFORM_5173.getValue().equals(platformId)) {
            dynamicParse5173Yxb(page);
        } else if (GamePlatformEnum.PLATFORM_DD373.getValue().equals(platformId)) {
            dynamicParse373Yxb(page);
        } else if (GamePlatformEnum.PLATFORM_UU898.getValue().equals(platformId)) {
            dynamicParse898Yxb(page);
        } else if (GamePlatformEnum.PLATFORM_7881.getValue().equals(platformId)) {
            dynamicParse7881Yxb(page);
        }
    }

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

    private void dynamicParseGmmYxb(Page page) {
        String body = page.getRawText();
        if (!JSON.isValidObject(body)) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(body);
        if (null == jsonObject || 0 != jsonObject.getInteger("return_code")) {
            return;
        }
        Request request = page.getRequest();
        String recordId = request.getExtra("recordId");
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("goodsList");
        if (null == jsonArray || jsonArray.isEmpty()) {
            updateCrawlerRecord(recordId, 0);
            return;
        }
        GameEntity game = request.getExtra("game");
        CrawlerGameEntity crawler = request.getExtra("crawler");
        String id = request.getExtra("id");
        String gameId = game.getId();
        String platformGameId = crawler.getPlatformGameId();
        String gameArea = crawler.getGameArea();
        String gameServer = crawler.getGameServer();
        String gameRace = crawler.getGameRace();
        Date date = new Date();
        List<GameGoodsEntity> insertList = CollectionUtils.newArrayList();
        GameGoodsEntity goods;
        JSONObject object;
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                object = jsonArray.getJSONObject(i);
                goods = new GameGoodsEntity();
                goods.setStatus("1");
                goods.setUpdateTime(date);
                goods.setPlatformId(GamePlatformEnum.PLATFORM_GMM.getValue());
                goods.setPlatformName(GamePlatformEnum.PLATFORM_GMM.getLabel());
                goods.setGoodsType(GameGoodsTypeEnum.GOODS_TYPE_YXB.getLabel());
                goods.setBookId(object.getString("book_id"));
                goods.setId(genSm3Id(GamePlatformEnum.PLATFORM_GMM.getValue(), gameId, goods.getBookId()));
                goods.setGameId(gameId);
                goods.setGameName(game.getGameName());
                goods.setGameArea(gameArea);
                goods.setGameServer(gameServer);
                goods.setGameRace(gameRace);
                goods.setTitle(object.getString("goods_list_title"));
                goods.setSellType("35".equals(object.getString("goods_type")) ? "担保" : "寄售");
                if (goods.getBookId().startsWith("DBP")) {
                    goods.setGoodsUrl(StringUtils.format("https://www.gmmsj.com/dy/{}_dbjb/detail_{}.shtml", platformGameId, goods.getBookId()));
                } else {
                    goods.setGoodsUrl(StringUtils.format("https://www.gmmsj.com/dy/{}_jb/detail_{}.shtml", platformGameId, goods.getBookId()));
                }
                goods.setPrice(object.getDouble("price"));
                goods.setPriceHint(object.getString("extra_hint") + ";" + object.getString("extra_hint2"));
                goods.setUnitPrice(Double.parseDouble(object.getString("extra_hint").replaceAll("1元=", "").replaceAll("万基纳", "")));
                goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
                goods.setGoodsStock(object.getInteger("avail_qty"));
                insertList.add(goods);
            } catch (Exception e) {
                log.error("dynamicParseGmm =============================== exception:{}", e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(recordId)) {
            updateCrawlerRecord(recordId, insertList.size());
        } else if (StringUtils.isNotBlank(id)) {
            RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_GMM.getValue() + StringConstants.HYPHEN + id, insertList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            updateGameGoodsUnitPrice(insertList.getFirst());
        }
    }

    private void dynamicParseGmmNewYxb(Page page) {
        Html html = page.getHtml();
        Request request = page.getRequest();
        String recordId = request.getExtra("recordId");
        List<Selectable> nodes = html.xpath("//ul[@id='goods-list-ajax']/li").nodes();
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
            goods.setTitle(WebmagicUtils.getTextTrim(node.xpath("//h4[@class='media-heading']")));
            goods.setSellType(WebmagicUtils.getTextTrim(node.xpath("//h4[@class='media-heading']")).contains("担保") ? "担保" : "寄售");
            goods.setGoodsUrl("https://www.gmmsj.com" + node.xpath("//div[@class='media-body row']/a/@href").toString());
            goods.setPrice(Double.parseDouble(WebmagicUtils.getTextTrim(node.xpath("//i[@data-bind='text:price']"))));
            String textPriceStr = WebmagicUtils.getTextTrim(node.xpath("//p[@data-bind='text:price']"));
            goods.setPriceHint(textPriceStr + ";" + WebmagicUtils.getTextTrim(node.xpath("//p[@data-bind='text:extra_hint2']")));
            if (StringUtils.isNotBlank(textPriceStr) && textPriceStr.contains("1元=")) {
                goods.setUnitPrice(Double.parseDouble(textPriceStr.replaceAll("1元=", "").replaceAll("万基纳", "")));
            }
            goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
            goods.setGoodsStock(Integer.parseInt(WebmagicUtils.getTextTrim(node.xpath("//p[@data-bind='html:avail_qty']"))));
        }
        if (StringUtils.isNotBlank(recordId)) {
            updateCrawlerRecord(recordId, insertList.size());
        } else if (StringUtils.isNotBlank(id)) {
            log.info("dynamicParseGmmNewYxb ============== crawlerId:{}, count:{}", crawler.getId(), insertList.size());
            RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_GMM.getValue() + StringConstants.HYPHEN + id, insertList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            updateGameGoodsUnitPrice(insertList.getFirst());
        }
    }

    private void dynamicParse5173Yxb(Page page) {
        Html html = page.getHtml();
        Request request = page.getRequest();
        String recordId = request.getExtra("recordId");
        List<Selectable> nodes = html.xpath("//div[@class='sin_pdlbox']").nodes();
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
        String bookId;
        Selectable selectableOne;
        for (Selectable node : nodes) {
            goods = new GameGoodsEntity();
            goods.setStatus("1");
            goods.setPlatformId(GamePlatformEnum.PLATFORM_5173.getValue());
            goods.setPlatformName(GamePlatformEnum.PLATFORM_5173.getLabel());
            goods.setGoodsType(GameGoodsTypeEnum.GOODS_TYPE_YXB.getLabel());
            goods.setGameId(gameId);
            goods.setGameName(game.getGameName());
            goods.setGameArea(gameArea);
            goods.setGameServer(gameServer);
            goods.setGameRace(gameRace);
            selectableOne = node.xpath("//ul[@class='pdlist_info']");
            if (!selectableOne.match()) {
                log.error("parse5173 ===================== node:{}", node.get());
                continue;
            }
            goods.setTitle(WebmagicUtils.getTextTrim(selectableOne.xpath("//li[@class='tt']")));
            goods.setGoodsUrl(selectableOne.xpath("//li[@class='tt']/h2/a/@href").toString().trim());
            bookId = StringUtils.subBefore(StringUtils.subAfter(goods.getGoodsUrl(), "/", true), ".", true);
            if (StringUtils.isEmpty(bookId)) {
                log.error("parse5173 ===================== selectableOne:{}", selectableOne.get());
                continue;
            }
            goods.setBookId(bookId);
            goods.setId(genSm3Id(GamePlatformEnum.PLATFORM_5173.getValue(), gameId, goods.getBookId()));
            if (bookId.startsWith("JS")) {
                goods.setSellType("寄售");
            } else {
                goods.setSellType("担保");
            }
            List<Selectable> priceNodes = node.xpath("//ul[@class='pdlist_price']/li/").nodes();
            if (CollectionUtils.isNotEmpty(priceNodes)) {
                goods.setPrice(Double.parseDouble(WebmagicUtils.getTextTrim(priceNodes.getFirst())));
            }
            List<Selectable> unitPriceNodes = node.xpath("//ul[@class='pdlist_unitprice']/li").nodes();
            if (CollectionUtils.isNotEmpty(unitPriceNodes)) {
                List<String> unitPriceList = CollectionUtils.newArrayList();
                for (Selectable unitPrice : unitPriceNodes) {
                    String str = unitPrice.xpath("//b/text()").toString();
                    if (StringUtils.isNotBlank(str) && str.contains("1元=")) {
                        goods.setUnitPrice(Double.parseDouble(str.replaceAll("1元=", "")));
                    }
                    unitPriceList.add(WebmagicUtils.getTextTrim(unitPrice));
                }
                goods.setPriceHint(CollectionUtils.join(unitPriceList, ";"));
            }
            goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
            List<Selectable> numNodes = node.xpath("//ul[@class='pdlist_num']/li").nodes();
            if (CollectionUtils.isNotEmpty(numNodes)) {
                goods.setGoodsStock(Integer.parseInt(WebmagicUtils.getTextTrim(numNodes.getFirst())));
            }
            insertList.add(goods);
        }
        if (StringUtils.isNotBlank(recordId)) {
            updateCrawlerRecord(recordId, insertList.size());
        } else if (StringUtils.isNotBlank(id)) {
            RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_5173.getValue() + StringConstants.HYPHEN + id, insertList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            updateGameGoodsUnitPrice(insertList.getFirst());
        }
    }

    private void dynamicParse373Yxb(Page page) {
        Html html = page.getHtml();
        Request request = page.getRequest();
        String recordId = request.getExtra("recordId");
        List<Selectable> nodes = html.xpath("//div[@class='goods-list-item']").nodes();
        if (CollectionUtils.isEmpty(nodes)) {
            updateCrawlerRecord(recordId, 0);
            return;
        }
        GameEntity game = request.getExtra("game");
        String id = request.getExtra("id");
        CrawlerGameEntity crawler = request.getExtra("crawler");
        String gameArea = crawler.getGameArea();
        String gameServer = crawler.getGameServer();
        String gameRace = crawler.getGameRace();
        String gameId = game.getId();
        GameGoodsEntity goods;
        String bookId;
        Selectable selectableOne;
        List<GameGoodsEntity> insertList = CollectionUtils.newArrayList();
        for (Selectable node : nodes) {
            try {
                goods = new GameGoodsEntity();
                goods.setStatus("1");
                goods.setPlatformId(GamePlatformEnum.PLATFORM_DD373.getValue());
                goods.setPlatformName(GamePlatformEnum.PLATFORM_DD373.getLabel());
                goods.setGoodsType(GameGoodsTypeEnum.GOODS_TYPE_YXB.getLabel());
                goods.setGameId(gameId);
                goods.setGameName(game.getGameName());
                goods.setGameArea(gameArea);
                goods.setGameServer(gameServer);
                goods.setGameRace(gameRace);
                selectableOne = node.xpath("//div[@class='width400']");
                if (!selectableOne.match()) {
                    log.error("parse373 ===================== node:{}", node.get());
                    continue;
                }
                goods.setTitle(WebmagicUtils.getTextTrim(selectableOne.xpath("//h2/a")));
                goods.setGoodsUrl("https://www.dd373.com" + selectableOne.xpath("//h2/a/@href").toString().trim());
                bookId = StringUtils.subBefore(StringUtils.subAfter(goods.getGoodsUrl(), "/", true), ".", true).replaceAll("detail-", "");
                if (StringUtils.isEmpty(bookId)) {
                    log.error("parse373 ===================== selectableOne:{}", selectableOne.get());
                    continue;
                }
                goods.setBookId(bookId);
                goods.setId(genSm3Id(GamePlatformEnum.PLATFORM_DD373.getValue(), gameId, goods.getBookId()));
                if (goods.getBookId().startsWith("JS")) {
                    goods.setSellType("寄售");
                } else {
                    goods.setSellType("担保");
                }
                Selectable priceSelectable = node.xpath("//div[@class='goods-price game-currency-price']/");
                if (priceSelectable.match()) {
                    goods.setPrice(Double.parseDouble(WebmagicUtils.getTextTrim(priceSelectable).replaceAll("￥", "")));
                }
                List<Selectable> unitPriceNodes = node.xpath("//div[@class='p-r66']/p").nodes();
                if (CollectionUtils.isNotEmpty(unitPriceNodes)) {
                    List<String> unitPriceList = CollectionUtils.newArrayList();
                    for (Selectable unitPrice : unitPriceNodes) {
                        String str = unitPrice.xpath("//p/text()").toString();
                        if (StringUtils.isNotBlank(str) && str.contains("1元=")) {
                            goods.setUnitPrice(Double.parseDouble(str.replaceAll("1元=", "").replaceAll("万金", "").replaceAll("币", "")));
                        }
                        unitPriceList.add(WebmagicUtils.getTextTrim(unitPrice));
                    }
                    goods.setPriceHint(CollectionUtils.join(unitPriceList, ";"));
                }
                goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
                Selectable numSelectable = node.xpath("//div[@class='kucun kucunW94']/");
                if (numSelectable.match()) {
                    goods.setGoodsStock(Integer.parseInt(WebmagicUtils.getTextTrim(numSelectable)));
                }
                insertList.add(goods);
            } catch (Exception e) {
                log.error("parse373 =============================== exception:{}", e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(recordId)) {
            updateCrawlerRecord(recordId, insertList.size());
        } else if (StringUtils.isNotBlank(id)) {
            RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_DD373.getValue() + StringConstants.HYPHEN + id, insertList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            updateGameGoodsUnitPrice(insertList.getFirst());
        }
    }

    private void dynamicParse898Yxb(Page page) {
        Html html = page.getHtml();
        Request request = page.getRequest();
        String recordId = request.getExtra("recordId");
        List<Selectable> nodes = html.xpath("//div[@id='divCommodityLst']/ul").nodes();
        if (CollectionUtils.isEmpty(nodes)) {
            updateCrawlerRecord(recordId, 0);
            return;
        }
        CrawlerGameEntity crawler = request.getExtra("crawler");
        GameEntity game = request.getExtra("game");
        String gameId = game.getId();
        String id = request.getExtra("id");
        String gameArea = crawler.getGameArea();
        String gameServer = crawler.getGameServer();
        String gameRace = crawler.getGameRace();
        GameGoodsEntity goods;
        String bookId;
        Selectable selectableOne;
        List<GameGoodsEntity> insertList = CollectionUtils.newArrayList();
        for (Selectable node : nodes) {
            try {
                goods = new GameGoodsEntity();
                goods.setStatus("1");
                goods.setPlatformId(GamePlatformEnum.PLATFORM_UU898.getValue());
                goods.setPlatformName(GamePlatformEnum.PLATFORM_UU898.getLabel());
                goods.setGoodsType(GameGoodsTypeEnum.GOODS_TYPE_YXB.getLabel());
                goods.setGameId(gameId);
                goods.setGameName(game.getGameName());
                goods.setGameArea(gameArea);
                goods.setGameServer(gameServer);
                goods.setGameRace(gameRace);
                bookId = node.xpath("//ul/@id").toString();
                if (StringUtils.isEmpty(bookId) || "lotter_box".equals(bookId)) {
                    continue;
                }
                goods.setBookId(bookId);
                goods.setId(genSm3Id(GamePlatformEnum.PLATFORM_UU898.getValue(), gameId, goods.getBookId()));
                selectableOne = node.xpath("//li[@class='sp_li0 pos']");
                if (!selectableOne.match()) {
                    log.error("parse898 ===================== selectableOne:{}", selectableOne.get());
                    continue;
                }
                goods.setGoodsUrl("https:" + selectableOne.xpath("//h2/a/@href").toString().trim());
                goods.setTitle(WebmagicUtils.getTextTrim(selectableOne.xpath("//h2/a")));
                goods.setPrice(Double.parseDouble(StringUtils.split(node.xpath("//ul/@data-jifen").toString(), "|").get(6)));
                if (bookId.contains("CN")) {
                    goods.setSellType("寄售");
                } else {
                    goods.setSellType("担保");
                }
                // 单价
                List<Selectable> unitPriceNodes = node.xpath("//li[@class='sp_li1']/h6/span").nodes();
                if (CollectionUtils.isNotEmpty(unitPriceNodes)) {
                    List<String> unitPriceList = CollectionUtils.newArrayList();
                    for (Selectable unitPrice : unitPriceNodes) {
                        String str = unitPrice.xpath("//span/text()").toString();
                        if (StringUtils.isNotBlank(str) && str.contains("1元=")) {
                            goods.setUnitPrice(Double.parseDouble(str.replaceAll("1元=", "").replaceAll("万金", "").replaceAll("币", "")));
                        }
                        unitPriceList.add(WebmagicUtils.getTextTrim(unitPrice));
                    }
                    goods.setPriceHint(CollectionUtils.join(unitPriceList, ";"));
                }
                goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
                // 库存数量
                Selectable numSelectable = node.xpath("//li[@class='sp_li3']");
                if (numSelectable.match()) {
                    goods.setGoodsStock(StringUtils.isEmpty(WebmagicUtils.getTextTrim(numSelectable)) ? 0 : Integer.parseInt(WebmagicUtils.getTextTrim(numSelectable)));
                }
                insertList.add(goods);
            } catch (Exception e) {
                log.error("parse898 =============================== exception:{}", e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(recordId)) {
            updateCrawlerRecord(recordId, insertList.size());
        } else if (StringUtils.isNotBlank(id)) {
            RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_UU898.getValue() + StringConstants.HYPHEN + id, insertList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            updateGameGoodsUnitPrice(insertList.getFirst());
        }
    }

    private void dynamicParse7881Yxb(Page page) {
        Html html = page.getHtml();
        Request request = page.getRequest();
        String recordId = request.getExtra("recordId");
        List<Selectable> nodes = html.xpath("//div[@class='list-item']").nodes();
        if (CollectionUtils.isEmpty(nodes)) {
            updateCrawlerRecord(recordId, 0);
            return;
        }
        String id = request.getExtra("id");
        GameEntity game = request.getExtra("game");
        CrawlerGameEntity crawler = request.getExtra("crawler");
        String gameArea = crawler.getGameArea();
        String gameServer = crawler.getGameServer();
        String gameRace = crawler.getGameRace();
        String gameId = game.getId();
        List<GameGoodsEntity> insertList = CollectionUtils.newArrayList();
        GameGoodsEntity goods;
        String bookId;
        Selectable selectableOne;
        for (Selectable node : nodes) {
            try {
                goods = new GameGoodsEntity();
                goods.setStatus("1");
                goods.setPlatformId(GamePlatformEnum.PLATFORM_7881.getValue());
                goods.setPlatformName(GamePlatformEnum.PLATFORM_7881.getLabel());
                goods.setGoodsType(GameGoodsTypeEnum.GOODS_TYPE_YXB.getLabel());
                goods.setGameId(gameId);
                goods.setGameName(game.getGameName());
                goods.setGameArea(gameArea);
                goods.setGameServer(gameServer);
                goods.setGameRace(gameRace);
                bookId = node.xpath("//div/@goodsid").toString();
                if (StringUtils.isEmpty(bookId)) {
                    continue;
                }
                goods.setBookId(bookId);
                goods.setId(genSm3Id(GamePlatformEnum.PLATFORM_7881.getValue(), gameId, goods.getBookId()));
                selectableOne = node.xpath("//div[@class='list-item-box']");
                if (!selectableOne.match()) {
                    log.error("parse7881 ===================== selectableOne:{}", selectableOne.get());
                    continue;
                }
                goods.setTitle(WebmagicUtils.getTextTrim(selectableOne.xpath("//div[@class='txt-box']/h2")));
                goods.setGoodsUrl("https://search.7881.com" + selectableOne.xpath("//div[@class='txt-box']/h2/a/@href").toString().trim());
                if (selectableOne.xpath("//div[@class='txt-box']/h2/a/em/@class").toString().contains("mjfh")) {
                    goods.setSellType("担保");
                } else {
                    goods.setSellType("寄售");
                }
                goods.setPrice(Double.parseDouble(WebmagicUtils.getTextTrim(selectableOne.xpath("//div[@class='list-v part-02']/h5")).replaceAll("¥", "")));
                // 单价
                Selectable unitPriceNode = selectableOne.xpath("//div[@class='list-v part-03']/");
                if (unitPriceNode.match()) {
                    List<String> unitPriceList = CollectionUtils.newArrayList();
                    unitPriceList.add(WebmagicUtils.getTextTrim(unitPriceNode.xpath("//h5")));
                    unitPriceList.add(WebmagicUtils.getTextTrim(unitPriceNode.xpath("//p")));
                    String str = WebmagicUtils.getTextTrim(unitPriceNode.xpath("//h5"));
                    if (StringUtils.isNotBlank(str) && str.contains("1元=")) {
                        goods.setUnitPrice(Double.parseDouble(str.replaceAll("1元=", "").replaceAll("金币", "")));
                    }
                    goods.setPriceHint(CollectionUtils.join(unitPriceList, ";"));
                }
                goods.setAmount(NumberUtils.round(NumberUtils.mul(goods.getUnitPrice(), goods.getPrice()), 0).doubleValue());
                // 库存数量
                List<Selectable> pNodes = selectableOne.xpath("//div[@class='txt-box']/p").nodes();
                if (CollectionUtils.isNotEmpty(pNodes)) {
                    for (Selectable p : pNodes) {
                        String str = WebmagicUtils.getTextTrim(p);
                        if (StringUtils.isNotBlank(str) && str.contains("库存：")) {
                            goods.setGoodsStock(Integer.parseInt(str.replaceAll("库存：", "")));
                            break;
                        }
                    }
                }
                insertList.add(goods);
            } catch (Exception e) {
                log.error("dynamicParse7881Yxb =============================== exception:{}", e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(recordId)) {
            updateCrawlerRecord(recordId, insertList.size());
        } else if (StringUtils.isNotBlank(id)) {
            RedisUtils.listRightPushAll(GamePlatformEnum.PLATFORM_7881.getValue() + StringConstants.HYPHEN + id, insertList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            updateGameGoodsUnitPrice(insertList.getFirst());
        }
    }

    @Override
    public Site getSite() {
        String cookies = "firstOpen_cc=true; clientId=fff6b3ddbf911e8b54f1a0dd2f83205c; acw_tc=43d2f694876f5569761f5f021144837833ebed3ce79a8f3e7d19a4acdee6ec12; dpushPC=true; Hm_lvt_b1609ca2c0a77d0130ec3cf8396eb4d5=1728535169,1729660635; HMACCOUNT=A90E4A96C2B13E73; Hm_lpvt_b1609ca2c0a77d0130ec3cf8396eb4d5=1729660744";
        List<String> arrList = StringUtils.split(cookies, ';');
        if (CollectionUtils.isNotEmpty(arrList)) {
            for (String arr : arrList) {
                if (StringUtils.isNotBlank(arr) && arr.contains("=")) {
                    site.addCookie(StringUtils.split(arr.trim(), '=').get(0), StringUtils.split(arr.trim(), '=').get(1));
                }
            }
        }
        site.addHeader("Sec-Fetch-Site", "none");
        site.addHeader("Sec-Fetch-Mode", "navigate");
        return site.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36");
    }

}
