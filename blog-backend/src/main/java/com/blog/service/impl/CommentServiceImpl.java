package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.common.exception.BizException;
import com.blog.dto.CommentRequest;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public void comment(Long articleId, Long userId, CommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setParentId(request.getParentId());
        comment.setReplyUserId(request.getReplyUserId());
        comment.setLikeCount(0);
        commentMapper.insert(comment);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || !comment.getUserId().equals(userId)) {
            throw new BizException("无权限删除此评论");
        }
        commentMapper.deleteById(commentId);
    }

    @Override
    public List<Comment> listByArticle(Long articleId) {
        return commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getArticleId, articleId)
                .orderByAsc(Comment::getCreatedAt));
    }

    @Override
    @Transactional
    public void like(Long commentId, Long userId) {
        commentMapper.incrementLikeCount(commentId);
    }
}
