package com.shuyoutech.notice.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import com.shuyoutech.notice.domain.bo.NoticeRecordBo;
import com.shuyoutech.notice.domain.entity.NoticeRecordEntity;
import com.shuyoutech.notice.domain.vo.NoticeRecordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-13 23:49:40
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeRecordServiceImpl extends SuperServiceImpl<NoticeRecordEntity, NoticeRecordVo> implements NoticeRecordService {

    @Override
    public List<NoticeRecordVo> convertTo(List<NoticeRecordEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public NoticeRecordVo convertTo(NoticeRecordEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(NoticeRecordBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public PageResult<NoticeRecordVo> page(PageQuery<NoticeRecordBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public NoticeRecordVo detail(String id) {
        NoticeRecordEntity entity = this.getById(id);
        return convertTo(entity);
    }

}