package com.shuyoutech.pay.service;

import com.shuyoutech.common.web.model.PageQuery;
import com.shuyoutech.common.web.model.PageResult;
import com.shuyoutech.common.web.service.SuperService;
import com.shuyoutech.pay.domain.bo.PayRechargePackageBo;
import com.shuyoutech.pay.domain.entity.PayRechargePackageEntity;
import com.shuyoutech.pay.domain.vo.PayRechargePackageVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author YangChao
 * @date 2025-08-27 18:35:05
 **/
public interface PayRechargePackageService extends SuperService<PayRechargePackageEntity, PayRechargePackageVo> {

    Query buildQuery(PayRechargePackageBo bo);

    PageResult<PayRechargePackageVo> page(PageQuery<PayRechargePackageBo> pageQuery);

    PayRechargePackageVo detail(String id);

    String savePayRechargePackage(PayRechargePackageBo bo);

    boolean updatePayRechargePackage(PayRechargePackageBo bo);

    boolean deletePayRechargePackage(List<String> ids);

}
