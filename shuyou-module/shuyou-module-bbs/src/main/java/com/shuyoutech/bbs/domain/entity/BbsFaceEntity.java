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
@Document(collection = "bbs_face")
@Schema(description = "捏脸表类")
public class BbsFaceEntity extends BaseEntity<BbsFaceEntity> {

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "游戏ID")
    private String gameId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "游戏角色职业")
    private String gameRole;

    @Schema(description = "捏脸标题")
    private String faceTitle;

    @Schema(description = "封面图片")
    private String coverImgUrl;

    @Schema(description = "捏脸数据")
    private String faceData;

    @Schema(description = "查看数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "下载次数")
    private Integer downloadCount;

    @Schema(description = "评论者IP地址")
    private String ipAddress;

}
