package com.example.db.service;

import com.example.db.domain.Article;
import com.example.db.repos.ArticleRepository;
import com.example.db.domain.Balance;
import com.example.db.repos.BalanceRepository;
import com.example.db.domain.Operation;
import com.example.db.model.OperationDTO;
import com.example.db.repos.OperationRepository;
import com.example.db.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final BalanceRepository balanceRepository;
    private final ArticleRepository articleRepository;

    public OperationService(final OperationRepository operationRepository,
            final BalanceRepository balanceRepository, final ArticleRepository articleRepository) {
        this.operationRepository = operationRepository;
        this.balanceRepository = balanceRepository;
        this.articleRepository = articleRepository;
    }

    public List<OperationDTO> findAll() {
        final List<Operation> operations = operationRepository.findAll(Sort.by("id"));
        return operations.stream()
                .map(operation -> mapToDTO(operation, new OperationDTO()))
                .toList();
    }

    public OperationDTO get(final Integer id) {
        return operationRepository.findById(id)
                .map(operation -> mapToDTO(operation, new OperationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OperationDTO operationDTO) {
        final Operation operation = new Operation();
        mapToEntity(operationDTO, operation);
        return operationRepository.save(operation).getId();
    }

    public void update(final Integer id, final OperationDTO operationDTO) {
        final Operation operation = operationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(operationDTO, operation);
        operationRepository.save(operation);
    }

    public void delete(final Integer id) {
        operationRepository.deleteById(id);
    }

    private OperationDTO mapToDTO(final Operation operation, final OperationDTO operationDTO) {
        operationDTO.setId(operation.getId());
        operationDTO.setDebit(operation.getDebit());
        operationDTO.setCredit(operation.getCredit());
        operationDTO.setCreateDate(operation.getCreateDate());
        operationDTO.setBalance(operation.getBalance() == null ? null : operation.getBalance().getId());
        operationDTO.setArticle(operation.getArticle() == null ? null : operation.getArticle().getId());
        return operationDTO;
    }

    private Operation mapToEntity(final OperationDTO operationDTO, final Operation operation) {
        operation.setDebit(operationDTO.getDebit());
        operation.setCredit(operationDTO.getCredit());
        operation.setCreateDate(operationDTO.getCreateDate());
        final Balance balance = operationDTO.getBalance() == null ? null : balanceRepository.findById(operationDTO.getBalance())
                .orElseThrow(() -> new NotFoundException("balance not found"));
        operation.setBalance(balance);
        final Article article = operationDTO.getArticle() == null ? null : articleRepository.findById(operationDTO.getArticle())
                .orElseThrow(() -> new NotFoundException("article not found"));
        operation.setArticle(article);
        return operation;
    }

}
