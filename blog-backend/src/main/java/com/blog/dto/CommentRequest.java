package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 评论请求
 */
@Data
public class CommentRequest {

    @NotBlank(message = "评论内容不能为空")
    private String content;

    private Long parentId = 0L;

    private Long replyUserId;
}
