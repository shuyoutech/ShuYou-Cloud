package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.GameGuildBo;
import com.shuyoutech.bbs.domain.entity.GameGuildEntity;
import com.shuyoutech.bbs.domain.vo.GameGuildVo;
import com.shuyoutech.common.core.util.MapstructUtils;
import com.shuyoutech.common.core.util.StringUtils;
import com.shuyoutech.common.mongodb.MongoUtils;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author YangChao
 * @date 2025-07-10 11:52:43
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class GameGuildServiceImpl extends SuperServiceImpl<GameGuildEntity, GameGuildVo> implements GameGuildService {

    @Override
    public List<GameGuildVo> convertTo(List<GameGuildEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public GameGuildVo convertTo(GameGuildEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(GameGuildBo bo) {
        Query query = new Query();
        if (StringUtils.isNotBlank(bo.getGuildName())) {
            query.addCriteria(Criteria.where("guildName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getGuildName()), Pattern.CASE_INSENSITIVE)));
        }
        if (StringUtils.isNotBlank(bo.getGameName())) {
            query.addCriteria(Criteria.where("gameName").regex(Pattern.compile(String.format("^.*%s.*$", bo.getGameName()), Pattern.CASE_INSENSITIVE)));
        }
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        GameGuildEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<GameGuildVo> page(PageQuery<GameGuildBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public GameGuildVo detail(String id) {
        GameGuildEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public String saveGameGuild(GameGuildBo bo) {
        GameGuildEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateGameGuild(GameGuildBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteGameGuild(List<String> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public boolean statusGameGuild(String id, String status) {
        GameGuildEntity entity = this.getById(id);
        if (null == entity) {
            return false;
        }
        Update update = new Update();
        update.set("status", status);
        return MongoUtils.patch(id, update, GameGuildEntity.class);
    }

}