package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 文章请求
 */
@Data
public class ArticleRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最多100字符")
    private String title;

    @Size(max = 300, message = "摘要最多300字符")
    private String summary;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String coverImage;

    private Long categoryId;

    /** 状态: 0草稿 1发布 */
    private Integer status = 1;
}
