package com.shuyoutech.aigc.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YangChao
 * @date 2025-07-13 15:42
 **/
@Data
@Schema(description = "AI使用请求类")
public class AigcUsageBo implements Serializable {

    @Schema(description = "日期")
    private String date;

}
