package com.shuyoutech.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperTreeService;
import com.shuyoutech.system.domain.bo.SysDictBo;
import com.shuyoutech.system.domain.entity.SysDictEntity;
import com.shuyoutech.system.domain.vo.SysDictVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 10:17:54
 **/
public interface SysDictService extends SuperTreeService<SysDictEntity, SysDictVo> {

    Query buildQuery(SysDictBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<SysDictVo> page(PageQuery<SysDictBo> pageQuery);

    SysDictVo detail(Long id);

    Long saveSysDict(SysDictBo bo);

    boolean updateSysDict(SysDictBo bo);

    boolean deleteSysDict(List<Long> ids);

    List<Tree<String>> tree(SysDictBo bo);

}
