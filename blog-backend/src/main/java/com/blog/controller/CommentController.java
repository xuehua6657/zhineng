package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.CommentRequest;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论接口
 */
@Tag(name = "评论模块")
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "发表评论")
    @PostMapping("/article/{articleId}")
    public Result<Void> comment(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long articleId,
                                @Valid @RequestBody CommentRequest request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        commentService.comment(articleId, userId, request);
        return Result.success();
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        commentService.delete(id, userId);
        return Result.success();
    }

    @Operation(summary = "文章评论列表")
    @GetMapping("/article/{articleId}")
    public Result<List<Comment>> list(@PathVariable Long articleId) {
        return Result.success(commentService.listByArticle(articleId));
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/{id}/like")
    public Result<Void> like(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        commentService.like(id, userId);
        return Result.success();
    }
}
