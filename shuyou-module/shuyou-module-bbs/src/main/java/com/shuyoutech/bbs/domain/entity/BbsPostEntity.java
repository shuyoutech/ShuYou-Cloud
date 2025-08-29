package com.shuyoutech.bbs.domain.entity;

import com.shuyoutech.bbs.domain.vo.GameVo;
import com.shuyoutech.common.mongodb.model.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
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
@AutoMapper(target = GameVo.class)
@Document(collection = "bbs_post")
@Schema(description = "帖子表")
public class BbsPostEntity extends BaseEntity<BbsPostEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "状态：1-正常，2-待审核，3-已删除，4-已隐藏，5-已锁定")
    private String status;

    @Schema(description = "发帖用户ID")
    private String userId;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "帖子标题")
    private String title;

    @Schema(description = "帖子内容")
    private String content;

    @Schema(description = "帖子类型：1-普通帖，2-精华帖，3-置顶帖，4-公告帖，5-活动帖")
    private String postType;

    @Schema(description = "回复数")
    private Integer replyCount;

    @Schema(description = "浏览数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "点踩数")
    private Integer dislikeCount;

    @Schema(description = "收藏数")
    private Integer favoriteCount;

    @Schema(description = "分享数")
    private Integer shareCount;

    @Schema(description = "举报数")
    private Integer reportCount;

    @Schema(description = "是否置顶")
    private Boolean stickyFlag;

    @Schema(description = "是否精华")
    private Boolean essenceFlag;

    @Schema(description = "是否锁定")
    private Boolean lockedFlag;

    @Schema(description = "发帖IP地址")
    private String ipAddress;

}
