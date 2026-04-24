package com.blog.service;

import com.blog.dto.CommentRequest;
import com.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    /** 发表评论 */
    void comment(Long articleId, Long userId, CommentRequest request);

    /** 删除评论 */
    void delete(Long commentId, Long userId);

    /** 获取文章评论列表 */
    List<Comment> listByArticle(Long articleId);

    /** 点赞评论 */
    void like(Long commentId, Long userId);
}
