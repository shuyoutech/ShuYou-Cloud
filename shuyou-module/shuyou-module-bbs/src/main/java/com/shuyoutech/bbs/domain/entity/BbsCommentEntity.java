package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author YangChao
 * @date 2025-07-07 00:27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bbs_comment")
@Schema(description = "评论表")
public class BbsCommentEntity extends BaseEntity<BbsCommentEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "帖子ID")
    private String postId;

    @Schema(description = "评论用户ID")
    private String userId;

    @Schema(description = "评论用户名称")
    private String userName;

    @Schema(description = "父评论ID，用于回复功能")
    private String parentId;

    @Schema(description = "根评论ID，用于标识评论树")
    private String rootId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "回复数")
    private Integer replyCount;

    @Schema(description = "查看数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "点踩数")
    private Integer dislikeCount;

    @Schema(description = "评论者IP地址")
    private String ipAddress;

}
