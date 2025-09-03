package com.shuyoutech.bbs.domain.bo;

import com.shuyoutech.bbs.domain.entity.BbsPostEntity;
import com.shuyoutech.common.core.model.group.StatusGroup;
import com.shuyoutech.common.core.model.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author YangChao
 * @date 2025-09-03 13:41:18
 **/
@Data
@AutoMapper(target = BbsPostEntity.class)
@Schema(description = "帖子类")
public class BbsPostBo implements Serializable {

    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class, StatusGroup.class})
    @Schema(description = "主键")
    private String id;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "状态：1-正常，2-草稿，3-审核中，4-已删除")
    private String status;

    @Schema(description = "发帖用户ID")
    private String userId;

    @Schema(description = "分类、板块")
    private String category;

    @Schema(description = "子分类、板块")
    private String subCategory;

    @Schema(description = "标签集合")
    private List<String> tags;

    @Schema(description = "帖子标题")
    private String postTitle;

    @Schema(description = "帖子内容")
    private String postContent;

    @Schema(description = "帖子摘要")
    private String postSummary;

    @Schema(description = "封面图片URL")
    private String coverImgUrl;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "评论次数")
    private Integer commentCount;

    @Schema(description = "点踩次数")
    private Integer dislikeCount;

    @Schema(description = "收藏次数")
    private Integer favoriteCount;

    @Schema(description = "分享次数")
    private Integer shareCount;

    @Schema(description = "举报次数")
    private Integer reportCount;

    @Schema(description = "是否置顶")
    private Boolean topFlag;

    @Schema(description = "是否精华")
    private Boolean essenceFlag;

    @Schema(description = "发帖IP地址")
    private String ipAddress;

}
