package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 **/
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bbs_like")
@Schema(description = "点赞表")
public class BbsLikeEntity extends BaseEntity<BbsLikeEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "目标类型：1-帖子，2-评论")
    private String targetType;

    @Schema(description = "目标ID")
    private String targetId;

    @Schema(description = "点赞类型：1-点赞，2-点踩，3-收藏，4-分享，5-举报")
    private String likeType;

}
