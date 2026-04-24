package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.ArticleRequest;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 文章接口
 */
@Tag(name = "文章模块")
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "发布文章")
    @PostMapping
    public Result<Long> publish(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @RequestBody ArticleRequest request) {
        Long authorId = Long.parseLong(userDetails.getUsername());
        Long articleId = articleService.publish(authorId, request);
        return Result.success(articleId);
    }

    @Operation(summary = "更新文章")
    @PutMapping("/{id}")
    public Result<Void> update(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long id,
                               @Valid @RequestBody ArticleRequest request) {
        Long authorId = Long.parseLong(userDetails.getUsername());
        articleService.update(id, authorId, request);
        return Result.success();
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long id) {
        Long authorId = Long.parseLong(userDetails.getUsername());
        articleService.delete(id, authorId);
        return Result.success();
    }

    @Operation(summary = "获取文章详情")
    @GetMapping("/{id}")
    public Result<Article> getById(@PathVariable Long id) {
        return Result.success(articleService.getById(id));
    }

    @Operation(summary = "文章列表(分页)")
    @GetMapping("/list")
    public Result<PageResult<Article>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        return Result.success(articleService.list(page, size, categoryId, keyword));
    }

    @Operation(summary = "我的文章")
    @GetMapping("/my")
    public Result<PageResult<Article>> myArticles(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Long authorId = Long.parseLong(userDetails.getUsername());
        return Result.success(articleService.myArticles(authorId, page, size));
    }

    @Operation(summary = "点赞文章")
    @PostMapping("/{id}/like")
    public Result<Void> like(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        articleService.like(id, userId);
        return Result.success();
    }

    @Operation(summary = "取消点赞")
    @DeleteMapping("/{id}/like")
    public Result<Void> unLike(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long id) {
        Long userId = Long.parseLong(userDetails.getUsername());
        articleService.unLike(id, userId);
        return Result.success();
    }
}
