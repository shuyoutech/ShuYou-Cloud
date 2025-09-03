package com.shuyoutech.bbs.service;

import com.shuyoutech.bbs.domain.bo.BbsPostBo;
import com.shuyoutech.bbs.domain.entity.BbsPostEntity;
import com.shuyoutech.bbs.domain.vo.BbsPostVo;
import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.model.ParamUnique;
import com.shuyoutech.common.web.service.SuperService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-09-03 13:41:18
 **/
public interface BbsPostService extends SuperService<BbsPostEntity, BbsPostVo> {

    Query buildQuery(BbsPostBo bo);

    boolean checkUnique(ParamUnique paramUnique);

    PageResult<BbsPostVo> page(PageQuery<BbsPostBo> pageQuery);

    BbsPostVo detail(String id);

    String saveBbsPost(BbsPostBo bo);

    boolean updateBbsPost(BbsPostBo bo);

    boolean deleteBbsPost(List<String> ids);

    boolean statusBbsPost(String id, String status);

    String publishPost(BbsPostBo bo);

}
