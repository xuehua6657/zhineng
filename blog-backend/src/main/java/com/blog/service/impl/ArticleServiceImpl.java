package com.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.PageResult;
import com.blog.common.constant.RedisConstants;
import com.blog.common.exception.BizException;
import com.blog.dto.ArticleRequest;
import com.blog.entity.Article;
import com.blog.entity.ArticleLike;
import com.blog.mapper.ArticleLikeMapper;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleLikeMapper articleLikeMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public Long publish(Long authorId, ArticleRequest request) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImage(request.getCoverImage());
        article.setCategoryId(request.getCategoryId());
        article.setAuthorId(authorId);
        article.setStatus(request.getStatus());
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        articleMapper.insert(article);
        return article.getId();
    }

    @Override
    @Transactional
    public void update(Long articleId, Long authorId, ArticleRequest request) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) throw new BizException("文章不存在");
        if (!article.getAuthorId().equals(authorId)) throw new BizException("无权限修改此文章");

        article.setTitle(request.getTitle());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImage(request.getCoverImage());
        article.setCategoryId(request.getCategoryId());
        article.setStatus(request.getStatus());
        articleMapper.updateById(article);
        safeDeleteCache(RedisConstants.ARTICLE_KEY + articleId);
    }

    @Override
    @Transactional
    public void delete(Long articleId, Long authorId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null || !article.getAuthorId().equals(authorId))
            throw new BizException("无权限删除此文章");
        articleMapper.deleteById(articleId);
        safeDeleteCache(RedisConstants.ARTICLE_KEY + articleId);
    }

    @Override
    public Article getById(Long id) {
        String key = RedisConstants.ARTICLE_KEY + id;
        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached != null) return (Article) cached;
        } catch (Exception e) {
            log.debug("Redis 缓存读取失败，直接查库");
        }

        Article article = articleMapper.selectById(id);
        if (article == null) throw new BizException("文章不存在");

        try {
            redisTemplate.opsForValue().set(key, article, 1, TimeUnit.HOURS);
        } catch (Exception ignored) {}
        return article;
    }

    @Override
    public PageResult<Article> list(int page, int size, Long categoryId, String keyword) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, 1)
                .eq(categoryId != null, Article::getCategoryId, categoryId)
                .like(StrUtil.isNotBlank(keyword), Article::getTitle, keyword)
                .orderByDesc(Article::getCreatedAt);
        Page<Article> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), page, size);
    }

    @Override
    public PageResult<Article> myArticles(Long authorId, int page, int size) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getAuthorId, authorId)
                .orderByDesc(Article::getCreatedAt);
        Page<Article> pageResult = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), page, size);
    }

    @Override
    @Transactional
    public void like(Long articleId, Long userId) {
        long count = articleLikeMapper.selectCount(new LambdaQueryWrapper<ArticleLike>()
                .eq(ArticleLike::getArticleId, articleId)
                .eq(ArticleLike::getUserId, userId));
        if (count > 0) throw new BizException("已经点赞过");

        ArticleLike like = new ArticleLike();
        like.setArticleId(articleId);
        like.setUserId(userId);
        articleLikeMapper.insert(like);
        articleMapper.incrementLikeCount(articleId);

        try {
            redisTemplate.opsForValue().set(RedisConstants.ARTICLE_LIKE_KEY + articleId + ":" + userId, "1");
        } catch (Exception ignored) {}
    }

    @Override
    @Transactional
    public void unLike(Long articleId, Long userId) {
        articleLikeMapper.delete(new LambdaQueryWrapper<ArticleLike>()
                .eq(ArticleLike::getArticleId, articleId)
                .eq(ArticleLike::getUserId, userId));
        articleMapper.decrementLikeCount(articleId);
        try {
            redisTemplate.delete(RedisConstants.ARTICLE_LIKE_KEY + articleId + ":" + userId);
        } catch (Exception ignored) {}
    }

    private void safeDeleteCache(String key) {
        try { redisTemplate.delete(key); } catch (Exception ignored) {}
    }
}
