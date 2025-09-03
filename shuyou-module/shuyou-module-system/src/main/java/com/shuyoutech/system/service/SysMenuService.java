package com.shuyoutech.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperTreeService;
import com.shuyoutech.system.domain.bo.SysMenuBo;
import com.shuyoutech.system.domain.entity.SysMenuEntity;
import com.shuyoutech.system.domain.vo.SysMenuVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-07-07 10:44:24
 **/
public interface SysMenuService extends SuperTreeService<SysMenuEntity, SysMenuVo> {

    Query buildQuery(SysMenuBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<SysMenuVo> page(PageQuery<SysMenuBo> pageQuery);

    SysMenuVo detail(Long id);

    Long saveSysMenu(SysMenuBo bo);

    boolean updateSysMenu(SysMenuBo bo);

    boolean deleteSysMenu(List<Long> ids);

    boolean statusSysMenu(Long id, String status);

    List<Tree<String>> tree(SysMenuBo bo);

}
