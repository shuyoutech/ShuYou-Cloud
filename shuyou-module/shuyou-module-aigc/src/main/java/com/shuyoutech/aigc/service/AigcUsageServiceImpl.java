package com.shuyoutech.aigc.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
import com.shuyoutech.aigc.domain.entity.AigcConsumeRecordEntity;
import com.shuyoutech.common.core.util.CollectionUtils;
import com.shuyoutech.common.core.util.DateUtils;
import com.shuyoutech.common.core.util.FormatUtils;
import com.shuyoutech.common.satoken.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.shuyoutech.common.mongodb.MongoUtils.mongoTemplate;

/**
 * @author YangChao
 * @date 2025-08-19 20:03
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AigcUsageServiceImpl implements AigcUsageService {

    @Override
    public List<JSONObject> usage(String date) {
        List<JSONObject> result = CollectionUtils.newArrayList();
        Date startDate = DateUtils.parseDateTime(date + "-01 00:00:00").toJdkDate();
        Date endDate = DateUtils.endOfMonth(startDate).toJdkDate();
        Criteria criteria = Criteria.where("createTime").gte(startDate).lte(endDate).and("userId").is(AuthUtils.getLoginUserId());
        MatchOperation matchOperation = Aggregation.match(criteria);
        ProjectionOperation projectionOperation = Aggregation.project("createTime", "modelName", "price", "count").and("createTime").dateAsFormattedString("%Y-%m-%d").as("date");
        GroupOperation groupOperation = Aggregation.group("date", "modelName").count().as("count").sum("price").as("price");
        AggregationOptions aggregationOptions = AggregationOptions.builder().allowDiskUse(true).build();
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation, groupOperation).withOptions(aggregationOptions);
        List<JSONObject> documents = mongoTemplate.aggregate(aggregation, AigcConsumeRecordEntity.class, JSONObject.class).getMappedResults();
        if (CollectionUtil.isEmpty(documents)) {
            return result;
        }
        Map<String, List<JSONObject>> map = Maps.newHashMap();
        List<JSONObject> tempList;
        JSONObject row;
        for (JSONObject document : documents) {
            tempList = map.getOrDefault(document.getJSONObject("_id").getString("date"), CollectionUtils.newArrayList());
            row = new JSONObject();
            row.put("modelName", document.getJSONObject("_id").getString("modelName"));
            row.put("price", FormatUtils.formatDotZero(document.getString("price")));
            row.put("count", document.getLongValue("count"));
            tempList.add(row);
            map.put(document.getJSONObject("_id").getString("date"), tempList);
        }
        List<String> dayList = DateUtils.dayByMonth(date);
        for (String day : dayList) {
            row = new JSONObject();
            row.put("date", day);
            row.put("data", map.getOrDefault(day, CollectionUtils.newArrayList()));
            result.add(row);
        }
        return result;
    }

}
