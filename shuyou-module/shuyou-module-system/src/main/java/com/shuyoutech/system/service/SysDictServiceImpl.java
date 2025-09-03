package com.shuyoutech.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import com.shuyoutech.common.core.util.*;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.model.TreeOption;
import com.shuyoutech.common.web.service.SuperTreeServiceImpl;
import com.shuyoutech.system.domain.bo.SysDictBo;
import com.shuyoutech.system.domain.entity.SysDictEntity;
import com.shuyoutech.system.domain.vo.SysDictVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 10:17:54
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends SuperTreeServiceImpl<SysDictEntity, SysDictVo> implements SysDictService {

    @Override
    public List<SysDictVo> convertTo(List<SysDictEntity> list) {
        return MapstructUtils.convert(list, this.voClass);
    }

    public SysDictVo convertTo(SysDictEntity entity) {
        return convertTo(Collections.singletonList(entity)).getFirst();
    }

    @Override
    public Query buildQuery(SysDictBo bo) {
        Query query = new Query();
        return query;
    }

    @Override
    public boolean checkUnique(ParamUnique paramUnique) {
        Query query = new Query();
        query.addCriteria(Criteria.where(paramUnique.getParamCode()).is(paramUnique.getParamValue()));
        SysDictEntity role = this.selectOne(query);
        if (null == role) {
            return true;
        }
        return StringUtils.isNotBlank(paramUnique.getId()) && paramUnique.getId().equals(role.getId());
    }

    @Override
    public PageResult<SysDictVo> page(PageQuery<SysDictBo> pageQuery) {
        PageQuery<Query> page = pageQuery.buildPage();
        page.setQuery(buildQuery(pageQuery.getQuery()));
        return this.selectPageVo(page);
    }

    @Override
    public SysDictVo detail(Long id) {
        SysDictEntity entity = this.getById(id);
        return convertTo(entity);
    }

    @Override
    public Long saveSysDict(SysDictBo bo) {
        SysDictEntity entity = this.save(bo);
        return null == entity ? null : entity.getId();
    }

    @Override
    public boolean updateSysDict(SysDictBo bo) {
        return this.patch(bo);
    }

    @Override
    public boolean deleteSysDict(List<Long> ids) {
        return this.deleteByIds(ids);
    }

    @Override
    public List<Tree<String>> tree(SysDictBo bo) {
        Query query = this.buildQuery(bo);
        List<SysDictEntity> dictList = this.selectList(query);
        if (CollectionUtils.isEmpty(dictList)) {
            return CollectionUtils.newArrayList();
        }
        List<TreeOption> list = CollectionUtil.newArrayList();
        for (SysDictEntity dict : dictList) {
            list.add(TreeOption.builder() //
                    .parentId(String.valueOf(dict.getParentId())) //
                    .label(dict.getDictLabel()) //
                    .value(String.valueOf(dict.getId())) //
                    .sort(dict.getDictSort()) //
                    .extra(BeanUtils.beanToMap(dict)) //
                    .build());
        }
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("value");
        treeNodeConfig.setNameKey("label");
        return TreeUtils.build(list, "0", treeNodeConfig, (treeOption, tree) -> //
                tree.setId(treeOption.getValue()) //
                        .setParentId(treeOption.getParentId())//
                        .setName(treeOption.getLabel())//
                        .setWeight(treeOption.getSort())//
                        .putExtra("dict", treeOption.getExtra())//
        );
    }

}