package com.blog.service;

import com.blog.common.PageResult;
import com.blog.dto.ArticleRequest;
import com.blog.entity.Article;

import java.util.List;

public interface ArticleService {

    /** 发布文章 */
    Long publish(Long authorId, ArticleRequest request);

    /** 更新文章 */
    void update(Long articleId, Long authorId, ArticleRequest request);

    /** 删除文章 */
    void delete(Long articleId, Long authorId);

    /** 根据ID获取文章(含缓存) */
    Article getById(Long id);

    /** 分页查询文章列表 */
    PageResult<Article> list(int page, int size, Long categoryId, String keyword);

    /** 获取我的文章 */
    PageResult<Article> myArticles(Long authorId, int page, int size);

    /** 点赞文章 */
    void like(Long articleId, Long userId);

    /** 取消点赞 */
    void unLike(Long articleId, Long userId);
}
