package com.example.db.service;

import com.example.db.domain.Article;
import com.example.db.model.ArticleDTO;
import com.example.db.repos.ArticleRepository;
import com.example.db.domain.Operation;
import com.example.db.repos.OperationRepository;
import com.example.db.util.NotFoundException;
import com.example.db.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final OperationRepository operationRepository;

    public ArticleService(final ArticleRepository articleRepository,
            final OperationRepository operationRepository) {
        this.articleRepository = articleRepository;
        this.operationRepository = operationRepository;
    }

    public List<ArticleDTO> findAll() {
        final List<Article> articles = articleRepository.findAll(Sort.by("id"));
        return articles.stream()
                .map(article -> mapToDTO(article, new ArticleDTO()))
                .toList();
    }

    public ArticleDTO get(final Integer id) {
        return articleRepository.findById(id)
                .map(article -> mapToDTO(article, new ArticleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ArticleDTO articleDTO) {
        final Article article = new Article();
        mapToEntity(articleDTO, article);
        return articleRepository.save(article).getId();
    }

    public void update(final Integer id, final ArticleDTO articleDTO) {
        final Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(articleDTO, article);
        articleRepository.save(article);
    }

    public void delete(final Integer id) {
        articleRepository.deleteById(id);
    }

    private ArticleDTO mapToDTO(final Article article, final ArticleDTO articleDTO) {
        articleDTO.setId(article.getId());
        articleDTO.setName(article.getName());
        return articleDTO;
    }

    private Article mapToEntity(final ArticleDTO articleDTO, final Article article) {
        article.setName(articleDTO.getName());
        return article;
    }

    public String getReferencedWarning(final Integer id) {
        final Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Operation articleOperation = operationRepository.findFirstByArticle(article);
        if (articleOperation != null) {
            return WebUtils.getMessage("article.operation.article.referenced", articleOperation.getId());
        }
        return null;
    }

}
