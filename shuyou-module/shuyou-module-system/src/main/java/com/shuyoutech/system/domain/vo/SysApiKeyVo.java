package com.shuyoutech.system.domain.vo;

import com.shuyoutech.common.mongodb.model.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-08-19 16:57:40
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户API key显示类")
public class SysApiKeyVo extends BaseVo {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户类型")
    private String userType;

    @Schema(description = "名称")
    private String apiName;

    @Schema(description = "Key")
    private String apiKey;

}
